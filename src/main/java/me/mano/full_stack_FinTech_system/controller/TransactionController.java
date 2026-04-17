package me.mano.full_stack_FinTech_system.controller;

import me.mano.full_stack_FinTech_system.entity.Transaction;
import me.mano.full_stack_FinTech_system.service.TransactionService;
import me.mano.full_stack_FinTech_system.repository.TransactionRepository;
import me.mano.full_stack_FinTech_system.entity.Account;
import me.mano.full_stack_FinTech_system.entity.User;
import me.mano.full_stack_FinTech_system.repository.AccountRepository;
import me.mano.full_stack_FinTech_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin
public class TransactionController {

        @Autowired
        private TransactionService transactionService;

        @Autowired
        private TransactionRepository transactionRepository;

        @Autowired
        private AccountRepository accountRepository;

        @Autowired
        private UserRepository userRepository;

        // Get Transactions for a User
        @GetMapping("/{userId}")
        public List<Transaction> getUserTransactions(@PathVariable Long userId) {

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Account account = accountRepository.findByUser(user)
                                .orElseThrow(() -> new RuntimeException("Account not found"));

                return transactionService.getTransactionsForAccount(account.getId());
        }
}