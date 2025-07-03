package com.mobble.mobbleserver.like.articleLike.controller;

import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.like.articleLike.dto.response.ArticleLikeToggleResponseDto;
import com.mobble.mobbleserver.like.articleLike.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles/{article-Id}/likes")
public class ArticleLikeController {
}
