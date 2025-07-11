package com.mobble.mobbleserver.domain.article.controller;

import com.mobble.mobbleserver.domain.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs/articles")
public class ArticleController {

    private final ArticleService articleService;
}
