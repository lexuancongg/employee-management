package com.xuancong.employee_management.config.ws;

import com.xuancong.employee_management.service.JwtService;
import com.xuancong.employee_management.utils.AuthenticationUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

//@RequiredArgsConstructor
//@Component
//public class JwtChannelInterceptor implements ChannelInterceptor {
//
//    private final JwtService jwtService;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//
//        StompHeaderAccessor accessor =
//                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//        if (accessor == null) return message;
//
//        String auth = accessor.getFirstNativeHeader("Authorization");
//
//        if (auth != null && auth.startsWith("Bearer ")) {
//
//            String token = auth.substring(7);
//
//            Claims claims = jwtService.parseClaims(token);
//            String userId = claims.getSubject();
//
//            accessor.setUser(() -> userId);
//        }
//
//        return message;
//    }
//}

@RequiredArgsConstructor
@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        // 🔥 chỉ xử lý khi CONNECT (quan trọng)
        if (accessor.getCommand() == null ||
                !accessor.getCommand().name().equals("CONNECT")) {
            return message;
        }

        // 👉 lấy cookie từ native headers
        String cookieHeader = accessor.getFirstNativeHeader("cookie");

        if (cookieHeader == null) {
            return message;
        }

        String token = extractCookie(cookieHeader, "accessToken");

        if (token == null) {
            return message;
        }

        // 👉 validate JWT
        String userId = jwtService.parseClaims(token).getSubject();

        // 👉 gắn user vào WebSocket session
        accessor.setUser(() -> userId);

        return message;
    }

    private String extractCookie(String cookieHeader, String name) {

        String[] cookies = cookieHeader.split(";");

        for (String cookie : cookies) {
            String[] parts = cookie.trim().split("=");

            if (parts.length == 2 && parts[0].equals(name)) {
                return parts[1];
            }
        }
        return null;
    }
}