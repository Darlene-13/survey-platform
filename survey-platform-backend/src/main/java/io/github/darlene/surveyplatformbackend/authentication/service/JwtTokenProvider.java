package io.github.darlene.surveyplatformbackend.authentication.service;


import io.github.darlene.surveyplatformbackend.authentication.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    // The JWT subject is the user's email address.
    public String generateToken(String email, UserRole role){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiry)
                .claim("role", role.name())
                .signWith(getSigningKey())
                .compact();


    }

    // Validate jwt token
    public boolean validateJwtToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e){
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    // Extract the user's email address from the JWT subject.
    public String getEmailFromToken(String token){
        return extractAllClaims(token).getSubject();
    }

    public String getRoleFromToken(String token){
        return extractAllClaims(token).get("role", String.class);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey(){
        byte[] bytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }

}
