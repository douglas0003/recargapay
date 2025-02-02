package com.recargapay.wallet.adapters.api;

import com.recargapay.wallet.application.service.WalletService;
import com.recargapay.wallet.domain.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private UUID walletId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
        walletId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    void testCreateWallet() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.ZERO);

        when(walletService.createWallet(userId)).thenReturn(wallet);

        mockMvc.perform(post("/api/wallets/create/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(0));

        verify(walletService, times(1)).createWallet(userId);
    }

    @Test
    void testGetWalletBalance() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));

        when(walletService.getWallet(walletId)).thenReturn(wallet);

        mockMvc.perform(get("/api/wallets/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(100));

        verify(walletService, times(1)).getWallet(walletId);
    }

    @Test
    void testDeposit() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(100);

        mockMvc.perform(post("/api/wallets/{walletId}/deposit", walletId)
                        .param("amount", amount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(walletService, times(1)).deposit(walletId, amount);
    }

    @Test
    void testWithdraw() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(50);

        mockMvc.perform(post("/api/wallets/{walletId}/withdraw", walletId)
                        .param("amount", amount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(walletService, times(1)).withdraw(walletId, amount);
    }

    @Test
    void testTransfer() throws Exception {
        UUID fromWalletId = UUID.randomUUID();
        UUID toWalletId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(30);

        mockMvc.perform(post("/api/wallets/{fromWalletId}/transfer/{toWalletId}", fromWalletId, toWalletId)
                        .param("amount", amount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(walletService, times(1)).transfer(fromWalletId, toWalletId, amount);
    }
}
