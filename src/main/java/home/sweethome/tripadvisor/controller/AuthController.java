package home.sweethome.tripadvisor.controller;

import home.sweethome.tripadvisor.dto.LoginCredentials;
import home.sweethome.tripadvisor.dto.UserDTO;
import home.sweethome.tripadvisor.entity.User;
import home.sweethome.tripadvisor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials credentials) {
        return userService.getToken(credentials);
    }

    @GetMapping("/me")
    public UserDTO showUser() {
        return userService.showAuthUser();
    }
}
