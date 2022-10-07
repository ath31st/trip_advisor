package home.sweethome.tripadvisor.service;

import home.sweethome.tripadvisor.dto.Jwt.JwtResponse;
import home.sweethome.tripadvisor.dto.LoginCredentials;
import home.sweethome.tripadvisor.entity.User;
import home.sweethome.tripadvisor.util.JWT.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Login Credentials");
        }
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
