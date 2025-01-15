package mk.tradesense.tradesense.controller;

import mk.tradesense.tradesense.data_transfer.LoginRequest;
import mk.tradesense.tradesense.data_transfer.LoginResponse;
import mk.tradesense.tradesense.data_transfer.RegisterRequest;
import mk.tradesense.tradesense.jason_web.JwtUtils;
import mk.tradesense.tradesense.model.UserEntity;
import mk.tradesense.tradesense.model.enumerations.Role;
import mk.tradesense.tradesense.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authManager;
    private final JwtUtils tokenGenerator;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository userRepo;

    public AuthenticationController(AuthenticationManager authManager, JwtUtils tokenGenerator, PasswordEncoder passwordEncoder, AccountRepository userRepo) {
        this.authManager = authManager;
        this.tokenGenerator = tokenGenerator;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registrationRequest) {
        if (userRepo.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken.");
        }

        if (userRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already registered.");
        }

        UserEntity newUser = new UserEntity(
                registrationRequest.getUsername(),
                registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                Role.ROLE_USER
        );

        userRepo.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginDetails) {
        Authentication authResult;
        try {
            authResult = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDetails.getUsername(), loginDetails.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid username or password.");
            errorResponse.put("status", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);
        UserEntity authenticatedUser = (UserEntity) authResult.getPrincipal();
        String jwt = tokenGenerator.generateTokenFromUsername(authenticatedUser);

        LoginResponse response = new LoginResponse(jwt, authenticatedUser.getUsername(), Role.ROLE_USER);
        return ResponseEntity.ok(response);
    }

    // Optional: Implement logout if needed
    // @PostMapping("/logout")
    // public ResponseEntity<?> logout(HttpServletRequest request) {
    //     // Implement logout logic
    //     return ResponseEntity.ok("User logged out successfully.");
    // }
}
