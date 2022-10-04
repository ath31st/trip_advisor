package home.sweethome.tripadvisor.service;

import home.sweethome.tripadvisor.dto.LoginCredentials;
import home.sweethome.tripadvisor.dto.UserDTO;
import home.sweethome.tripadvisor.entity.RefreshToken;
import home.sweethome.tripadvisor.entity.User;
import home.sweethome.tripadvisor.repository.RefreshTokenRepository;
import home.sweethome.tripadvisor.repository.UserRepository;
import home.sweethome.tripadvisor.util.JWT.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static home.sweethome.tripadvisor.util.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final RefreshTokenRepository refreshTokenRepository;

    public Map<String, Object> saveUser(User user) {
        checkExistingUser(user);

        String encodedPass = bCryptPasswordEncoder.encode(user.getPassword());
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(encodedPass);
        user.setRoles(Collections.singletonList(ROLE_USER));
        user.setRegisterDate(LocalDateTime.now());

        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        userRepository.save(user);

        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        refreshTokenRepository.save(new RefreshToken(user.getUsername(),refreshToken));
        return Map.of("access token", accessToken, "refresh token", refreshToken);
    }

    public Map<String, Object> getToken(LoginCredentials credentials) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateAccessToken(credentials.getEmail());

            return Collections.singletonMap("token", token);
        } catch (AuthenticationException authExc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Login Credentials");
        }
    }

    public User getByUsername(String username) {
        if (userRepository.findByUsernameIgnoreCase(username).isEmpty())
            throw new UsernameNotFoundException("User with username: " + username + " nor found!");

        return userRepository.findByUsernameIgnoreCase(username).get();
    }

    public ResponseEntity<Map<String, String>> deleteUser(String username) {
        if (userRepository.findByUsernameIgnoreCase(username).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with username: " + username + " nor found!");

        userRepository.delete(userRepository.findByUsernameIgnoreCase(username).get());
        return ResponseEntity.ok().body(Collections.singletonMap("status", "User with username " + username + " successful deleted!"));
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    public UserDTO showAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userRepository.findByUsernameIgnoreCase((String) authentication.getPrincipal()).isPresent()) {
            User user = userRepository.findByUsernameIgnoreCase((String) authentication.getPrincipal()).get();
            return UserDTO.builder()
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .username(user.getUsername())
                    .registerDate(user.getRegisterDate())
                    .roles(user.getRoles())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You are not registered yet!");
        }
    }

    private void checkExistingUser(User user) {
        if (userRepository.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with name: " + user.getUsername() + " already exists!");
        }
    }
}
