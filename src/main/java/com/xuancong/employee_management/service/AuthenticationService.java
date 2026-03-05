package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.auth.AuthRequest;
import com.xuancong.employee_management.dto.auth.AuthenticationResponse;
import com.xuancong.employee_management.dto.auth.LogoutRequest;
import com.xuancong.employee_management.dto.auth.RefreshRequest;
import com.xuancong.employee_management.enums.TokenType;
import com.xuancong.employee_management.exception.BadCredentialsException;
import com.xuancong.employee_management.exception.UnauthorizedException;
import com.xuancong.employee_management.model.RefreshToken;
import com.xuancong.employee_management.model.User;
import com.xuancong.employee_management.repository.RefreshTokenRepository;
import com.xuancong.employee_management.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private  final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationResponse login(AuthRequest authRequest){
        System.out.println(authRequest.getUsername());
        System.out.println(authRequest.getPassword());

        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() ->
                        new BadCredentialsException(Constants.ErrorKey.USER_NOT_FOUND));

        boolean authenticated =
                passwordEncoder.matches(authRequest.getPassword(), user.getPassword());

        if(!authenticated){
            throw new BadCredentialsException(Constants.ErrorKey.PASSWORD_NOT_VALID);
        }

        String accessToken  = jwtService.generateToken(user, TokenType.ACCESS);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH);

        long expiresIn = jwtService.getAccessTokenExpirationSeconds();

        String jit = jwtService.parseClaims(refreshToken).getId();

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .id(jit)
                        .refreshToken(refreshToken)
                        .user(user)
                        .build()
        );

        return new AuthenticationResponse(
                accessToken,
                refreshToken,
                expiresIn,
                "role"
        );
    }


    public AuthenticationResponse refresh(RefreshRequest refreshRequest){
        String refreshToken = refreshRequest.refreshToken();
        if(!jwtService.verifyJwt(refreshToken)){
            throw new UnauthorizedException(Constants.ErrorKey.REFRESH_TOKEN_INVALID, refreshToken);
        }
        Claims claimsJwt = jwtService.parseClaims(refreshToken);

        String jti = claimsJwt.getId();
        String type = claimsJwt.get("type", String.class);

        if (jti == null || type == null) {
            throw new UnauthorizedException("Invalid token");
        }

        if (!TokenType.REFRESH.name().toLowerCase().equals(type)) {
            throw new UnauthorizedException("Invalid token type");
        }
        RefreshToken refreshSaved = refreshTokenRepository.findById(jti)
                .orElseThrow(()-> new UnauthorizedException("Refresh token revoked"));

        User user = refreshSaved.getUser();
        String newAccessToken = jwtService.generateToken(user, TokenType.ACCESS);
        String newRefreshToken = jwtService.generateToken(user,TokenType.REFRESH);

        refreshTokenRepository.delete(refreshSaved);
        Claims newClaims = jwtService.parseClaims(newRefreshToken);

        String newJti = newClaims.getId();
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .id(newJti)
                        .user(user)
                        .refreshToken(newRefreshToken)
                        .build()
        );
        return  new AuthenticationResponse(
                newAccessToken,
                newRefreshToken,
                jwtService.getAccessTokenExpirationSeconds(),
                "role"
        );


    }


    public void logout(LogoutRequest logoutRequest){
        String refreshToken = logoutRequest.refreshToken();
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException(Constants.ErrorKey.REFRESH_TOKEN_INVALID, refreshToken));
        refreshTokenRepository.delete(token);

    }
}
