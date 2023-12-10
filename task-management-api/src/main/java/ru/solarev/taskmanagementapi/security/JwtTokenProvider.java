package ru.solarev.taskmanagementapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.solarev.taskmanagementapi.service.properties.JwtProperties;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean hasClaim(String token, String claimName) {
        final Claims claims = extractAllClaims(token);
        return claims.get(claimName) != null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private byte[] getSigningKey() {
        return Base64.getDecoder().decode(jwtProperties.getSecret());
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails);
    }

    public String generateToken(UserDetails userDetails,
                                Map<String, Object> claims) {
        return createToken(claims, userDetails);
    }

    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()
                        + TimeUnit.HOURS.toMillis(jwtProperties.getAccess())))
                .signWith(SignatureAlgorithm.HS256, getSigningKey()).compact();
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));

    }

}
