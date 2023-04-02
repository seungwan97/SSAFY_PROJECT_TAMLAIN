package com.ssafy.config;

import com.ssafy.db.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .antMatchers("/oauth/callback/**").permitAll();
//        http.csrf().disable();
    }
}


//@RequiredArgsConstructor
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    @Bean
//    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
//
////        http.authorizeRequests()
////                .mvcMatchers(HttpMethod.OPTIONS,"/**").permitAll()
////                .antMatchers("/oauth/callback/**").permitAll();
//        http.csrf().disable()
//                .cors().configurationSource(corsConfigurationSource())
//                .and()
//                .exceptionHandling()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .formLogin().disable()
//                .httpBasic().disable()
//                .authorizeRequests()
//                .anyRequest().permitAll();
//
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.addAllowedOriginPattern("*");
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.addExposedHeader(HttpHeaders.AUTHORIZATION);
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//}