package me.mano.full_stack_FinTech_system.service;

import me.mano.full_stack_FinTech_system.entity.User;
import me.mano.full_stack_FinTech_system.repository.UserRepository;
import me.mano.full_stack_FinTech_system.exception.ResourceNotFoundException;
import me.mano.full_stack_FinTech_system.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        }
        throw new BadRequestException("Invalid email or password");
    }

    public User createUser(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }
        return userRepository.save(user);
    }
}