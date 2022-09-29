package home.sweethome.fussdb.controller;

import home.sweethome.fussdb.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody User user) {
        return null;
    }

    @PostMapping("/login")
    public Map<String, Object> loginHandler(@RequestBody User user) {
        return null;
    }
}
