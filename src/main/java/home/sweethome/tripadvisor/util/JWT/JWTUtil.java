package home.sweethome.tripadvisor.util.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.refresh_secret}")
    private String refreshSecret;

    public String generateAccessToken(String email) throws IllegalArgumentException, JWTCreationException {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();

        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withExpiresAt(accessExpirationInstant)
                .withIssuer("Trip Advisor")
                .sign(Algorithm.HMAC256(secret));
    }

    public String generateRefreshToken(String email) throws IllegalArgumentException, JWTCreationException {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();

        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withExpiresAt(refreshExpirationInstant)
                .withIssuer("Trip Advisor")
                .sign(Algorithm.HMAC256(refreshSecret));
    }

    public String validateTokenAndRetrieveSubject(String token)throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("Trip Advisor")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }

}