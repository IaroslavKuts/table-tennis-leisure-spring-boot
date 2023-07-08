package com.example.demo.config.web.handlers;

import com.example.demo.config.security.AuthConfig;
import com.example.demo.domain.entity.AppUser;
import com.example.demo.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private static Logger LOG = LoggerFactory.getLogger(AppAuthenticationSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LOG.info("entered " + this.getClass().getCanonicalName());
        AppUser authenticatedUser = (AppUser)authentication.getPrincipal();
        String authenticatedUserAuthority = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());
        response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of(
                "accessToken",jwtService.generateAccessToken(),
                "authorities",authenticatedUserAuthority,
                "user_id",authenticatedUser.getUser_id()
                )
        ));
    }


}
