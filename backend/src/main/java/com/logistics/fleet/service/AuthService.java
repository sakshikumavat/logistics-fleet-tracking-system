package com.logistics.fleet.service;

import com.logistics.fleet.dto.LoginRequest;
import com.logistics.fleet.dto.LoginResponse;
import com.logistics.fleet.dto.SignupRequest;
import com.logistics.fleet.entity.AppUser;
import com.logistics.fleet.repository.AppUserRepository;
import com.logistics.fleet.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email and password are required");
        }
        AppUser user = repository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"));

        if (user.getActive() == null || user.getActive() != 1
                || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token, user.getEmail(), user.getFullName(), user.getRole());
    }

    @Transactional
    public LoginResponse signup(SignupRequest request) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Registration data is required");
        }

        if (request.getFullName() == null || request.getFullName().trim().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Full name is required");
        }

        if (request.getEmail() == null || request.getEmail().trim().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email is required");
        }

        String email = request.getEmail().trim().toLowerCase();
        if (!email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid email format");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must be at least 6 characters");
        }

        if (repository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account with this email already exists.");
        }

        Long maxId = repository.findMaxUserId();
        Long newId = (maxId != null ? maxId : 0L) + 1L;

        // Security requirement: Assign safe default role on public signup.
        // Never allow users to escalate privileges to ADMIN or MANAGER via request JSON.
        String assignedRole = "DISPATCHER";
        if (request.getRole() != null) {
            String requested = request.getRole().trim().toUpperCase();
            if ("DRIVER".equals(requested)) {
                assignedRole = "DRIVER";
            }
        }

        AppUser user = new AppUser();
        user.setUserId(newId);
        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(assignedRole);
        user.setActive(1);
        user.setCreatedAt(LocalDateTime.now());

        repository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token, user.getEmail(), user.getFullName(), user.getRole());
    }

    public AppUser getUserByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"));
    }
}
