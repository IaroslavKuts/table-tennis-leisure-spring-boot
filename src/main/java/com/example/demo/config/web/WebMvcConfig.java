package com.example.demo.config.web;

import com.example.demo.config.web.interceptors.AppUserInterceptor;
import com.example.demo.domain.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer, RepositoryRestConfigurer {
    @Autowired
    AppUserInterceptor appUserInterceptor;

//    Doesn't work properly with Spring REST Data
//    https://github.com/spring-projects/spring-data-rest/issues/1522

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(userServiceInterceptor).addPathPatterns("/users/**","/abonements/","/orders");
//    }

    //Works with Spring REST Data
    //https://stackoverflow.com/questions/46953039/spring-interceptor-not-working-in-spring-data-rest-urls
    @Bean
    public MappedInterceptor userServiceMappedInterceptor() {
        return new MappedInterceptor(new String[]{appUserInterceptor.URL}, appUserInterceptor);
    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(AppUser.class);
        config.exposeIdsFor(Abonement.class);
        config.exposeIdsFor(WorkSchedule.class);
        config.exposeIdsFor(AlteredWorkSchedule.class);
        config.exposeIdsFor(AppOrder.class);
    }


}
