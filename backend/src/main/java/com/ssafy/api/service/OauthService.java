package com.ssafy.api.service;


import com.ssafy.api.response.LoginResponse;
import com.ssafy.api.response.OauthTokenResponse;
import com.ssafy.db.entity.User;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface OauthService {

    public LoginResponse login(String providerName, String code);

    public OauthTokenResponse getToken(String code, ClientRegistration provider);

    public MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider);

    public User getUserProfile(String providerName, OauthTokenResponse tokenResponse, ClientRegistration provider);

    public Map<String, Object> getUserAttributes(ClientRegistration provider, OauthTokenResponse tokenResponse);

}
