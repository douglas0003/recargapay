package com.recargapay.wallet.adapters.persistence;

import com.recargapay.wallet.domain.model.Transaction;
import com.recargapay.wallet.domain.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionJpaRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByWallet(Wallet wallet);
}
