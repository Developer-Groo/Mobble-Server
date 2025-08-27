package com.mobble.mobbleserver.domain.member.controller;

import com.mobble.mobbleserver.account.auth.principal.AuthMember;
import com.mobble.mobbleserver.domain.member.dto.request.MemberUpdateRequestDto;
import com.mobble.mobbleserver.domain.member.dto.response.MemberResponseDto;
import com.mobble.mobbleserver.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponseDto> getMember(
            @AuthenticationPrincipal AuthMember authMember
    ) {
        Long memberId = authMember.memberId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.getMember(memberId));
    }

    @PatchMapping
    public ResponseEntity<MemberResponseDto> updateMember(
            @AuthenticationPrincipal AuthMember authMember,
            @RequestBody @Valid MemberUpdateRequestDto dto
    ) {
        Long memberId = authMember.memberId();

        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.updateMember(memberId, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(
            @AuthenticationPrincipal AuthMember authMember
    ) {
        Long memberId = authMember.memberId();
        memberService.deleteMember(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
