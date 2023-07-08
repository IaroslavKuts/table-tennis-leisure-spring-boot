package com.example.demo.config.web.handlers;

import com.example.demo.domain.entity.AppUser;
import com.example.demo.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * Custom implementation of {@link LogoutHandler}.
 * Its purpose is to set current authenticated user refreshToken to null
 */
@Component
public class AppLogoutHandler implements LogoutHandler {

    @Autowired
    private AppUserRepository appUserRepository;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<AppUser> currentUserOptional = Optional.ofNullable((AppUser) authentication.getPrincipal());
        currentUserOptional.ifPresent(currentUser->{
            currentUser.setRefreshToken(null);
            appUserRepository.save(currentUser);
        });

    }
}
