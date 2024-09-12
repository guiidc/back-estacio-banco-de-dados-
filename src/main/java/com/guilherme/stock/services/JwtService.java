package com.guilherme.stock.services;

import com.guilherme.stock.entities.UserApp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {


    @Value("${jwt.secret}")
    private String secretKey;
    private final long expirationTimeInMillis = 86_400_000 * 20; // 20 dias
    private final String issuer = "vvl-serigrafia";

    public SecretKey getSecretKey() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String generateToken(UserApp user) {
        return this.generateToken(user, Map.of());
    }

    public String generateToken(UserApp user, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getEmail())
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(this.getSecretKey())
                .compact();
    }

    public String getSubjectFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String token, String givenEmail) {
        String tokenEmail = this.getSubjectFromToken(token);
        return tokenEmail.equals(givenEmail) && !isTokenExpired(token);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        return getClaimFromToken(token, Claims::getExpiration).before(new Date());
    }
}
