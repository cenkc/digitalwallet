package com.cenkc.digitalwallet.service;

import com.cenkc.digitalwallet.entity.CurrencyType;
import com.cenkc.digitalwallet.entity.User;
import com.cenkc.digitalwallet.entity.Wallet;
import com.cenkc.digitalwallet.entity.dto.WalletRequestDTO;
import com.cenkc.digitalwallet.entity.dto.WalletResponseDTO;
import com.cenkc.digitalwallet.exception.ResourceNotFoundException;
import com.cenkc.digitalwallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private WalletServiceImpl walletService;

    private User testUser;
    private WalletRequestDTO walletRequestDTO;
    private Wallet testWallet;

    @BeforeEach
    void setUp() {
        // Set default values for wallet service properties
        ReflectionTestUtils.setField(walletService, "defaultBalance", new BigDecimal("0.00"));
        ReflectionTestUtils.setField(walletService, "defaultUsableBalance", new BigDecimal("100.00"));

        // Create a test user
        testUser = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .tckn("12345678901")
                .username("testuser")
                .email("test@example.com")
                .password("encoded_password")
                .build();

        // Create a wallet request DTO
        walletRequestDTO = new WalletRequestDTO();
        walletRequestDTO.setWalletName("Test Wallet");
        walletRequestDTO.setCurrencyType(CurrencyType.TRY);
        walletRequestDTO.setActiveForShopping(true);
        walletRequestDTO.setActiveForWithdraw(true);

        // Create a test wallet
        testWallet = Wallet.builder()
                .id(1L)
                .user(testUser)
                .walletName("Test Wallet")
                .currencyType(CurrencyType.TRY)
                .activeForShopping(true)
                .activeForWithdraw(true)
                .balance(new BigDecimal("0.00"))
                .usableBalance(new BigDecimal("100.00"))
                .transactions(new HashSet<>())
                .build();
    }

    @Nested
    @DisplayName("addWallet() Tests")
    class AddWalletTests {

        @Test
        @DisplayName("Should successfully add a new wallet")
        void addWallet_ShouldAddWallet_WhenValidRequest() {
            // Arrange
            when(userService.getCurrentUser()).thenReturn(testUser);
            when(walletRepository.save(any(Wallet.class))).thenReturn(testWallet);

            // Act
            WalletResponseDTO result = walletService.addWallet(walletRequestDTO);

            // Assert
            assertNotNull(result);
            assertEquals(walletRequestDTO.getWalletName(), result.getWalletName());
            assertEquals(walletRequestDTO.getCurrencyType(), result.getCurrencyType());
            assertEquals(walletRequestDTO.isActiveForShopping(), result.isActiveForShopping());
            assertEquals(walletRequestDTO.isActiveForWithdraw(), result.isActiveForWithdraw());

            // Verify the Wallet object created
            ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
            verify(walletRepository).save(walletCaptor.capture());
            Wallet capturedWallet = walletCaptor.getValue();

            assertEquals(walletRequestDTO.getWalletName(), capturedWallet.getWalletName());
            assertEquals(walletRequestDTO.getCurrencyType(), capturedWallet.getCurrencyType());
            assertEquals(walletRequestDTO.isActiveForShopping(), capturedWallet.isActiveForShopping());
            assertEquals(walletRequestDTO.isActiveForWithdraw(), capturedWallet.isActiveForWithdraw());
            assertEquals(testUser, capturedWallet.getUser());
            assertEquals(new BigDecimal("0.00"), capturedWallet.getBalance());
            assertEquals(new BigDecimal("100.00"), capturedWallet.getUsableBalance());
        }

        @Test
        @DisplayName("Should use custom default balance values when provided")
        void addWallet_ShouldUseCustomDefaultValues_WhenConfigured() {
            // Arrange
            BigDecimal customDefaultBalance = new BigDecimal("50.00");
            BigDecimal customDefaultUsableBalance = new BigDecimal("25.00");

            ReflectionTestUtils.setField(walletService, "defaultBalance", customDefaultBalance);
            ReflectionTestUtils.setField(walletService, "defaultUsableBalance", customDefaultUsableBalance);

            Wallet customWallet = Wallet.builder()
                    .id(1L)
                    .user(testUser)
                    .walletName("Test Wallet")
                    .currencyType(CurrencyType.TRY)
                    .activeForShopping(true)
                    .activeForWithdraw(true)
                    .balance(customDefaultBalance)
                    .usableBalance(customDefaultUsableBalance)
                    .transactions(new HashSet<>())
                    .build();

            when(userService.getCurrentUser()).thenReturn(testUser);
            when(walletRepository.save(any(Wallet.class))).thenReturn(customWallet);

            // Act
            walletService.addWallet(walletRequestDTO);

            // Verify the Wallet object created with custom values
            ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
            verify(walletRepository).save(walletCaptor.capture());
            Wallet capturedWallet = walletCaptor.getValue();

            assertEquals(customDefaultBalance, capturedWallet.getBalance());
            assertEquals(customDefaultUsableBalance, capturedWallet.getUsableBalance());
        }
    }

    @Nested
    @DisplayName("updateWallet() Tests")
    class UpdateWalletTests {

        @Test
        @DisplayName("Should successfully update an existing wallet")
        void updateWallet_ShouldUpdateWallet_WhenWalletExists() {
            // Arrange
            Long walletId = 1L;

            // Create an updated wallet request
            WalletRequestDTO updatedWalletRequest = new WalletRequestDTO();
            updatedWalletRequest.setWalletName("Updated Wallet");
            updatedWalletRequest.setCurrencyType(CurrencyType.USD);
            updatedWalletRequest.setActiveForShopping(false);
            updatedWalletRequest.setActiveForWithdraw(false);

            // Create the updated wallet that will be returned after saving
            Wallet updatedWallet = Wallet.builder()
                    .id(walletId)
                    .user(testUser)
                    .walletName(updatedWalletRequest.getWalletName())
                    .currencyType(updatedWalletRequest.getCurrencyType())
                    .activeForShopping(updatedWalletRequest.isActiveForShopping())
                    .activeForWithdraw(updatedWalletRequest.isActiveForWithdraw())
                    .balance(testWallet.getBalance())
                    .usableBalance(testWallet.getUsableBalance())
                    .transactions(new HashSet<>())
                    .build();

            when(walletRepository.findById(walletId)).thenReturn(Optional.of(testWallet));
            when(walletRepository.save(any(Wallet.class))).thenReturn(updatedWallet);

            // Act
            WalletResponseDTO result = walletService.updateWallet(walletId, updatedWalletRequest);

            // Assert
            assertNotNull(result);
            assertEquals(updatedWalletRequest.getWalletName(), result.getWalletName());
            assertEquals(updatedWalletRequest.getCurrencyType(), result.getCurrencyType());
            assertEquals(updatedWalletRequest.isActiveForShopping(), result.isActiveForShopping());
            assertEquals(updatedWalletRequest.isActiveForWithdraw(), result.isActiveForWithdraw());

            // Verify the Wallet object updated
            ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
            verify(walletRepository).save(walletCaptor.capture());
            Wallet capturedWallet = walletCaptor.getValue();

            assertEquals(walletId, capturedWallet.getId()); // ID should remain the same
            assertEquals(updatedWalletRequest.getWalletName(), capturedWallet.getWalletName());
            assertEquals(updatedWalletRequest.getCurrencyType(), capturedWallet.getCurrencyType());
            assertEquals(updatedWalletRequest.isActiveForShopping(), capturedWallet.isActiveForShopping());
            assertEquals(updatedWalletRequest.isActiveForWithdraw(), capturedWallet.isActiveForWithdraw());

            // Balance and user should remain unchanged
            assertEquals(testWallet.getBalance(), capturedWallet.getBalance());
            assertEquals(testWallet.getUsableBalance(), capturedWallet.getUsableBalance());
            assertEquals(testWallet.getUser(), capturedWallet.getUser());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when wallet not found")
        void updateWallet_ShouldThrowException_WhenWalletNotFound() {
            // Arrange
            Long nonExistentWalletId = 999L;
            when(walletRepository.findById(nonExistentWalletId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class,
                    () -> walletService.updateWallet(nonExistentWalletId, walletRequestDTO));

            // Verify
            verify(walletRepository).findById(nonExistentWalletId);
            verify(walletRepository, never()).save(any(Wallet.class));
        }
    }
}