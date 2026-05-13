package com.craftsmanship.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")   private String secret;
    @Value("${jwt.expiration}") private long expiration;

    private Key key() { return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); }

    public String generate(String subject) {
        return Jwts.builder().setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
    }

    public String subject(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean valid(String token) {
        try { Jwts.parserBuilder().
                setSigningKey(key()).build().
                parseClaimsJws(token); return true; }
        catch (Exception e) { return false; }
    }
}
