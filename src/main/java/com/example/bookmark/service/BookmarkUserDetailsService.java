package com.example.bookmark.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public BookmarkUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user found for '" + username + "'"));
    }
}
