package Focus_Zandi.version1.web.controller;

import Focus_Zandi.version1.domain.Member;
import Focus_Zandi.version1.domain.MemberDetails;
import Focus_Zandi.version1.domain.dto.DetailsDto;
import Focus_Zandi.version1.domain.dto.FolloweeNameDto;
import Focus_Zandi.version1.domain.dto.MemberReturnerDto;
import Focus_Zandi.version1.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @GetMapping("/getUserInfo")
    public MemberReturnerDto loginHandler(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String username = getUsername(request);
        MemberReturnerDto details = memberService.findMemberByUserNameWithDetails(username);
//        Member member = memberService.findMemberByUserName(username);
//        MemberReturnerDto memberReturnerDto = new MemberReturnerDto(member);
        return details;
    }

    @PostMapping("/doFollow")
    public int followMember(@RequestBody FolloweeNameDto followeeName, HttpServletResponse response, HttpServletRequest request) {
        memberService.makeFollow(followeeName.getFolloweeName(), getUsername(request));
        return response.getStatus();
    }

    @PostMapping("/undoFollow")
    public int unfollowMember(@RequestBody FolloweeNameDto followeeName, HttpServletResponse response, HttpServletRequest request) {
        memberService.makeUnFollow(followeeName.getFolloweeName(), getUsername(request));
        return response.getStatus();
    }

    //상세정보 기입 및 수정기능
    @PostMapping("/register")
    public int getDetails(@RequestBody DetailsDto detailsDto, HttpServletRequest request, HttpServletResponse response) {
        memberService.join(detailsDto, getUsername(request));
        return response.getStatus();
    }

    // 전체 팔로워 조회
    @GetMapping("/findMyFollowers")
    public List<String> getFollowers(HttpServletRequest request) {
        String username = getUsername(request);
        return memberService.getFollowers(username);
    }

    private String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        SecurityContextImpl context = (SecurityContextImpl)session.getAttribute("SPRING_SECURITY_CONTEXT");
        return context.getAuthentication().getName();
    }
}
