package com.green.greengramver.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter  extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizeationHeader = request.getHeader(HEADER_AUTHORIZATION);
        log.info("authorizeationHeader: {}", authorizeationHeader);

        String token = getAccessToken(authorizeationHeader);
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizaionHeader) {
        if(authorizaionHeader != null && authorizaionHeader.startsWith(TOKEN_PREFIX)) {
            return authorizaionHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

}
