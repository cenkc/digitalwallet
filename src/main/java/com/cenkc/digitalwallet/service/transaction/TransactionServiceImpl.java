package com.cenkc.digitalwallet.service.transaction;

import com.cenkc.digitalwallet.entity.*;
import com.cenkc.digitalwallet.entity.dto.TransactionRequestDTO;
import com.cenkc.digitalwallet.entity.dto.TransactionResponseDTO;
import com.cenkc.digitalwallet.exception.BadRequestException;
import com.cenkc.digitalwallet.exception.ResourceNotFoundException;
import com.cenkc.digitalwallet.repository.TransactionRepository;
import com.cenkc.digitalwallet.repository.WalletRepository;
import com.cenkc.digitalwallet.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final BigDecimal PENDING_THRESHOLD = new BigDecimal("1000.00");

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserService userService;
    private final TransactionProcessorFactory transactionProcessorFactory;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  UserService userService,
                                  WalletRepository walletRepository,
                                  TransactionProcessorFactory transactionProcessorFactory) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.walletRepository = walletRepository;
        this.transactionProcessorFactory = transactionProcessorFactory;
    }

    @Override
    @Transactional
    public TransactionResponseDTO processTransaction(TransactionRequestDTO requestDTO) {
        // Validate the wallet exists and belongs to the current user
        // Use pessimistic locking to prevent concurrent modifications
        Wallet wallet = validateAndGetWalletWithLock(requestDTO.getWalletId());

        // Handle opposite party wallet if provided
        Wallet oppositePartyWallet = null;
        if (requestDTO.getOppositePartyWalletId() != null) {
            oppositePartyWallet = walletRepository.findById(requestDTO.getOppositePartyWalletId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    String.format(
                                            "Opposite party wallet not found with ID: %d",
                                            requestDTO.getOppositePartyWalletId())));

            // For withdrawals to another wallet, check if currencies match
            // PS:  Since there are only two TransactionTypes (DEPOSIT and WITHDRAW),
            //      here treating a WITHDRAW as also transferring money between one's own wallets
            if (requestDTO.getType() == TransactionType.WITHDRAW &&
                    !wallet.getCurrencyType().equals(oppositePartyWallet.getCurrencyType())) {
                throw new BadRequestException("Currency mismatch: Cannot transfer between wallets with different currencies");
            }
        }

        // Create transaction
        Transaction transaction = createTransactionFromRequest(requestDTO, wallet, oppositePartyWallet);

        // Get the appropriate processor for this transaction type
        TransactionProcessor processor = transactionProcessorFactory.getProcessor(transaction.getType());

        // Process the transaction
        processor.process(transaction, wallet);

        // Save the transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Save the updated wallet
        walletRepository.save(wallet);

        // Return response
        return convertToDTO(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponseDTO approveTransaction(Long transactionId, TransactionStatusType status) {
        // Find the transaction
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + transactionId));

        // Validate if transaction is in PENDING state
        if (transaction.getStatus() != TransactionStatusType.PENDING) {
            throw new BadRequestException("Only PENDING transactions can be approved or denied");
        }

        // Validate new status is either APPROVED or DENIED
        if (status != TransactionStatusType.APPROVED && status != TransactionStatusType.DENIED) {
            throw new BadRequestException("Status must be either APPROVED or DENIED");
        }

        // Get the wallet with a pessimistic lock
        Long walletId = transaction.getWallet().getId();
        Wallet wallet = walletRepository.findByIdWithLock(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with ID: " + walletId));

        // Get the appropriate processor
        TransactionProcessor processor = transactionProcessorFactory.getProcessor(transaction.getType());

        // Update transaction status
        transaction.setStatus(status);

        // Process the status change
        processor.processStatusChange(transaction, wallet, status);

        // Save the transaction and wallet
        Transaction savedTransaction = transactionRepository.save(transaction);
        walletRepository.save(wallet);

        return convertToDTO(savedTransaction);
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByWalletId(Long walletId) {
        Wallet wallet = validateAndGetWallet(walletId);
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        return transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Wallet validateAndGetWallet(Long walletId) {
        // Get current user
        User currentUser = userService.getCurrentUser();

        // Find the wallet
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with ID: " + walletId));

        // Check if the wallet belongs to the current user or if user is an admin
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

        if (!isAdmin && !wallet.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You don't have permission to access this wallet");
        }

        return wallet;
    }

    /**
     * Validates access to wallet and returns it with a pessimistic lock to prevent concurrent modifications
     */
    private Wallet validateAndGetWalletWithLock(Long walletId) {
        // Get current user
        User currentUser = userService.getCurrentUser();

        // Find the wallet with pessimistic lock
        Wallet wallet = walletRepository.findByIdWithLock(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with ID: " + walletId));

        // Check if the wallet belongs to the current user or if user is an admin
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

        if (!isAdmin && !wallet.getUser().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You don't have permission to access this wallet");
        }

        return wallet;
    }

    private Transaction createTransactionFromRequest(TransactionRequestDTO requestDTO, Wallet wallet, Wallet oppositePartyWallet) {
        // Validate if there's enough balance for withdrawal
        if (requestDTO.getType() == TransactionType.WITHDRAW) {
            if (wallet.getUsableBalance().compareTo(requestDTO.getAmount()) < 0) {
                throw new BadRequestException("Insufficient funds in wallet for withdrawal");
            }
        }

        // Determine transaction status based on amount
        TransactionStatusType status = determineTransactionStatus(requestDTO.getAmount());

        // Create and return the transaction
        return Transaction.builder()
                .wallet(wallet)
                .amount(requestDTO.getAmount())
                .type(requestDTO.getType())
                .oppositePartyType(requestDTO.getOppositePartyType())
                .oppositePartyIdentifier(requestDTO.getOppositePartyIdentifier())
                .oppositePartyWallet(oppositePartyWallet)
                .status(status)
                .build();
    }

    private TransactionStatusType determineTransactionStatus(BigDecimal amount) {
        return amount.compareTo(PENDING_THRESHOLD) > 0
                ? TransactionStatusType.PENDING
                : TransactionStatusType.APPROVED;
    }

    private TransactionResponseDTO convertToDTO(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .walletId(transaction.getWallet().getId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .oppositePartyType(transaction.getOppositePartyType())
                .oppositePartyIdentifier(transaction.getOppositePartyIdentifier())
                .oppositePartyWalletId(transaction.getOppositePartyWallet() != null ?
                        transaction.getOppositePartyWallet().getId() : null)
                .status(transaction.getStatus())
                .build();
    }

}
