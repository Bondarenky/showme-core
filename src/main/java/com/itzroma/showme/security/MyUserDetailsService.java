package com.itzroma.showme.security;

import com.itzroma.showme.domain.entity.User;
import com.itzroma.showme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return MyUserDetails.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .authorities(Collections.emptyList())
                .build();
    }
}
