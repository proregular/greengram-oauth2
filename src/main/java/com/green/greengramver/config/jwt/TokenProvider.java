package com.green.greengramver.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengramver.common.exception.CustomException;
import com.green.greengramver.common.exception.UserErrorCode;
import com.green.greengramver.config.security.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
public class TokenProvider {
    private final ObjectMapper objectMapper; //Jackson 라이브러리
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public TokenProvider(ObjectMapper objectMapper, JwtProperties jwtProperties) {
        this.objectMapper = objectMapper;
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getSecretKey()));
    }

    // JWT 생성
    // sgMemo: 매개변수 -> jwtUser: clam에 넣을 유저 정보(아이디 권한 닉네임 등등... 유저에 대해 넣고 싶은 데이터)
    // , expiredAt: 만료 일시
    public String generateToken(JwtUser jwtUser, Duration expiredAt) {
        Date now = new Date();
        return makeToken(jwtUser, new Date(now.getTime() + expiredAt.toMillis()));
    }

    private String makeToken(JwtUser jwtUser, Date expiry) {
        // JWT 암호화
        // sgMemo: 밑에 빌더 패턴으로 생성되는 타입은 String(문자열)이다.
        return Jwts.builder()
                .header().type("JWT")
                .and()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date())
                .expiration(expiry)
                .claim("signedUser", makeClaimByUserToString(jwtUser))
                .signWith(secretKey)
                .compact();
    }

    private String makeClaimByUserToString(JwtUser jwtUser) {
        //객체 자체를 JWT에 담고 싶어서 객체를 직렬화
        //jwtUser에 담고있는 데이터를 JSON형태의 문자열로 변환
        try {
            return objectMapper.writeValueAsString(jwtUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validToken(String token) {
        try {
            //JWT 복호화
            //sgMemo: 만약 유효기간이 지났다면 메소드를 호출 했을때 예외가 발생하니
            // 그 상황으로 유효한 토큰인지 검사 할 수 있다.
            getClaims(token);
        } catch (Exception e) {
            throw new CustomException(UserErrorCode.EXPIRED_TOKEN);
        }

        return true;
    }

    // Spring security에서 인증 처리를 해주어야 한다. 그때 Authentication 객체가 필요
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetailsFromToken(token);
        return userDetails == null
                ? null
                : new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public JwtUser getUser(String token) {
        Claims claims = getClaims(token);
        String json = (String)claims.get("signedUser");
        JwtUser jwtUser = null;

        try {
            jwtUser = objectMapper.readValue(json, JwtUser.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return jwtUser;
    }

    public UserDetails getUserDetailsFromToken(String token) {
        JwtUser jwtUser = getUser(token);
        MyUserDetails userDetails = new MyUserDetails();
        userDetails.setJwtUser(jwtUser);

        return userDetails;
    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

}
