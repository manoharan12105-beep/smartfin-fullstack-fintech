package me.mano.full_stack_FinTech_system.controller;

import me.mano.full_stack_FinTech_system.entity.Account;
import me.mano.full_stack_FinTech_system.entity.User;
import me.mano.full_stack_FinTech_system.repository.AccountRepository;
import me.mano.full_stack_FinTech_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    // Get account using userId
    @GetMapping("/{userId}")
    public Account getAccountByUserId(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    //  Get balance only
    @GetMapping("/balance/{userId}")
    public Double getBalance(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return account.getBalance();
    }
}