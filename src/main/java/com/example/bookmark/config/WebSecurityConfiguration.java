package com.example.bookmark.config;

import com.example.bookmark.security.MD5PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable().and()
                .authorizeRequests()
                .mvcMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated().and()
                .httpBasic().and().formLogin();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers("/", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }

}
