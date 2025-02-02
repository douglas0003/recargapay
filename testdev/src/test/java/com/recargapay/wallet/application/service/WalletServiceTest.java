package com.recargapay.wallet.application.service;

import com.recargapay.wallet.adapters.persistence.TransactionJpaRepository;
import com.recargapay.wallet.adapters.persistence.UserJpaRepository;
import com.recargapay.wallet.adapters.persistence.WalletJpaRepository;
import com.recargapay.wallet.domain.model.Transaction;
import com.recargapay.wallet.domain.model.User;
import com.recargapay.wallet.domain.model.Wallet;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletJpaRepository walletRepository;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private TransactionJpaRepository transactionRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void testCreateWalletAvoidingNullPointer() {
        UUID userId = UUID.randomUUID();
        User mockUser = new User("John Doe");
        mockUser.setId(userId);

        Wallet mockWallet = new Wallet(mockUser);
        mockWallet.setId(UUID.randomUUID());

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(walletRepository.save(any(Wallet.class))).thenReturn(mockWallet);

        Wallet createdWallet = assertDoesNotThrow(() -> walletService.createWallet(userId),
                "Wallet creation should not throw NullPointerException");

        assertNotNull(createdWallet, "Created wallet should not be null");
        assertNotNull(createdWallet.getId(), "Wallet ID should not be null");
        assertEquals(mockUser, createdWallet.getUser(), "Wallet user should match the expected user");

        verify(userRepository, times(1)).findById(userId);
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testDepositAvoidingNullPointer() {
        UUID walletId = UUID.randomUUID();
        BigDecimal depositAmount = BigDecimal.valueOf(100);
        User mockUser = new User("John Doe");
        Wallet mockWallet = new Wallet(mockUser);
        mockWallet.setId(walletId);

        // Mock repository behavior
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(mockWallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(mockWallet);

        assertDoesNotThrow(() -> walletService.deposit(walletId, depositAmount),
                "Deposit should not throw NullPointerException");

        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, times(1)).save(mockWallet);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testWithdrawAvoidingNullPointer() {
        UUID walletId = UUID.randomUUID();
        BigDecimal withdrawAmount = BigDecimal.valueOf(50);
        User mockUser = new User("John Doe");
        Wallet mockWallet = new Wallet(mockUser);
        mockWallet.setId(walletId);
        mockWallet.deposit(BigDecimal.valueOf(100)); // Ensuring sufficient balance

        // Mock repository behavior
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(mockWallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(mockWallet);

        assertDoesNotThrow(() -> walletService.withdraw(walletId, withdrawAmount),
                "Withdraw should not throw NullPointerException");

        verify(walletRepository, times(1)).findById(walletId);
        verify(walletRepository, times(1)).save(mockWallet);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testTransferAvoidingNullPointer() {
        UUID fromWalletId = UUID.randomUUID();
        UUID toWalletId = UUID.randomUUID();
        BigDecimal transferAmount = BigDecimal.valueOf(30);

        User user1 = new User("John Doe");
        User user2 = new User("Jane Doe");
        Wallet fromWallet = new Wallet(user1);
        Wallet toWallet = new Wallet(user2);
        fromWallet.setId(fromWalletId);
        toWallet.setId(toWalletId);
        fromWallet.deposit(BigDecimal.valueOf(100)); // Ensuring sufficient balance

        // Mock repository behavior
        when(walletRepository.findById(fromWalletId)).thenReturn(Optional.of(fromWallet));
        when(walletRepository.findById(toWalletId)).thenReturn(Optional.of(toWallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(fromWallet).thenReturn(toWallet);

        assertDoesNotThrow(() -> walletService.transfer(fromWalletId, toWalletId, transferAmount),
                "Transfer should not throw NullPointerException");

        verify(walletRepository, times(2)).findById(any(UUID.class));
        verify(walletRepository, times(2)).save(any(Wallet.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void testGetTransactionHistoryAvoidingNullPointer() {
        UUID walletId = UUID.randomUUID();
        User mockUser = new User("John Doe");
        Wallet mockWallet = new Wallet(mockUser);
        mockWallet.setId(walletId);
        Transaction mockTransaction = new Transaction(mockWallet, "DEPOSIT", BigDecimal.valueOf(50));

        // Mock repository behavior
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(mockWallet));
        when(transactionRepository.findByWallet(mockWallet)).thenReturn(List.of(mockTransaction));

        List<Transaction> transactions = assertDoesNotThrow(() -> walletService.getTransactionHistory(walletId),
                "Getting transaction history should not throw NullPointerException");

        assertNotNull(transactions, "Transaction list should not be null");
        assertFalse(transactions.isEmpty(), "Transaction list should not be empty");
        assertEquals(1, transactions.size(), "Transaction list should contain one entry");

        verify(walletRepository, times(1)).findById(walletId);
        verify(transactionRepository, times(1)).findByWallet(mockWallet);
    }
}