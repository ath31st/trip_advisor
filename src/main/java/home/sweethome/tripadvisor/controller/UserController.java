package home.sweethome.tripadvisor.controller;

import home.sweethome.tripadvisor.dto.ChangePassDTO;
import home.sweethome.tripadvisor.dto.Jwt.JwtResponse;
import home.sweethome.tripadvisor.dto.UserDTO;
import home.sweethome.tripadvisor.entity.User;
import home.sweethome.tripadvisor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> registerHandler(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Map<String, String>> deleteHandler(@PathVariable @NotBlank String email) {
        return userService.deleteUser(email);
    }

    @GetMapping("/me")
    public UserDTO showUser(Principal principal) {
        return userService.showAuthUser(principal);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> passwordHandler(@RequestBody ChangePassDTO changePassDTO, Principal principal) {
        return userService.changePassword(changePassDTO, principal);
    }

}
