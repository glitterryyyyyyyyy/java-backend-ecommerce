package com.shreya.java_backend.service;

import com.shreya.java_backend.model.User;
import com.shreya.java_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ========== Register ==========
    public User register(User user) {
        String sanitizedEmail = sanitizeEmail(user.getEmail());
        user.setEmail(sanitizedEmail);

        if (userRepository.findByEmail(sanitizedEmail).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        user.setVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Default role

        // OTP setup
        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        // Send OTP via email
        emailService.sendEmail(
            sanitizedEmail,
            "Verify Your Email - OTP",
            "Your OTP for email verification is: " + otp
        );

        return userRepository.save(user);
    }

    // ========== Login ==========
    public User login(String email, String password) {
        String sanitizedEmail = sanitizeEmail(email);
        Optional<User> optionalUser = userRepository.findByEmail(sanitizedEmail);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found.");
        }

        User user = optionalUser.get();

        if (!user.isVerified()) {
            throw new RuntimeException("Email not verified. Please verify your email before logging in.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Incorrect password.");
        }

        return user;
    }

    // ========== Verify OTP ==========
    public boolean verifyOtp(String email, String otp) {
        String sanitizedEmail = sanitizeEmail(email);
        Optional<User> optionalUser = userRepository.findByEmail(sanitizedEmail);

        if (optionalUser.isEmpty()) return false;

        User user = optionalUser.get();

        if (user.getOtp() == null || !user.getOtp().equals(otp)) return false;
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) return false;

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        // Send welcome email
        String subject = "Welcome to Nebula Technologies Labs!";
        String body = "Hi " + user.getName() + ",\n\nWelcome to Nebula Technologies Labs! We're excited to have you onboard.\n\n- Team Nebula";
        emailService.sendEmail(user.getEmail(), subject, body);

        return true;
    }

    // ========== Get User By Email ==========
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(sanitizeEmail(email)).orElse(null);
    }

    // ========== OTP Generator ==========
    private String generateOTP() {
        int otp = 100000 + new Random().nextInt(900000); // Generates a 6-digit OTP
        return String.valueOf(otp);
    }

    // ========== Email Sanitizer ==========
    private String sanitizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
