package com.example.demo.config.security;

import com.example.demo.domain.entity.AppUser;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.JwtService;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


public class JwtBasedCookie extends Cookie {
    private static final Logger LOG = LoggerFactory.getLogger(CookieSecurityContextRepository2.class);
    public static final String NAME = "jwtCookie";
    private static final String PATH = "/";
    private String jwt;
    private final Payload2 payload2;

    // For cookie creation
    public JwtBasedCookie(AppUser appUser, JwtService jwtService) {
        super(NAME, "");
        //        TODO add AppUserRepo
        this.payload2 = new Payload2(
                appUser
        );
        this.jwt = jwtService.generateRefreshToken(this.payload2.appUser());

        this.setPath(PATH);
        this.setMaxAge((int) Duration.of(1, ChronoUnit.HOURS).toSeconds());
        this.setHttpOnly(true);
    }
    //For reading data from cookie
    public JwtBasedCookie(Cookie cookie, JwtService jwtService, AppUserRepository appUserRepository) {
        super(NAME, "");
        if (!NAME.equals(cookie.getName()))
            throw new IllegalArgumentException(String.format("Cookie '{}' was not found ",NAME));

        String userName = jwtService.extractUsername(cookie.getValue());
//        TODO Process a case when user data is present in jwtCooke in browser
//         - but same user does not exist in data base
        this.payload2 = new Payload2(
                appUserRepository.findByEmail(userName).get()
        );



        this.setPath(cookie.getPath());
        this.setMaxAge(cookie.getMaxAge());
        this.setHttpOnly(cookie.isHttpOnly());
    }

    //value of cookie in browser
    @Override
    public String getValue() {
        return this.jwt;
    }

    public AppUser getAppUser() {
        return this.payload2.appUser();
    }


    private record Payload2(AppUser appUser){}

}
