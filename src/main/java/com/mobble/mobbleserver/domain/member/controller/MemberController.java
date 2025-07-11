package com.mobble.mobbleserver.domain.member.controller;

import com.mobble.mobbleserver.domain.member.dto.request.MemberCreateRequestDto;
import com.mobble.mobbleserver.domain.member.dto.request.MemberUpdateRequestDto;
import com.mobble.mobbleserver.domain.member.dto.response.MemberCreateResponseDto;
import com.mobble.mobbleserver.domain.member.dto.response.MemberResponseDto;
import com.mobble.mobbleserver.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberCreateResponseDto> createMember(
            @RequestBody @Valid MemberCreateRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.createMember(dto));
    }

    @GetMapping
    public ResponseEntity<MemberResponseDto> getMember() {
        Long memberId = 1L;

        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.getMember(memberId));
    }

    @PatchMapping
    public ResponseEntity<MemberResponseDto> updateMember(
            @RequestBody @Valid MemberUpdateRequestDto dto
    ) {
        Long memberId = 1L;

        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.updateMember(memberId, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember() {
        Long memberId = 1L;
        memberService.deleteMember(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}