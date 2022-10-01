package home.sweethome.fussdb.controller;

import home.sweethome.fussdb.dto.LoginCredentials;
import home.sweethome.fussdb.entity.User;
import home.sweethome.fussdb.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody LoginCredentials credentials) {
        return userService.getToken(credentials);
    }
}
