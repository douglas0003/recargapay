package com.recargapay.wallet.adapters.persistence;

import com.recargapay.wallet.domain.model.Wallet;
import com.recargapay.wallet.domain.ports.WalletRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class WalletJpaRepository {

    private final WalletRepositoryPort walletRepositoryPort;

    public WalletJpaRepository(WalletRepositoryPort walletRepositoryPort) {
        this.walletRepositoryPort = walletRepositoryPort;
    }

    public Wallet save(Wallet wallet) {
        return walletRepositoryPort.save(wallet);
    }

    public Optional<Wallet> findById(UUID walletId) {
        return walletRepositoryPort.findById(walletId);
    }
}
