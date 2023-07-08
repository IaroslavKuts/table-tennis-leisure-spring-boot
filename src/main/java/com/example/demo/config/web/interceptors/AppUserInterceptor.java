package com.example.demo.config.web.interceptors;

import com.example.demo.domain.entity.AppUser;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


/**
 * The interceptor ensures that only the authorized user can modify data associated with their own identity.
 */
@Component
@RequiredArgsConstructor
public class AppUserInterceptor implements HandlerInterceptor {



    public static final String URL = "/users/**";

    private final JwtService jwtService;

    private final AppUserRepository appUserRepository;

    private static Logger LOG = LoggerFactory.getLogger(AppUserInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("PREHANDLE");
//        request.isUserInRole("USER")) enum that contains role must have next "form" ROLE_USER, ROLE_ADMIN, ROLE_ETC
        if(request.getRemoteUser() != null && request.isUserInRole("USER")) {;
            AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            LOG.info(currentUser.getRole().getAuthority());
            Map<String, String> uriVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (currentUser.getUser_id() != Long.valueOf(uriVariables.get("id"))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to browse this page");


            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LOG.info("POSTHANDLE");
//        System.out.println("POSTHANDLE");
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LOG.info("AFTERCOMPLETION");
//        System.out.println("AFTERCOMPLETION");
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }


}
