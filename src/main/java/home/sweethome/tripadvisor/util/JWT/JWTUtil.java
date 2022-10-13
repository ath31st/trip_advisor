package home.sweethome.tripadvisor.util.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import home.sweethome.tripadvisor.entity.PayloadRandomPiece;
import home.sweethome.tripadvisor.mongorepository.PayloadRandomPiecesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JWTUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.refresh_secret}")
    private String refreshSecret;

    private final PayloadRandomPiecesRepository payloadRandomPiecesRepository;

    public String generateAccessToken(String email) throws IllegalArgumentException, JWTCreationException {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(50).atZone(ZoneId.systemDefault()).toInstant();

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
        String uuid = UUID.randomUUID().toString();

        payloadRandomPiecesRepository.save(new PayloadRandomPiece(email, uuid));

        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withExpiresAt(refreshExpirationInstant)
                .withIssuer("Trip Advisor")
                .withPayload(Collections.singletonMap("UUID", uuid))
                .sign(Algorithm.HMAC256(refreshSecret));
    }

    public String validateAccessTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("Trip Advisor")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }

    public String validateRefreshTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(refreshSecret))
                .withSubject("User Details")
                .withIssuer("Trip Advisor")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        String username = jwt.getClaim("email").asString();
        if (payloadRandomPiecesRepository.findByUsernameIgnoreCase(username) == null)
            //throw new JWTVerificationException("Payload piece not found!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is deleted. You need to login!");

        String savedUuid = payloadRandomPiecesRepository.findByUsernameIgnoreCase(username).getUuid();

        if (!savedUuid.equals(jwt.getClaim("UUID").asString())) {
            throw new JWTVerificationException("Invalid token UUID");
        }
        return username;
    }

    public void deletePayloadRandomPieces(String username) {
        PayloadRandomPiece piece = payloadRandomPiecesRepository.findByUsernameIgnoreCase(username);
        if (piece != null) {
            payloadRandomPiecesRepository.delete(payloadRandomPiecesRepository.findByUsernameIgnoreCase(username));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "payload piece not found!");
        }
    }

}