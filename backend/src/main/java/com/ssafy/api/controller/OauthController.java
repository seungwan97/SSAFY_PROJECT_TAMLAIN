package com.ssafy.api.controller;

import com.ssafy.api.service.OauthService;
import com.ssafy.api.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@Slf4j
@RestController
public class OauthController {

    private final OauthService oauthService;
    @GetMapping("/oauth/callback/{provider}")
    public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestParam String code){
        System.out.println("여기까지 들어오나 ? ");
        LoginResponse loginResponse = oauthService.login(provider, code);
        System.out.println(loginResponse);
        return ResponseEntity.ok().body(loginResponse);
    }

}