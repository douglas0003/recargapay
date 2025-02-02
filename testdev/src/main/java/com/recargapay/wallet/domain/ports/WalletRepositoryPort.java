package com.recargapay.wallet.domain.ports;

import com.recargapay.wallet.domain.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface WalletRepositoryPort extends JpaRepository<Wallet, UUID> {
}