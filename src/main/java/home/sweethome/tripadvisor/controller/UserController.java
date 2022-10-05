package home.sweethome.tripadvisor.controller;

import home.sweethome.tripadvisor.dto.UserDTO;
import home.sweethome.tripadvisor.entity.User;
import home.sweethome.tripadvisor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Map<String, String>> deleteHandler(@PathVariable String email) {
        return userService.deleteUser(email);
    }

    @GetMapping("/me")
    public UserDTO showUser() {
        return userService.showAuthUser();
    }
}
