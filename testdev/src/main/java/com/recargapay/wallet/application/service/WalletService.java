package com.recargapay.wallet.application.service;

import com.recargapay.wallet.domain.model.Transaction;
import com.recargapay.wallet.domain.model.TransactionType;
import com.recargapay.wallet.domain.model.User;
import com.recargapay.wallet.domain.model.Wallet;
import com.recargapay.wallet.adapters.persistence.TransactionJpaRepository;
import com.recargapay.wallet.adapters.persistence.UserJpaRepository;
import com.recargapay.wallet.adapters.persistence.WalletJpaRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletJpaRepository walletRepository;
    private final UserJpaRepository userRepository;
    private final TransactionJpaRepository transactionRepository;
    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    public WalletService(WalletJpaRepository walletRepository, UserJpaRepository userRepository, TransactionJpaRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Wallet createWallet(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Wallet wallet = new Wallet(user);
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet getWallet(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
    }

    @Transactional
    public void deposit(UUID walletId, BigDecimal amount) {
        Wallet wallet = getWallet(walletId);
        wallet.deposit(amount);
        walletRepository.save(wallet);

        transactionRepository.save(new Transaction(wallet, TransactionType.DEPOSIT.name(), amount));
    }

    @Transactional
    public void withdraw(UUID walletId, BigDecimal amount) {
        Wallet wallet = getWallet(walletId);
        wallet.withdraw(amount);
        walletRepository.save(wallet);

        transactionRepository.save(new Transaction(wallet, TransactionType.WITHDRAW.name(), amount));
    }

    @Transactional
    public void transfer(UUID fromWalletId, UUID toWalletId, BigDecimal amount) {
        if (fromWalletId.equals(toWalletId)) {
            throw new IllegalArgumentException("Cannot transfer to the same wallet.");
        }

        Wallet fromWallet = getWallet(fromWalletId);
        Wallet toWallet = getWallet(toWalletId);

        fromWallet.withdraw(amount);
        toWallet.deposit(amount);

        try {
            walletRepository.save(fromWallet);
            walletRepository.save(toWallet);

            transactionRepository.save(new Transaction(fromWallet, TransactionType.TRANSFER_OUT.name(), amount));
            transactionRepository.save(new Transaction(toWallet, TransactionType.TRANSFER_IN.name(), amount));
        } catch (OptimisticLockException e) {
            throw new IllegalStateException("Transfer failed, please try again.");
        }
    }

    @Transactional
    public List<Transaction> getTransactionHistory(UUID walletId) {
        Wallet wallet = getWallet(walletId);
        return transactionRepository.findByWallet(wallet);
    }
}