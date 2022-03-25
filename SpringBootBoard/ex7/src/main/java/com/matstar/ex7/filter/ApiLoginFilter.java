package com.matstar.ex7.filter;

import com.matstar.ex7.dto.ClubAuthMemberDTO;
import com.matstar.ex7.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("attempAuthentication");

        String email = request.getParameter("email");
        String pw = "1111";

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email,pw);

        return getAuthenticationManager().authenticate(authToken);

//        if(email == null) {
//            throw new BadCredentialsException("email cannot be null");
//        }
//        return null;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        log.info("SuccessfulAuthentication : " + authResult);
        log.info("result : " + authResult.getPrincipal());

        String email = ((ClubAuthMemberDTO)authResult.getPrincipal()).getUsername();

        String token = null;

        try {

            token = jwtUtil.generateToken(email);

            response.setContentType("text/palin");
            response.getOutputStream().write(token.getBytes());

            log.info("token : " + token);

        }catch(Exception e) {
            e.printStackTrace();
        }

       // super.successfulAuthentication(request, response, chain, authResult);
    }
}
