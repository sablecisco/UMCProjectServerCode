package Focus_Zandi.version1.web.config.jwt;

import Focus_Zandi.version1.domain.Member;
import Focus_Zandi.version1.domain.dto.JwtReturner;
import com.nimbusds.oauth2.sdk.token.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateTokens {

    public static JwtReturner createAccessToken(Member member) {

        Claims claims = Jwts.claims().setSubject(member.getUsername()); // JWT payload 에 저장되는 정보단위
        claims.put("memberId", member.getId());
        Date now = new Date();

        //Access Token
        String accessToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, JwtProperties.SECRET)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret 세팅
                .compact();

        //Refresh Token
        String refreshToken = Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + JwtProperties.EXPIRATION_TIME * 100)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, JwtProperties.SECRET)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();


        return new JwtReturner(accessToken, refreshToken);
    }
}