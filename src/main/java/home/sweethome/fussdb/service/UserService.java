package home.sweethome.fussdb.service;

import home.sweethome.fussdb.dto.LoginCredentials;
import home.sweethome.fussdb.entity.User;
import home.sweethome.fussdb.repository.UserRepository;
import home.sweethome.fussdb.util.JWT.JWTUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static home.sweethome.fussdb.util.Role.ROLE_USER;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authManager;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       JWTUtil jwtUtil,
                       AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
    }

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

        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername());

        return Collections.singletonMap("token", token);
    }

    public Map<String, Object> getToken(LoginCredentials credentials) {
        try {
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());

            authManager.authenticate(authInputToken);

            String token = jwtUtil.generateToken(credentials.getEmail());

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

    private void checkExistingUser(User user) {
        if (userRepository.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with name: " + user.getUsername() + " already exists!");
        }
    }
}
