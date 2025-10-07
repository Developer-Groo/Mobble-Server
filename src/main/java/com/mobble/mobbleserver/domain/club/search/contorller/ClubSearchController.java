package com.mobble.mobbleserver.domain.club.search.contorller;

import com.mobble.mobbleserver.domain.club.search.dto.request.ClubSearchRequestDto;
import com.mobble.mobbleserver.domain.club.search.dto.response.ClubSummaryDto;
import com.mobble.mobbleserver.domain.club.search.service.ClubSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search/clubs")
public class ClubSearchController {

    private final ClubSearchService clubSearchService;

    @GetMapping
    public ResponseEntity<List<ClubSummaryDto>> searchClubs(
            @Validated ClubSearchRequestDto dto,
            @AuthenticationPrincipal(expression = "memberId") Long memberId
            ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(clubSearchService.searchClubs(dto, memberId));
    }

}
