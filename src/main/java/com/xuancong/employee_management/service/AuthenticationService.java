package com.xuancong.employee_management.service;

import com.xuancong.employee_management.constants.Constants;
import com.xuancong.employee_management.dto.auth.AuthRequest;
import com.xuancong.employee_management.dto.auth.AuthenticationResponse;
import com.xuancong.employee_management.exception.BadCredentialsException;
import com.xuancong.employee_management.exception.NotFoundException;
import com.xuancong.employee_management.model.User;
import com.xuancong.employee_management.repository.UserRepository;
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

    public AuthenticationResponse authenticate(AuthRequest authRequest){
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(()-> new BadCredentialsException(Constants.ErrorKey.USER_NOT_FOUND, authRequest.getUsername()));

        boolean authenticated = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        if(!authenticated){
            throw new BadCredentialsException(Constants.ErrorKey.PASSWORD_NOT_VALID, authRequest.getPassword());
        }
        String token  = jwtService.generateToken(user);


    }

}
