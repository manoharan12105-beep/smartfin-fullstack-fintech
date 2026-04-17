package me.mano.full_stack_FinTech_system.repository;

import me.mano.full_stack_FinTech_system.entity.Account;
import me.mano.full_stack_FinTech_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByUser(User user);
}