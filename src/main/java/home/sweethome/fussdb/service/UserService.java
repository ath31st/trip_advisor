package home.sweethome.fussdb.service;

import home.sweethome.fussdb.entity.User;
import home.sweethome.fussdb.repository.UserRepository;
import home.sweethome.fussdb.util.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static home.sweethome.fussdb.util.Role.ROLE_USER;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, Object> saveUser(User user) {
        checkExistingUser(user);

        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(encodedPass);
        user.setRoles(Collections.singletonList(ROLE_USER));
        user.setRegisterDate(LocalDateTime.now());
        user = userRepository.save(user);
        return Collections.singletonMap("success", user);
    }

    public ResponseEntity<Map<String, String>> deleteUser(String email) {
        if (userRepository.findByEmailIgnoreCase(email).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email: " + email + " nor found!");

        userRepository.delete(userRepository.findByEmailIgnoreCase(email).get());
        return ResponseEntity.ok().body(Collections.singletonMap("status", "User with email " + email + " successful deleted!"));
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userRepository.findByEmailIgnoreCase(email).isEmpty())
            throw new UsernameNotFoundException("Invalid User");

        User user = userRepository.findByEmailIgnoreCase(email).get();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

    private void checkExistingUser(User user) {
        if (userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with name: " + user.getEmail() + " already exists!");
        }
    }
}
