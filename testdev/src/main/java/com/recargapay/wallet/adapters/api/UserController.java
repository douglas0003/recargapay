package com.recargapay.wallet.adapters.api;

import com.recargapay.wallet.application.service.UserService;
import com.recargapay.wallet.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user", description = "Creates a user for used for new wallets.")
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestParam String name) {
        return ResponseEntity.ok(userService.createUser(name));
    }
}
