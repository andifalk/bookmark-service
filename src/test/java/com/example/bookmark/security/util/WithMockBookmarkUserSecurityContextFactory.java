package com.example.bookmark.security.util;

import com.example.bookmark.service.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WithMockBookmarkUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockBookmarkUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockBookmarkUser withMockBookmarkUser) {

        List<GrantedAuthority> grantedAuthorities =
                Arrays.stream(withMockBookmarkUser.roles())
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList());

        User libraryUser =
                new User(
                        withMockBookmarkUser.identifier(),
                        withMockBookmarkUser.firstName(),
                        withMockBookmarkUser.lastName(),
                        "n/a",
                        withMockBookmarkUser.email(),
                        List.of(withMockBookmarkUser.roles()));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(libraryUser, "n/a", grantedAuthorities);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(usernamePasswordAuthenticationToken);
        return context;
    }
}
