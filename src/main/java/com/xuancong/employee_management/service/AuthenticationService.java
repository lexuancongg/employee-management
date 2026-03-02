package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.auth.AuthRequest;
import com.xuancong.employee_management.dto.auth.AuthenticationResponse;
import com.xuancong.employee_management.dto.auth.RefreshRequest;
import com.xuancong.employee_management.exception.BadCredentialsException;
import com.xuancong.employee_management.model.User;
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

    public AuthenticationResponse login(AuthRequest authRequest){
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(()-> new BadCredentialsException(Constants.ErrorKey.USER_NOT_FOUND, authRequest.getUsername()));

        boolean authenticated = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        if(!authenticated){
            throw new BadCredentialsException(Constants.ErrorKey.PASSWORD_NOT_VALID, authRequest.getPassword());
        }
        String jti = UUID.randomUUID().toString();
        String accessToken  = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user,jti);
        long expiresIn = jwtService.getAccessTokenExpirationSeconds();
        return new AuthenticationResponse(accessToken, refreshToken, expiresIn);

    }


    public AuthenticationResponse refresh(RefreshRequest refreshRequest){
        String refreshToken = refreshRequest.refreshToken();

    }
}
