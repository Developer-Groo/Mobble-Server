package com.mobble.mobbleserver.domain.member.controller;

import com.mobble.mobbleserver.domain.member.dto.request.MemberCreateRequestDto;
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
@RequestMapping("/login")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
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


//    @PatchMapping("/{member-id}")

//    @DeleteMapping("/withdraw")

}
