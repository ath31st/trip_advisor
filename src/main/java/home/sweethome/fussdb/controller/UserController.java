package home.sweethome.fussdb.controller;

import home.sweethome.fussdb.entity.User;
import home.sweethome.fussdb.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Map<String, String>> deleteHandler(@PathVariable String email) {
        return userService.deleteUser(email);
    }
}
