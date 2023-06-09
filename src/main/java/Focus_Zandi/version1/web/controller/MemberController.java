package Focus_Zandi.version1.web.controller;

import Focus_Zandi.version1.domain.Member;
import Focus_Zandi.version1.domain.MemberDetails;
import Focus_Zandi.version1.domain.dto.DetailsDto;
import Focus_Zandi.version1.domain.dto.FolloweeNameDto;
import Focus_Zandi.version1.domain.dto.FolloweeReturner;
import Focus_Zandi.version1.domain.dto.MemberReturnerDto;
import Focus_Zandi.version1.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    //DTO 수정해서 프론트 요구사항 맞추면 됨
    //유저 정보 조회
    @GetMapping("/showMember")
    public MemberReturnerDto showMember(HttpServletRequest request, Authentication authentication) {
        String username = getUsername(authentication);
        MemberReturnerDto details = memberService.findMemberByUserNameWithDetails(username);
        return details;
    }

    @PostMapping("/memberQuit")
    public void memberQuit(Authentication authentication, HttpServletResponse response) throws IOException {
        String username = getUsername(authentication);
        memberService.deleteAll(username);
        Member memberByUserName = memberService.findMemberByUserName(username);
        if (memberByUserName == null) {
            response.setStatus(200);
        } else {
            response.sendError(400, "Delete FAILED!");
        }
    }

    @GetMapping("/members/{name}")
    public MemberReturnerDto showMemberByName(@PathVariable String name) {
        return memberService.findMemberByUserNameWithDetailsV2(name);
    }

    // 유저 정보 수정 (최초생성시에는 null로 기입)
    @PostMapping("/editMember")
    public int getDetails(@RequestBody DetailsDto detailsDto, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        memberService.join(detailsDto, getUsername(authentication));
        return response.getStatus();
    }

    // 친구 추가
//    @PostMapping("/addFriend")
//    public int followMember(@RequestBody FolloweeNameDto followeeName, Authentication authentication, HttpServletResponse response, HttpServletRequest request) {
//        memberService.makeFollow(followeeName.getFolloweeName(), getUsername(authentication));
//        return response.getStatus();
//    }

    // 친구 추가
    @PostMapping("/addFriend")
    public FolloweeReturner addFriend (@RequestBody FolloweeNameDto followeeName, Authentication authentication, HttpServletResponse response, HttpServletRequest request) {
        return memberService.makeFollow(followeeName.getFolloweeName(), getUsername(authentication));
    }

    // 친구 삭제
    @PostMapping("/removeFriend")
    public int unfollowMember(@RequestBody FolloweeNameDto followeeName, Authentication authentication, HttpServletResponse response, HttpServletRequest request) {
        memberService.makeUnFollow(followeeName.getFolloweeName(), getUsername(authentication));
        return response.getStatus();
    }

    // 전체 친구 조회
    @GetMapping("/findFriend")
    public List<String> getFollowers(Authentication authentication) {
        String username = getUsername(authentication);
        return memberService.getFollowers(username);
    }

    private String getUsername(Authentication authentication) {
        String name = authentication.getName();
        return name;
    }

//    private String getUsername (HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        SecurityContextImpl context = (SecurityContextImpl)session.getAttribute("SPRING_SECURITY_CONTEXT");
//        return context.getAuthentication().getName();
//    }
}
