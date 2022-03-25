package com.matstar.ex7.config;

import com.matstar.ex7.filter.ApiCheckFilter;
import com.matstar.ex7.filter.ApiLoginFilter;
import com.matstar.ex7.security.handler.ApiLoginFailHandler;
import com.matstar.ex7.security.handler.ClubLoginSuccessHandler;
import com.matstar.ex7.service.ClubUserDetailService;
import com.matstar.ex7.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private ClubUserDetailService userDetailsService;


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//       // 사용자 계정
//        auth.inMemoryAuthentication().withUser("user1")
//                .password("$2a$10$d07AyG4cahQKaMvKAkIhlu.ZsAHi0p4AkZiX5QDPiakaOBIddEGc2")
//                .roles("USER");
//       // 패스워드 1111 인코딩 결과
//
//
//        super.configure(auth);
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //http.authorizeRequests()
         //       .antMatchers("/sample/all").permitAll()
            //            .antMatchers("/sample/member").hasRole("USER");

        http.formLogin();
        http.csrf().disable();
        http.logout();
        http.oauth2Login().successHandler(successHandler());
        http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService);
        //super.configure(http);
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(),UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    public ClubLoginSuccessHandler successHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() {
        return new ApiCheckFilter("/notes/**/*",jwtUtil());
    }


    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {

        ApiLoginFilter apiLoginFilter =
                new ApiLoginFilter("/api/login",jwtUtil());

        apiLoginFilter.setAuthenticationManager(authenticationManager());

        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }





}
