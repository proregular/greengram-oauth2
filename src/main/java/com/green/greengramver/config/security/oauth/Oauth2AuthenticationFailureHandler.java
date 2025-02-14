package com.green.greengramver.config.security.oauth;

import com.green.greengramver.common.CookieUtils;
import com.green.greengramver.common.GlobalOauth2;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final Oauth2AuthenticationRequestBasedOnCookieRepository repository;
    private final CookieUtils cookieUtils;
    private final GlobalOauth2 globalOauth2;

    @Override
    public void onAuthenticationFailure(final HttpServletRequest req, HttpServletResponse res, AuthenticationException exception)
    throws IOException {
        exception.printStackTrace();

        // FE - Redirect-Url 획득 from Cookie
        String redirectUrl = cookieUtils.getValue(req, globalOauth2.getRedirectUriParamCookieName(), String.class);

        // URL에 에러 쿼리스트링 추가
        String targetUrl = redirectUrl == null ? "/" : UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build()
                .toUriString();

        String errRedirectUrl = UriComponentsBuilder.fromUriString("/err_message")
                .queryParam("message", "Social Login 실패하였습니다.").encode()
                .toUriString();

        // targetUrl = "http://localhost:8080/fe/redirect?error=에러메세지"
        getRedirectStrategy().sendRedirect(req, res, errRedirectUrl);
    }
}
