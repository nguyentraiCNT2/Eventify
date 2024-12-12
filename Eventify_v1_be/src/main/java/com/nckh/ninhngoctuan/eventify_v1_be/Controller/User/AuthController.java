package com.nckh.ninhngoctuan.eventify_v1_be.Controller.User;

import com.nckh.ninhngoctuan.eventify_v1_be.Configuration.Application.RequestContext;
import com.nckh.ninhngoctuan.eventify_v1_be.Configuration.Security.CustomUserDetailsService;
import com.nckh.ninhngoctuan.eventify_v1_be.Configuration.Security.JwtTokenUtil;
import com.nckh.ninhngoctuan.eventify_v1_be.Model.Request.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenUtil jwtTokenUtil;

    public AuthController(CustomUserDetailsService customUserDetailsService, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());
            boolean isMash = passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword());
            if (!isMash)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Mật khẩu không chính xác", "status", HttpStatus.INTERNAL_SERVER_ERROR.value()));
            String accessToken = jwtTokenUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("token", accessToken);
            RequestContext context = RequestContext.get();
            if (context != null) {
                response.put("requestId", context.getRequestId());
                response.put("timestamp", context.getTimestamp());
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage(), "status", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
}
