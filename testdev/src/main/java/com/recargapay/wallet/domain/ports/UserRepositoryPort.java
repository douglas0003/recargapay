package com.recargapay.wallet.domain.ports;


import com.recargapay.wallet.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepositoryPort extends JpaRepository<User, UUID> {
}
