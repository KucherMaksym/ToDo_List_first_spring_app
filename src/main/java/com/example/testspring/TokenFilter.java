package com.example.testspring;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtCore jwtCore;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null) {
            String[] authElements = header.split(" ");

            if (authElements.length == 2
                    && "Bearer".equals(authElements[0])) {
                try {
                    SecurityContextHolder.getContext().setAuthentication(
                            jwtCore.validateToken(authElements[1]));
                } catch (RuntimeException e) {
                    SecurityContextHolder.clearContext();
                    throw e;
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String jwt = null;
//        String username= null;
//        UserDetails userDetails = null;
//        UsernamePasswordAuthenticationToken auth = null;
//        try {
//            String headerAuth = request.getHeader("Authorization");
//            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
//                System.out.println(jwt);
//                jwt = headerAuth.substring(7);
//                System.out.println("2  " + jwt);
//            }
//            if (jwt != null) {
//                try {
//                    System.out.println("3  " + jwt);
//                    username = jwtCore.getNameFromJwt(jwt);
//
//                    System.out.println("has");
//                } catch (ExpiredJwtException e) {
//
//                }
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    userDetails = userDetailsService.loadUserByUsername(username);
//                    auth = new UsernamePasswordAuthenticationToken(userDetails, null);
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//                    System.out.println("эьцузоцшс" + auth);
//                }
//
//            }
//        } catch (Exception e) {
//
//        }
//        filterChain.doFilter(request, response);
//    }
}
