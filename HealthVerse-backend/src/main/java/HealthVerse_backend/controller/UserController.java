package HealthVerse_backend.controller;

import HealthVerse_backend.dto.LoginRequest;
import HealthVerse_backend.dto.LoginResponse;
import HealthVerse_backend.dto.UserResponse;
import HealthVerse_backend.model.User;
import HealthVerse_backend.repository.UserRepository;
import HealthVerse_backend.service.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @GetMapping
    public List<UserResponse> getUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findAll()
                .stream()
                .filter(u -> u.getEmail().equals(request.getEmail()))
                .findFirst()
                .orElse(null);

        if (user != null &&
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword())) {

           String token = jwtService.generateToken(user); 
            return new LoginResponse("Login successful", token);
        }

        return new LoginResponse("Invalid email or password", null);
    }
}