package com.mobble.mobbleserver.like.articleLike.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.like.articleLike.dto.response.ArticleLikeToggleResponseDto;
import com.mobble.mobbleserver.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikeService {
}
