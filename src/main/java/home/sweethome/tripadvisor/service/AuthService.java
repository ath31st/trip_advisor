package home.sweethome.tripadvisor.service;

import home.sweethome.tripadvisor.dto.Jwt.JwtResponse;
import home.sweethome.tripadvisor.dto.LoginCredentials;
import home.sweethome.tripadvisor.entity.User;
import home.sweethome.tripadvisor.exceptionhandler.exception.LoginCredentialException;
import home.sweethome.tripadvisor.util.JWT.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JwtResponse login(LoginCredentials authRequest) {
        final User user = userService.getByUsername(authRequest.getEmail());
        if (bCryptPasswordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtUtil.generateAccessToken(user.getUsername());
            final String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new LoginCredentialException(HttpStatus.BAD_REQUEST, "Invalid login credentials");
        }
    }

    public ResponseEntity<Map<String, String>> logout(Principal principal) {
        jwtUtil.deletePayloadRandomPieces(principal.getName());
        return ResponseEntity.ok().body(Collections.singletonMap("status", "You successfully logout!"));
    }

    public JwtResponse getAccessToken(String refreshToken) {
        final String login = jwtUtil.validateRefreshTokenAndRetrieveSubject(refreshToken);

        final User user = userService.getByUsername(login);
        final String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        return new JwtResponse(accessToken, null);
    }

    public JwtResponse getRefreshToken(String refreshToken) {
        final String login = jwtUtil.validateRefreshTokenAndRetrieveSubject(refreshToken);

        final User user = userService.getByUsername(login);
        final String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        final String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        return new JwtResponse(accessToken, newRefreshToken);
    }

}
