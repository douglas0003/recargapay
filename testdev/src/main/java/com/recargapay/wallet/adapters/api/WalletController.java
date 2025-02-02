package com.recargapay.wallet.adapters.api;

import com.recargapay.wallet.application.service.WalletService;
import com.recargapay.wallet.domain.model.Transaction;
import com.recargapay.wallet.domain.model.Wallet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
@Tag(name = "Wallet API", description = "Digital wallet management")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(summary = "Create a new wallet", description = "Creates a digital wallet for a specific user.")
    @PostMapping("/create/{userId}")
    public ResponseEntity<Wallet> createWallet(@PathVariable UUID userId) {
        Wallet wallet = walletService.createWallet(userId);
        return ResponseEntity.ok(wallet);
    }

    @Operation(summary = "Get wallet balance", description = "Returns the details of a wallet by its ID.")
    @GetMapping("/{walletId}")
    public ResponseEntity<Wallet> getWalletBalance(@PathVariable UUID walletId) {
        return ResponseEntity.ok(walletService.getWallet(walletId));
    }

    @Operation(summary = "Deposit money", description = "Adds an amount to the wallet.")
    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable UUID walletId, @RequestParam BigDecimal amount) {
        walletService.deposit(walletId, amount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Withdraw money", description = "Removes an amount from the wallet.")
    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable UUID walletId, @RequestParam BigDecimal amount) {
        walletService.withdraw(walletId, amount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Transfer money", description = "Transfers an amount from one wallet to another.")
    @PostMapping("/{fromWalletId}/transfer/{toWalletId}")
    public ResponseEntity<Void> transfer(
            @PathVariable UUID fromWalletId,
            @PathVariable UUID toWalletId,
            @RequestParam BigDecimal amount) {
        walletService.transfer(fromWalletId, toWalletId, amount);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get transaction history", description = "Returns all transactions of a wallet.")
    @GetMapping("/{walletId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable UUID walletId) {
        return ResponseEntity.ok(walletService.getTransactionHistory(walletId));
    }
}