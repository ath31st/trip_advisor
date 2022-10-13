package home.sweethome.tripadvisor.service;

import home.sweethome.tripadvisor.dto.ChangePassDTO;
import home.sweethome.tripadvisor.dto.Jwt.JwtResponse;
import home.sweethome.tripadvisor.dto.UserDTO;
import home.sweethome.tripadvisor.entity.User;
import home.sweethome.tripadvisor.exceptionhandler.exception.UserServiceException;
import home.sweethome.tripadvisor.repository.UserRepository;
import home.sweethome.tripadvisor.util.JWT.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
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

    public ResponseEntity<JwtResponse> saveUser(User user) {
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

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    public User getByUsername(String username) {
        if (userRepository.findByUsernameIgnoreCase(username).isEmpty())
            throw new UsernameNotFoundException("User with username: " + username + " nor found!");

        return userRepository.findByUsernameIgnoreCase(username).get();
    }

    public ResponseEntity<Map<String, String>> deleteUser(String username) {
        if (userRepository.findByUsernameIgnoreCase(username).isEmpty())
            throw new UserServiceException(HttpStatus.NOT_FOUND, "User with username: " + username + " nor found!");

        userRepository.delete(userRepository.findByUsernameIgnoreCase(username).get());
        return ResponseEntity.ok().body(Collections.singletonMap("status", "User with username " + username + " successful deleted!"));
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    public UserDTO showAuthUser(Principal principal) {
        User user = userRepository.findByUsernameIgnoreCase(principal.getName()).orElseThrow(
                () -> new UserServiceException(HttpStatus.NOT_FOUND, "User not found!"));
        return UserDTO.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .registerDate(user.getRegisterDate())
                .roles(user.getRoles())
                .build();
    }

    private void checkExistingUser(User user) {
        if (userRepository.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            throw new UserServiceException(HttpStatus.CONFLICT, "User with username: " + user.getUsername() + " already exists!");
        }
    }

    public ResponseEntity<Map<String, String>> changePassword(ChangePassDTO changePassDTO, Principal principal) {
        if (changePassDTO.getOldPass().equals(changePassDTO.getNewPass()))
            throw new UserServiceException(HttpStatus.BAD_REQUEST, "Passwords don't be equals");

        User user = userRepository.findByUsernameIgnoreCase(principal.getName()).orElseThrow(
                () -> new UserServiceException(HttpStatus.NOT_FOUND, "User not found!"));
        if (bCryptPasswordEncoder.matches(changePassDTO.getOldPass(), user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(changePassDTO.getNewPass()));
            userRepository.save(user);

            return ResponseEntity.ok(Collections.singletonMap("status", "password successfully changed"));
        } else {
            throw new UserServiceException(HttpStatus.BAD_REQUEST, "Wrong old password");
        }

    }

}
