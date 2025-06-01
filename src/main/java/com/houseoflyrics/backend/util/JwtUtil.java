package com.houseoflyrics.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import com.houseoflyrics.backend.entity.Users;
import com.houseoflyrics.backend.service.UserService;

public class JwtUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private static final long EXPIRATION_TIME = 3600000;

    public static String generateToken(String mail) {
        return Jwts.builder()
                .setSubject(mail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validateToken(String token, String mail) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject().equals(mail) && claims.getExpiration().after(new Date());
        } catch(Exception ex) {
            return false;
        }
    }

    public static Optional<Users> getUserFromToken(String token, UserService userService) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String mail = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return userService.findByMail(mail);
        } catch(Exception e) {
            return Optional.empty();
        }
    }
}
