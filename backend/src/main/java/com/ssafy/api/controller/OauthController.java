package com.ssafy.api.controller;

import com.ssafy.api.request.RefreshTokenRequest;
import com.ssafy.api.response.AccessTokenResponse;
import com.ssafy.api.service.AuthService;
import com.ssafy.api.service.OauthService;
import com.ssafy.api.response.LoginResponse;
import com.ssafy.util.AuthorizationExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@Slf4j
@RestController
public class OauthController {

    private final OauthService oauthService;
    private final AuthService authService;

    @GetMapping("/oauth/callback/{provider}")
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code){
        System.out.println("여기까지 들어오나 ? ");
        LoginResponse loginResponse = oauthService.login(provider, code);
        System.out.println(loginResponse);
        return ResponseEntity.ok().body(loginResponse);
    }

    /**
     * @title access token 갱신
     */
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AccessTokenResponse> refreshAccessToken(HttpServletRequest request,
                                                                  @ModelAttribute RefreshTokenRequest refreshToken) throws AuthenticationException {
        String accessToken = AuthorizationExtractor.extract(request);
        return ResponseEntity.ok().body(authService.refreshAccessToken(accessToken, refreshToken));
    }

    /**
     * @title 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String accessToken = AuthorizationExtractor.extract(request);
        authService.logout(accessToken);
        return ResponseEntity.noContent().build();
    }
}