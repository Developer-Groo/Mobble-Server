package com.mobble.mobbleserver.domain.article.controller;

import com.mobble.mobbleserver.domain.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.article.service.ArticleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/{club-id}")
    public ResponseEntity<ArticleResponseDto> createArticle(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestBody @Valid ArticleRequestDto dto
    ) {
        Long memberId = 1L; // Todo: 임시 member id

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.createArticle(memberId, clubId, dto));
    }

    @GetMapping("/{club-id}")
    public ResponseEntity<List<ArticleSummaryResponseDto>> findArticlesByClubId(
            @PathVariable("club-id") @Positive Long clubId,
            @RequestParam(value = "articleType", required = false) ArticleType articleType
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.findArticlesByClubId(clubId,articleType));
    }
}
