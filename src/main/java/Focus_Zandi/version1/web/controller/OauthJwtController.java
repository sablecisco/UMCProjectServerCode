package Focus_Zandi.version1.web.controller;

import Focus_Zandi.version1.domain.Member;
import Focus_Zandi.version1.domain.MemberDetails;
import Focus_Zandi.version1.domain.dto.JwtReturner;
import Focus_Zandi.version1.web.config.jwt.CreateJwt;
import Focus_Zandi.version1.web.config.jwt.CreateTokens;
import Focus_Zandi.version1.web.config.jwt.JwtProperties;
import Focus_Zandi.version1.web.config.oauth.provider.GoogleUser;
import Focus_Zandi.version1.web.config.oauth.provider.OAuthUserInfo;
import Focus_Zandi.version1.web.repository.MemberRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.Ref;
import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OauthJwtController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/oauth/google")
    public JwtReturner jwtCreate(@RequestBody Map<String, Object> data, HttpServletResponse response) {
        OAuthUserInfo googleUser =
                new GoogleUser((Map<String, Object>)data.get("profileObj")); // 프론트가 보내준 json 받기

        Member memberEntity = memberRepository.findByProviderId(googleUser.getProviderId());

        String provider = googleUser.getProvider();
        String providerId = googleUser.getProviderId();
        String name = googleUser.getName();
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("CommonPassword");
        String email = googleUser.getEmail();
        MemberDetails memberDetails = new MemberDetails();

        if(memberEntity == null) {
            Member memberRequest = Member.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .email(email)
                    .providerId(providerId)
                    .provider(provider)
                    .memberDetails(memberDetails)
                    .build();
            memberEntity = memberRepository.save(memberRequest);
        } else {
            System.out.println("User already Exists");
        }

//        String AccessToken = JWT.create()
//                .withSubject(memberEntity.getUsername())
////                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))
//                .withExpiresAt(new Date(System.currentTimeMillis() + 30000))
//                .withClaim("id", memberEntity.getId())
//                .withClaim("username", memberEntity.getUsername())
//                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
//
//        String RefToken = JWT.create()
//                .withSubject(memberEntity.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis()+ 60000*100))
//                .withClaim("AccessToken", AccessToken)
//                .withClaim("username", memberEntity.getUsername())
//                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

//        String jwtToken = CreateJwt.createAccessToken(memberEntity);

        String accessToken = CreateJwt.createAccessToken(memberEntity);
        String refreshToken = CreateJwt.createRefreshToken(memberEntity, accessToken);

//        JwtReturner returner = CreateTokens.createAccessToken(memberEntity);

        return new JwtReturner(accessToken, refreshToken);
    }

//    @GetMapping("/oauth/new_token")
//    public JwtReturner new_token() {
//
//    }
}
