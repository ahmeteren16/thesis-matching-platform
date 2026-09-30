package com.lorem_ipsum.thesis.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final AppUserService appUserService;

    public SecurityConfig(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity chainbuilder) throws Exception {
        chainbuilder.authorizeHttpRequests(
                        configurer -> configurer.requestMatchers("/home","/")
                                .permitAll()
                                .anyRequest().authenticated())
                .oauth2Login(config ->
                        config.userInfoEndpoint(
                                info -> info.userService(appUserService)
                        ));


        return chainbuilder.build();
    }





}
