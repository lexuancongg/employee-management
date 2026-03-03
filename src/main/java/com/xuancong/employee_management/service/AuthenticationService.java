package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.auth.AuthRequest;
import com.xuancong.employee_management.dto.auth.AuthenticationResponse;
import com.xuancong.employee_management.dto.auth.RefreshRequest;
import com.xuancong.employee_management.exception.BadCredentialsException;
import com.xuancong.employee_management.exception.UnauthorizedException;
import com.xuancong.employee_management.model.RefreshToken;
import com.xuancong.employee_management.model.User;
import com.xuancong.employee_management.repository.RefreshTokenRepository;
import com.xuancong.employee_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private  final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationResponse login(AuthRequest authRequest){
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(()-> new BadCredentialsException(Constants.ErrorKey.USER_NOT_FOUND, authRequest.getUsername()));

        boolean authenticated = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        if(!authenticated){
            throw new BadCredentialsException(Constants.ErrorKey.PASSWORD_NOT_VALID, authRequest.getPassword());
        }
        String accessToken  = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        long expiresIn = jwtService.getAccessTokenExpirationSeconds();
        return new AuthenticationResponse(accessToken, refreshToken, expiresIn);

    }


    public AuthenticationResponse refresh(RefreshRequest refreshRequest){
        String refreshToken = refreshRequest.refreshToken();
        if(!jwtService.isValid(refreshToken)){
            throw new UnauthorizedException(Constants.ErrorKey.REFRESH_TOKEN_INVALID, refreshToken);
        }
        String type = jwtService.extractType(refreshToken);
        if (!"refresh".equals(type)) {
            throw new UnauthorizedException("Invalid token type");
        }
        String jti = jwtService.extractJti(refreshToken);
        RefreshToken refreshSaved = refreshTokenRepository.findById(jti)
                .orElseThrow(()-> new UnauthorizedException("Refresh token revoked"));
        User user = refreshSaved.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        refreshTokenRepository.delete(refreshSaved);
        String newJti = jwtService.extractJti(newRefreshToken);
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
                15*60
        );


    }
}
