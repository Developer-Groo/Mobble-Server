package com.mobble.mobbleserver.refactor.member.controller;

import com.mobble.mobbleserver.refactor.member.dto.request.MemberUpdateRequestDto;
import com.mobble.mobbleserver.refactor.member.dto.response.MemberResponseDto;
import com.mobble.mobbleserver.refactor.member.service.MemberService;
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
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.getMember(memberId));
    }

    @PatchMapping
    public ResponseEntity<MemberResponseDto> updateMember(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @RequestBody @Valid MemberUpdateRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.updateMember(memberId, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        memberService.deleteMember(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
