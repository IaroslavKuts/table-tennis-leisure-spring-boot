package com.example.demo.config.security.requestMatchers;

import com.example.demo.rest.controller.AppUserController;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;

/**
 * The class provides utility methods for creating ant path request matchers specific to ROLE_ADMIN users
 */
public class AdminRequestMatchers implements RequestMatcher {
    private static Logger LOG = LoggerFactory.getLogger(AdminRequestMatchers.class);
    private final Map<String, List<HttpMethod>> urlMap;

    private final List<AntPathRequestMatcher> requestMatchers;

    public AdminRequestMatchers() {
        this.urlMap = new HashMap<>();
        this.requestMatchers = new ArrayList<>();
        initializeUrlMap();
    }

    private void initializeUrlMap() {
        // hard to read, imo
        urlMap.put("/abonements/**", Arrays.asList(GET,POST, PATCH, PUT, DELETE));
        urlMap.put("/admin/**", Arrays.asList(GET, POST, PATCH, PUT, DELETE));
        urlMap.put("/users", Arrays.asList(GET));
        urlMap.put("/orders/**", Arrays.asList(GET, POST, PATCH, PUT, DELETE));
        urlMap.put("/work_schedule/**", Arrays.asList(GET, POST, PATCH, PUT, DELETE));
        urlMap.put("/altered_work_schedule/**", Arrays.asList(GET, POST, PATCH, PUT, DELETE));
        requestMatchers.addAll(
                urlMap.entrySet().stream()
                        .flatMap(entry -> entry.getValue().stream()
                                .map(httpMethod -> new AntPathRequestMatcher(entry.getKey(), httpMethod.name()))
                        )
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return requestMatchers.stream().anyMatch(antPathRequestMatcher -> antPathRequestMatcher.matches(request));


    }
}
