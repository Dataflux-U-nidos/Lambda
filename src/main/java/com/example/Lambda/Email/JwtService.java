package com.example.Lambda.Email;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String SECRET_KEY = "c9d8e7f6a5b4c3d2e1f0a1b2c3d4e5f60718293"; 

    public String generateToken(String email) {
        long expirationTimeMs = 4 * 60 * 60 * 1000; // 4 horas

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
