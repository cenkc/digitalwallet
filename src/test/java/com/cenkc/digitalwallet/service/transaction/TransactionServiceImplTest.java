package com.cenkc.digitalwallet.service.transaction;

import com.cenkc.digitalwallet.entity.*;
import com.cenkc.digitalwallet.entity.dto.TransactionRequestDTO;
import com.cenkc.digitalwallet.entity.dto.TransactionResponseDTO;
import com.cenkc.digitalwallet.exception.BadRequestException;
import com.cenkc.digitalwallet.exception.ResourceNotFoundException;
import com.cenkc.digitalwallet.repository.TransactionRepository;
import com.cenkc.digitalwallet.repository.WalletRepository;
import com.cenkc.digitalwallet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserService userService;

    @Mock
    private TransactionProcessorFactory transactionProcessorFactory;

    @Mock
    private TransactionProcessor transactionProcessor;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User user;
    private Wallet wallet;
    private Role adminRole;
    private TransactionRequestDTO depositRequest;
    private TransactionRequestDTO withdrawRequest;
    private Transaction depositTransaction;
    private Transaction withdrawTransaction;

    @BeforeEach
    void setUp() {
        // Setup user
        adminRole = new Role(1L, "ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRoles(roles);

        // Setup wallet
        wallet = new Wallet();
        wallet.setId(1L);
        wallet.setUser(user);
        wallet.setWalletName("Test Wallet");
        wallet.setCurrencyType(CurrencyType.USD);
        wallet.setBalance(new BigDecimal("1000.00"));
        wallet.setUsableBalance(new BigDecimal("900.00"));
        wallet.setActiveForWithdraw(true);
        wallet.setActiveForShopping(true);

        // Setup deposit request
        depositRequest = new TransactionRequestDTO();
        depositRequest.setWalletId(1L);
        depositRequest.setAmount(new BigDecimal("500.00"));
        depositRequest.setType(TransactionType.DEPOSIT);
        depositRequest.setOppositePartyType(OppositePartyType.IBAN);
        depositRequest.setOppositePartyIdentifier("TR330006100519786457841326");

        // Setup withdraw request
        withdrawRequest = new TransactionRequestDTO();
        withdrawRequest.setWalletId(1L);
        withdrawRequest.setAmount(new BigDecimal("300.00"));
        withdrawRequest.setType(TransactionType.WITHDRAW);
        withdrawRequest.setOppositePartyType(OppositePartyType.PAYMENT);
        withdrawRequest.setOppositePartyIdentifier("PAY123456789");

        // Setup deposit transaction
        depositTransaction = Transaction.builder()
                .id(1L)
                .wallet(wallet)
                .amount(new BigDecimal("500.00"))
                .type(TransactionType.DEPOSIT)
                .oppositePartyType(OppositePartyType.IBAN)
                .oppositePartyIdentifier("TR330006100519786457841326")
                .status(TransactionStatusType.APPROVED)
                .build();

        // Setup withdraw transaction
        withdrawTransaction = Transaction.builder()
                .id(2L)
                .wallet(wallet)
                .amount(new BigDecimal("300.00"))
                .type(TransactionType.WITHDRAW)
                .oppositePartyType(OppositePartyType.PAYMENT)
                .oppositePartyIdentifier("PAY123456789")
                .status(TransactionStatusType.APPROVED)
                .build();
    }

    @Test
    void processTransaction_Deposit_Success() {
        // Given
        when(userService.getCurrentUser()).thenReturn(user);
        when(walletRepository.findByIdWithLock(1L)).thenReturn(Optional.of(wallet));
        when(transactionProcessorFactory.getProcessor(TransactionType.DEPOSIT)).thenReturn(transactionProcessor);
        doNothing().when(transactionProcessor).process(any(Transaction.class), any(Wallet.class));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(depositTransaction);

        // When
        TransactionResponseDTO response = transactionService.processTransaction(depositRequest);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(new BigDecimal("500.00"), response.getAmount());
        assertEquals(TransactionType.DEPOSIT, response.getType());
        assertEquals(OppositePartyType.IBAN, response.getOppositePartyType());
        assertEquals("TR330006100519786457841326", response.getOppositePartyIdentifier());
        assertEquals(TransactionStatusType.APPROVED, response.getStatus());

        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void processTransaction_Withdraw_Success() {
        // Given
        when(userService.getCurrentUser()).thenReturn(user);
        when(walletRepository.findByIdWithLock(1L)).thenReturn(Optional.of(wallet));
        when(transactionProcessorFactory.getProcessor(TransactionType.WITHDRAW)).thenReturn(transactionProcessor);
        doNothing().when(transactionProcessor).process(any(Transaction.class), any(Wallet.class));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(withdrawTransaction);

        // When
        TransactionResponseDTO response = transactionService.processTransaction(withdrawRequest);

        // Then
        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals(new BigDecimal("300.00"), response.getAmount());
        assertEquals(TransactionType.WITHDRAW, response.getType());
        assertEquals(OppositePartyType.PAYMENT, response.getOppositePartyType());
        assertEquals("PAY123456789", response.getOppositePartyIdentifier());
        assertEquals(TransactionStatusType.APPROVED, response.getStatus());

        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void processTransaction_WithOppositePartyWallet_Success() {
        // Given
        Wallet oppositeWallet = new Wallet();
        oppositeWallet.setId(2L);
        oppositeWallet.setUser(user);
        oppositeWallet.setCurrencyType(CurrencyType.USD);

        TransactionRequestDTO requestWithOppositeWallet = new TransactionRequestDTO();
        requestWithOppositeWallet.setWalletId(1L);
        requestWithOppositeWallet.setAmount(new BigDecimal("200.00"));
        requestWithOppositeWallet.setType(TransactionType.WITHDRAW);
        requestWithOppositeWallet.setOppositePartyType(OppositePartyType.PAYMENT);
        requestWithOppositeWallet.setOppositePartyWalletId(2L);

        Transaction transactionWithOppositeWallet = Transaction.builder()
                .id(3L)
                .wallet(wallet)
                .amount(new BigDecimal("200.00"))
                .type(TransactionType.WITHDRAW)
                .oppositePartyType(OppositePartyType.PAYMENT)
                .oppositePartyWallet(oppositeWallet)
                .status(TransactionStatusType.APPROVED)
                .build();

        when(userService.getCurrentUser()).thenReturn(user);
        when(walletRepository.findByIdWithLock(1L)).thenReturn(Optional.of(wallet));
        when(walletRepository.findById(2L)).thenReturn(Optional.of(oppositeWallet));
        when(transactionProcessorFactory.getProcessor(TransactionType.WITHDRAW)).thenReturn(transactionProcessor);
        doNothing().when(transactionProcessor).process(any(Transaction.class), any(Wallet.class));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionWithOppositeWallet);

        // When
        TransactionResponseDTO response = transactionService.processTransaction(requestWithOppositeWallet);

        // Then
        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals(2L, response.getOppositePartyWalletId());

        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void processTransaction_InsufficientFunds_ThrowsException() {
        // Given
        TransactionRequestDTO largeWithdrawRequest = new TransactionRequestDTO();
        largeWithdrawRequest.setWalletId(1L);
        largeWithdrawRequest.setAmount(new BigDecimal("1000.00")); // More than usable balance
        largeWithdrawRequest.setType(TransactionType.WITHDRAW);
        largeWithdrawRequest.setOppositePartyType(OppositePartyType.PAYMENT);

        when(userService.getCurrentUser()).thenReturn(user);
        when(walletRepository.findByIdWithLock(1L)).thenReturn(Optional.of(wallet));

        // When & Then
        assertThrows(BadRequestException.class, () ->
                transactionService.processTransaction(largeWithdrawRequest));
    }

    @Test
    void processTransaction_WalletNotFound_ThrowsException() {
        // Given
        when(userService.getCurrentUser()).thenReturn(user);
        when(walletRepository.findByIdWithLock(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () ->
                transactionService.processTransaction(depositRequest));
    }

    @Test
    void processTransaction_OppositePartyWalletNotFound_ThrowsException() {
        // Given
        TransactionRequestDTO requestWithInvalidOppositeWallet = new TransactionRequestDTO();
        requestWithInvalidOppositeWallet.setWalletId(1L);
        requestWithInvalidOppositeWallet.setAmount(new BigDecimal("200.00"));
        requestWithInvalidOppositeWallet.setType(TransactionType.WITHDRAW);
        requestWithInvalidOppositeWallet.setOppositePartyType(OppositePartyType.PAYMENT);
        requestWithInvalidOppositeWallet.setOppositePartyWalletId(999L); // Non-existent wallet

        when(userService.getCurrentUser()).thenReturn(user);
        when(walletRepository.findByIdWithLock(1L)).thenReturn(Optional.of(wallet));
        when(walletRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () ->
                transactionService.processTransaction(requestWithInvalidOppositeWallet));
    }

    @Test
    void getTransactionsByWalletId_Success() {
        // Given
        when(userService.getCurrentUser()).thenReturn(user);
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(transactionRepository.findByWalletId(1L)).thenReturn(Collections.singletonList(depositTransaction));

        // When
        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByWalletId(1L);

        // Then
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(1L, transactions.get(0).getId());
        assertEquals(TransactionType.DEPOSIT, transactions.get(0).getType());
    }

    @Test
    void approveTransaction_Success() {
        // Given
        Transaction pendingTransaction = Transaction.builder()
                .id(3L)
                .wallet(wallet)
                .amount(new BigDecimal("1500.00"))
                .type(TransactionType.DEPOSIT)
                .oppositePartyType(OppositePartyType.IBAN)
                .oppositePartyIdentifier("TR330006100519786457841326")
                .status(TransactionStatusType.PENDING)
                .build();

        when(transactionRepository.findById(3L)).thenReturn(Optional.of(pendingTransaction));
        when(walletRepository.findByIdWithLock(1L)).thenReturn(Optional.of(wallet));
        when(transactionProcessorFactory.getProcessor(TransactionType.DEPOSIT)).thenReturn(transactionProcessor);
        doNothing().when(transactionProcessor).processStatusChange(
                any(Transaction.class), any(Wallet.class), eq(TransactionStatusType.APPROVED));

        Transaction approvedTransaction = Transaction.builder()
                .id(3L)
                .wallet(wallet)
                .amount(new BigDecimal("1500.00"))
                .type(TransactionType.DEPOSIT)
                .oppositePartyType(OppositePartyType.IBAN)
                .oppositePartyIdentifier("TR330006100519786457841326")
                .status(TransactionStatusType.APPROVED)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(approvedTransaction);

        // When
        TransactionResponseDTO response = transactionService.approveTransaction(3L, TransactionStatusType.APPROVED);

        // Then
        assertNotNull(response);
        assertEquals(TransactionStatusType.APPROVED, response.getStatus());
        verify(transactionRepository).save(any(Transaction.class));
        verify(walletRepository).save(wallet);
    }
}