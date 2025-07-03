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
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
//    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public ArticleLikeToggleResponseDto toggleLike(Long articleId, Member member) {
        // TODO 예외처리 강화
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

//         // TODO ClubMember 구현 및 security 구현 후 리팩토링
//         boolean isClubMember = clubMemberRepository.existsByClubAndMember(article.getClub(), member);
//         if (!isClubMember) {
//             throw new IllegalStateException("클럽에 가입된 사용자만 좋아요를 누를 수 있습니다.");
//         }

        return articleLikeRepository.findByArticleAndMember(article, member)
                // like > unlike 변환
                .map(existingLike -> {
                    articleLikeRepository.delete(existingLike);
                    return ArticleLikeToggleResponseDto.toDto(false, articleId, member.getId());
                })
                // unlike > like 변환
                .orElseGet(() -> {
                    ArticleLike articleLike = ArticleLike.createArticleLike(article, member);
                    articleLikeRepository.save(articleLike);
                    return ArticleLikeToggleResponseDto.toDto(true, articleId, member.getId());
                });

    }
}
