package com.project.AuthService.Controller;

import com.project.AuthService.Componenet.JwtHelper;
import com.project.AuthService.DTO.JwtRequest;
import com.project.AuthService.DTO.JwtResponse;
import com.project.AuthService.DTO.SignupRequest;
import com.project.AuthService.Entity.AppUser;
import com.project.AuthService.Repository.UserRepository;
import com.project.AuthService.Service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        AppUser u = new AppUser();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole("USER");
        userRepo.save(u);
        return ResponseEntity.ok("User registered");
    }

//    @PostMapping("/login")
//    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
//        try {
//            var authToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
//            authenticationManager.authenticate(authToken);
//        } catch (BadCredentialsException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtResponse(null, "Invalid credentials"));
//        }
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
//        String token = jwtHelper.generateToken(userDetails);
//        return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername()));
//    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        // Fetch user from DB
        AppUser user = userRepo.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse(null, "User not found"));
        }

        // Validate password using BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse(null, "Invalid credentials"));
        }

        // Create UserDetails manually (or use a mapper)
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER") // or load from DB
                .build();

        // Generate JWT token
        String token = jwtHelper.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token, user.getUsername()));
    }

    // Optional: validate endpoint that returns 200 if valid
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null) return ResponseEntity.badRequest().body("token required");
        try {
            String username = jwtHelper.getUsernameFromToken(token);
            UserDetails ud = userDetailsService.loadUserByUsername(username);
            boolean valid = jwtHelper.validateToken(token, ud);
            if (valid) {
                return ResponseEntity.ok(Map.of("valid", true, "username", username));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false, "error", e.getMessage()));
        }
    }
}
