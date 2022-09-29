package home.sweethome.fussdb.service;

import home.sweethome.fussdb.entity.User;
import home.sweethome.fussdb.repository.UserRepository;
import home.sweethome.fussdb.util.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid User");

        } else {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            for (Role role : user.getRoles()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
            }

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);

        }
    }
}
