package com.example.demo.config.security;

import com.example.demo.domain.entity.AppUser;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.*;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

// I was inspired by https://github.com/innoq/cookie-based-session-springboot-app repo
@Component
@RequiredArgsConstructor
public class CookieSecurityContextRepository2 implements SecurityContextRepository {

    private static final Logger LOG = LoggerFactory.getLogger(CookieSecurityContextRepository2.class);
    private static final String EMPTY_CREDENTIALS = "";
    private static final String ANONYMOUS_USER = "anonymousUser";
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder)  {
//  TODO avoid reRead an user from db. It seems to load user data from db every time any URL is called
            LOG.info("Entered loadCOntext");
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            readUserInfoFromCookie(requestResponseHolder.getRequest()).ifPresent(appUser ->{
                LOG.info("Entered ifPresent");
                context.setAuthentication(new UsernamePasswordAuthenticationToken(appUser, EMPTY_CREDENTIALS, appUser.getAuthorities()));
            });

            return context;

    }
// According to documentation this method must be used instead of loadContext
//    @Override
//    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {;
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        readUserInfoFromCookie(request).ifPresent(userInfo ->{
//            LOG.info("Entered ifPresent");
//            LOG.info(userInfo.getUsername());
//            context.setAuthentication(new UsernamePasswordAuthenticationToken(userInfo, EMPTY_CREDENTIALS, userInfo.getAuthorities()));
//            SecurityContextHolder.setContext(context);
//            LOG.info(SecurityContextHolder.getContext().getAuthentication().getName());
//        });
//
//
//        return SecurityContextRepository.super.loadDeferredContext(request);
//    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Entered saveContext");
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            LOG.debug("No securityContext.authentication, skip saveContext");
            return;
        }

        if (ANONYMOUS_USER.equals(authentication.getPrincipal())) {
            LOG.debug("Anonymous User SecurityContext, skip saveContext");
            return;
        }

        if (!(authentication.getPrincipal() instanceof AppUser)) {
            LOG.warn("securityContext.authentication.principal of unexpected type {}, skip saveContext", authentication.getPrincipal().getClass().getCanonicalName());
            return;
        }

        AppUser appUser = (AppUser) authentication.getPrincipal();
        JwtBasedCookie jwtCookie = new JwtBasedCookie(appUser,jwtService);
        appUser.setRefreshToken(jwtCookie.getValue());
        LOG.info(String.format("jwtCookie value %s",jwtCookie.getValue()));
        appUserRepository.save(appUser);
        jwtCookie.setSecure(request.isSecure());
        response.addCookie(jwtCookie);
        //TODO accessToken and refreshToken in header
        response.setHeader("Bearer ", jwtService.generateAccessToken());
//        Map<String,String> accessToken =  Collections.singletonMap("accessToken",jwtService.generateAccessToken(appUser));
//        response.getWriter().write(new ObjectMapper().writeValueAsString(accessToken));

        LOG.debug("SecurityContext for principal '{}' saved in Cookie", appUser.getUsername());
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        LOG.info("Entered containsContext");
        return readUserInfoFromCookie(request).isPresent();
    }

    private Optional<AppUser> readUserInfoFromCookie(HttpServletRequest request) {

        return readCookieFromRequest(request)
                .map(this::createAppUser);
    }

    private Optional<Cookie> readCookieFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) {
            LOG.debug("No cookies in request");
            LOG.info("No cookies in request");
            return Optional.empty();
        }

        Optional<Cookie> maybeCookie = Optional.ofNullable(WebUtils.getCookie(request,JwtBasedCookie.NAME));

        if (maybeCookie.isEmpty()) {
            LOG.debug("No {} cookie in request", JwtBasedCookie.NAME);
            LOG.info("No {} cookie in request", JwtBasedCookie.NAME);
        }
        return maybeCookie;
    }

    private AppUser createAppUser(Cookie cookie) {
        return new JwtBasedCookie(cookie, jwtService,appUserRepository).getAppUser();
    }


}