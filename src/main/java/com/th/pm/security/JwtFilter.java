package com.th.pm.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter{

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PmUserDetailsService pmUserDetailsService;
    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, 
        @SuppressWarnings("null") HttpServletResponse response, 
        @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer")){
            log.warn("Authorization header is missing or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }
        try{
            final String token = authHeader.substring(7);
            Claims claims = jwtService.extractAllClaims(token);
            String email = claims.get("email").toString();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(email !=null && authentication == null ){
                UserDetails userDetails = pmUserDetailsService.loadUserByEmail(email);
                if(jwtService.isTokenValid(token, userDetails)){
                    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,"", userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }catch(Exception e){
            log.error("Error validating jwt token", e);
            handlerExceptionResolver.resolveException(request, response, authHeader, e);
        }
    }
    
}
