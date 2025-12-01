package com.mobble.mobbleserver.infrastructure.web.member;

import com.mobble.mobbleserver.application.member.command.UpdateMemberCommand;
import com.mobble.mobbleserver.application.member.port.provided.MemberQueryPort;
import com.mobble.mobbleserver.application.member.port.provided.MemberSoftDeletePort;
import com.mobble.mobbleserver.application.member.port.provided.MemberUpdatePort;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.infrastructure.web.member.dto.request.UpdateMemberRequestDto;
import com.mobble.mobbleserver.infrastructure.web.member.dto.response.MemberResponseDto;
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
public class MemberAPI {

    private final MemberQueryPort memberQueryPort;
    private final MemberUpdatePort memberUpdatePort;
    private final MemberSoftDeletePort memberDeletePort;


    @GetMapping
    public ResponseEntity<MemberResponseDto> getMember(
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        Member member = memberQueryPort.getMember(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(MemberResponseDto.toDto(member));
    }

    @PatchMapping
    public ResponseEntity<MemberResponseDto> updateMember(
            @AuthenticationPrincipal(expression = "memberId") Long memberId,
            @RequestBody @Valid UpdateMemberRequestDto dto
    ) {
        UpdateMemberCommand command = UpdateMemberCommand.create(memberId, dto);

        Member member = memberUpdatePort.updateMember(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(MemberResponseDto.toDto(member));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(
            @AuthenticationPrincipal(expression = "memberId") Long memberId
    ) {
        memberDeletePort.softDeleteMember(memberId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
