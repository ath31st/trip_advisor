package home.sweethome.tripadvisor.controller;

import home.sweethome.tripadvisor.dto.Jwt.JwtResponse;
import home.sweethome.tripadvisor.dto.Jwt.RefreshJwtRequest;
import home.sweethome.tripadvisor.dto.LoginCredentials;
import home.sweethome.tripadvisor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginHandler(@RequestBody LoginCredentials credentials) {
        final JwtResponse token = authService.login(credentials);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
