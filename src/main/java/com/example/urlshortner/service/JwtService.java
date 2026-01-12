package com.example.urlshortner.service;

import com.example.urlshortner.security.userDetails.JwtUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.urlshortner.UrlShortenerConstants.*;

@Service
public class JwtService {

    private final int jwtExpirationMs;
    private final String jwtSecret;
    private SecretKey key;

    public JwtService(CachedPropertyService cachedPropertyService) {
        this.jwtExpirationMs = Integer.parseInt(cachedPropertyService.getAppProperty(JWT_EXPIRATION).getValue());
        this.jwtSecret = cachedPropertyService.getAppProperty(JWT_SECRET).getValue();
    }

    // Initializes the key after the class is instantiated and the jwtSecret is injected,
    // preventing the repeated creation of the key and enhancing performance
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT token
    public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(username)
                .claim(ROLES, roles)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    // Validate JWT token
    public JwtUserDetails validateJwtToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            String username = claims.getSubject();

            Object rolesObj = claims.get(ROLES);
            List<? extends GrantedAuthority> roles = List.of();
            if (rolesObj instanceof Collection) {
                roles = ((List<Map<String, String>>) rolesObj).stream()
                        .map(map -> new SimpleGrantedAuthority(map.get(AUTHORITY)))
                        .collect(Collectors.toList());
            }

            return new JwtUserDetails(username, roles);
        } catch (SecurityException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return null;
    }
}
