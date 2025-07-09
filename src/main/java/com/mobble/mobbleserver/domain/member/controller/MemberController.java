package com.mobble.mobbleserver.domain.member.controller;

import com.mobble.mobbleserver.domain.member.dto.request.MemberCreateRequestDto;
import com.mobble.mobbleserver.domain.member.dto.request.MemberUpdateRequestDto;
import com.mobble.mobbleserver.domain.member.dto.response.MemberCreateResponseDto;
import com.mobble.mobbleserver.domain.member.dto.response.MemberResponseDto;
import com.mobble.mobbleserver.domain.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping()
    public ResponseEntity<MemberCreateResponseDto> createMember(
            @Valid @RequestBody MemberCreateRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.createMember(dto));
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<MemberResponseDto> getMember(
            @PathVariable("member-id") @Positive Long memberId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.getMember(memberId));
    }

    @PatchMapping("/{member-id}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable("member-id") @Positive Long memberId,
            @Valid @RequestBody MemberUpdateRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(memberService.updateMember(memberId, dto));
    }

    @DeleteMapping("/{member-id}/withdraw")
    public ResponseEntity<Void> deleteMember(
            @PathVariable("member-id") @Positive Long memberId
    ) {
        memberService.deleteMember(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
