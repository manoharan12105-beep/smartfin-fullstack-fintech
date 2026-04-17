package me.mano.full_stack_FinTech_system.controller;

import me.mano.full_stack_FinTech_system.dto.LoginRequest;
import me.mano.full_stack_FinTech_system.entity.User;
import me.mano.full_stack_FinTech_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return userService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.createUser(user);
    }
}