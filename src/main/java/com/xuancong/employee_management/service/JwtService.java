package com.xuancong.employee_management.service;

import com.xuancong.employee_management.enums.TokenType;
import com.xuancong.employee_management.model.User;
import com.xuancong.employee_management.model.UserRole;
import com.xuancong.employee_management.repository.UserRoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRoleRepository userRoleRepository;

    @Value("${jwt.signerKey}")
    private  String SECRET;
    private static final long ACCESS_TOKEN_EXP_MS = 15 * 60 * 1000;
    private static final long REFRESH_TOKEN_EXP_MS = 7L * 24 * 60 * 60 * 1000;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(User user, TokenType tokenType) {
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        List<String> roles  = userRoles.stream().map(userRole ->
                        userRole.getRole().getName())
                .toList();
        long exp = (tokenType == TokenType.ACCESS)
                ? ACCESS_TOKEN_EXP_MS
                : REFRESH_TOKEN_EXP_MS;

        JwtBuilder builder = Jwts.builder()
                .subject(user.getId().toString())
                .claim("type", tokenType.name().toLowerCase())

                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + exp))
                .signWith(getSignKey());

        if (tokenType == TokenType.REFRESH) {
            builder.id(UUID.randomUUID().toString());
        }else {
            builder.claim("roles", roles);
        }

        return builder.compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean verifyJwt(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSignKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public long getAccessTokenExpirationSeconds() {
        return ACCESS_TOKEN_EXP_MS / 1000;
    }






}
