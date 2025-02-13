package com.green.greengramver.config.security.oauth;

import com.green.greengramver.common.GlobalOauth2;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AutenticationCheckRedirectUriFilter extends OncePerRequestFilter {

    private final GlobalOauth2 globalOauth2;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /*
            호스트 주소값을 제외한 요청한 URI
            예) http://localhost:8080/oauth2/authorization
            호스트 주소값: http://localhost:8080
            제외한 요청한 URI: /oauth2/authorization
        */
        String requrstUri = request.getRequestURI(); // 요청한 URI
        log.info("request uri: {}", requrstUri);

        if(requrstUri.startsWith(globalOauth2.getBaseUri())) {
            String redirectUri = request.getParameter("redirect_uri");
            if(redirectUri != null && !hasAuthorizedRedirectUri(redirectUri)) { // 약속한 redirect_uri값이 아니었다면
                String errRedirectUrl = UriComponentsBuilder.fromUriString("/err_message")
                                                            .queryParam("message", "유효한 Redirect URL이 아닙니다.").encode()
                                                            .toUriString();
                // reeRedirectUrl = "/err_message?message=유효한 Redirect URL이 아닙니다."
                response.sendRedirect(errRedirectUrl);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // 약속한 redirect_uri가 맞는지 체크 없으면 false, 있으면 true 리턴
    private boolean hasAuthorizedRedirectUri(String redirectUri) {
        for(String uri : globalOauth2.getAuthorizedRedirectUris()) {
            if(uri.startsWith(redirectUri)) {
                return true;
            }
        }
        return false;
    }
}
