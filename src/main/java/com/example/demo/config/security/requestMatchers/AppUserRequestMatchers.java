package com.example.demo.config.security.requestMatchers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpMethod.*;

// not in use
public class AppUserRequestMatchers implements RequestMatcher {
    private final Map<String, List<HttpMethod>> urlMap;

    private final List<AntPathRequestMatcher> requestMatchers;

    public AppUserRequestMatchers() {
        this.urlMap = new HashMap<>();
        this.requestMatchers = new ArrayList<>();
        initializeUrlMap();
    }

    private void initializeUrlMap() {
        // hard to read, imo
        urlMap.put("/abonements", Arrays.asList(GET));

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
