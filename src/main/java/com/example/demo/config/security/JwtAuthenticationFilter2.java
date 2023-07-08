package com.example.demo.config.security;

import com.example.demo.config.web.exceptions.JwtParsedDataDissimilarityException;
import com.example.demo.domain.entity.AppUser;
import com.example.demo.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * Its purpose is to catch {@link JwtException} and send back
 * to browser more readable response.
 * Response's template is placed in {@link com.example.demo.config.web.handlers.GlobalExceptionHandler#handleException(JwtException, WebRequest)}
 */
@Component
@RequiredArgsConstructor
//TODO Find out purpose
public class JwtAuthenticationFilter2 extends OncePerRequestFilter {
    private static Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter2.class);
    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            LOG.info(" entered JwtAuthenticationFilter2");
            Optional<Cookie> cookie = Optional.ofNullable(WebUtils.getCookie(request,JwtBasedCookie.NAME));
            cookie.ifPresent(c -> {
                        LOG.info(" entered JwtAuthenticationFilter2 ifPresent");
                        String username = jwtService.extractUsername(c.getValue());// here jwtException might be thrown
                        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if(!currentUser.getEmail().equals(username)
                                &&
                        currentUser.getRefreshToken() != c.getValue())
                            throw new JwtParsedDataDissimilarityException("Data stored in jwtCookie is not similar to" +
                                    "data of authenticated user");
                        response.setHeader("Authorization",String.format("Bearer %s",jwtService.generateAccessToken()));
            }
            );

            filterChain.doFilter(request, response);
        } catch (JwtException ex){
            //https://stackoverflow.com/questions/17715921/exception-handling-for-filter-in-spring
            //This seems to "pass" exception to @RestControllerAdvice
            handlerExceptionResolver.resolveException(request,response,null,ex);
        }
    }
}