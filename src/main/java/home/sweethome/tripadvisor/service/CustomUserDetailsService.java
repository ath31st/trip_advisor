package home.sweethome.tripadvisor.service;

import home.sweethome.tripadvisor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository.findByUsernameIgnoreCase(username).isEmpty())
            throw new UsernameNotFoundException("User with username " + username + " not found!");

        return userRepository.findByUsernameIgnoreCase(username).get();
    }
}
