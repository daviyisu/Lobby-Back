package com.lobby.app.model.auth;

import com.lobby.app.exception.InvalidRegisterException;
import com.lobby.app.exception.UserAlreadyExistsException;
import com.lobby.app.jwt.JwtService;
import com.lobby.app.model.User;
import com.lobby.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.lobby.app.utils.GlobalVariables.INPUT_USERNAME_MAX_LENGTH;
import static com.lobby.app.utils.GlobalVariables.INPUT_USERNAME_MIN_LENGTH;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) throws UserAlreadyExistsException {

        if (request.getUsername().length() < INPUT_USERNAME_MIN_LENGTH
                || request.getUsername().length() > INPUT_USERNAME_MAX_LENGTH
                || request.getUsername().isEmpty()
                || request.getPassword().isEmpty())
                {
            throw new InvalidRegisterException("Invalid register credentials");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}
