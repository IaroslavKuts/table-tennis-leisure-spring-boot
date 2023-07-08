package com.example.demo.config.security;

//import com.example.demo.service.AppUserService;
import com.example.demo.config.security.requestMatchers.AdminRequestMatchers;
import com.example.demo.config.security.requestMatchers.AppUserRequestMatchers;
import com.example.demo.config.web.handlers.AppAuthenticationSuccessHandler;
import com.example.demo.config.web.handlers.AppLogoutHandler;
import com.example.demo.config.web.interceptors.AppUserInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

    private final AppLogoutHandler appLogoutHandler;
    private final JwtAuthenticationFilter2 jwtAuthenticationFilter2;
    private final CookieSecurityContextRepository2 cookieSecurityContextRepository2;
    private  final AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;

    @Value("${urls.auth.login}")
    private String LOGIN_PROCESSING_URL;
    @Value("${urls.auth.main}")
    private String MAIN_PAGE;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        RequestMatcher adminMatchers = new AdminRequestMatchers();
        http
                .cors().configurationSource(this.corsConfigurationSource())
                .and()
                .csrf(csrf->csrf.disable())
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.NEVER))
                .securityContext(securityContext->securityContext.securityContextRepository(cookieSecurityContextRepository2))
                .logout(logout->logout
                        .addLogoutHandler(appLogoutHandler)
                        .deleteCookies(JwtBasedCookie.NAME)
                )
                .addFilterBefore(jwtAuthenticationFilter2, LogoutFilter.class)
                .requestCache().disable()
                .formLogin(form->form.usernameParameter("email")
                                .passwordParameter("password")
                                .successHandler(appAuthenticationSuccessHandler)
                                .loginPage(MAIN_PAGE)
                                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                )
                .authorizeHttpRequests(request->request
                        .requestMatchers(POST,"/users").permitAll()
                        .requestMatchers(GET,"/abonements").hasAnyRole("ADMIN","USER")
                                .requestMatchers(GET,"/work_schedule").hasAnyRole("ADMIN","USER")
                                .requestMatchers(GET,"/altered_work_schedule").hasAnyRole("ADMIN","USER")
                        .requestMatchers(adminMatchers).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
        ;
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:3000/","http://localhost:8080","https://www.example.com")); // Replace with your allowed origin(s)
        configuration.setAllowedMethods(Arrays.asList(CorsConfiguration.ALL));
        configuration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }




}