package me.mano.full_stack_FinTech_system.service;

import me.mano.full_stack_FinTech_system.entity.Account;
import me.mano.full_stack_FinTech_system.entity.Transaction;
import me.mano.full_stack_FinTech_system.entity.User;
import me.mano.full_stack_FinTech_system.repository.AccountRepository;
import me.mano.full_stack_FinTech_system.repository.TransactionRepository;
import me.mano.full_stack_FinTech_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Transaction> getTransactionsForAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}