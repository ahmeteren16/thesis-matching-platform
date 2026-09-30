package com.lorem_ipsum.thesis.config;

import com.lorem_ipsum.thesis.domain.repository.BetreuerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AppUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    private final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();
    private final BetreuerRepository betreuerRepository;

    public AppUserService(BetreuerRepository betreuerRepository) {
        this.betreuerRepository = betreuerRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User originalUser = defaultService.loadUser(userRequest);


        String login = originalUser.getAttribute("login");

        Set<GrantedAuthority> authorities = new HashSet<>(originalUser.getAuthorities());


        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));


        if (login != null && betreuerRepository.existsByGithubLogin(login)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_BETREUER"));
        }


        if ("ahmeteren116".equalsIgnoreCase(login)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new DefaultOAuth2User(authorities, originalUser.getAttributes(), "login");
    }
}

