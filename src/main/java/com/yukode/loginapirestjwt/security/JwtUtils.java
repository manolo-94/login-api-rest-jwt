package com.yukode.loginapirestjwt.security;

//import io.jsonwebtoken.*;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

    private final SecretKey jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final int jwtExpirationMs = 86400000; // 1 day in milliseconds

    public String generateJwtToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(jwtSecret, SignatureAlgorithm.HS512) // Updated method signature
                .compact();
    }

    public String getEmailFromJwToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public Boolean validateJwtToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // You could log the exception here for debugging purposes
            return false;
        }

    }

}
