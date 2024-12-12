package com.nckh.ninhngoctuan.eventify_v1_be.Configuration.Security;

import com.nckh.ninhngoctuan.eventify_v1_be.Entity.UserEntity;
import com.nckh.ninhngoctuan.eventify_v1_be.Repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserNameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

        if (!user.isActive())
            throw new RuntimeException("Tài khoản này đã bị khóa");

        if (!user.isEmailVerified())
            throw new RuntimeException("Chưa xác thực tài khoản");

        return User.withUsername(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
