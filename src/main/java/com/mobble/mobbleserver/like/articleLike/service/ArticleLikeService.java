package com.mobble.mobbleserver.like.articleLike.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import com.mobble.mobbleserver.like.articleLike.dto.response.ArticleLikeCountResponseDto;
import com.mobble.mobbleserver.like.articleLike.dto.response.ArticleLikeMemberResponseDto;
import com.mobble.mobbleserver.like.articleLike.dto.response.ArticleLikeToggleResponseDto;
import com.mobble.mobbleserver.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.like.articleLike.repository.ArticleLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikeService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
//    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public ArticleLikeToggleResponseDto toggleLike(Long articleId, Long memberId) {
        Member member = findMemberOrThrow(memberId);
        Article article = findArticleOrThrow(articleId);

//         // TODO ClubMember 구현 및 security 구현 후 리팩토링
//         boolean isClubMember = clubMemberRepository.existsByClubAndMember(article.getClub(), member);
//         if (!isClubMember) {
//             throw new IllegalStateException("클럽에 가입된 사용자만 좋아요를 누를 수 있습니다.");
//         }

        return articleLikeRepository.findByArticleAndMember(article, member)
                // like > unlike 변환
                .map(existingLike -> {
                    articleLikeRepository.delete(existingLike);
                    return ArticleLikeToggleResponseDto.toDto(false, article, member);
                })
                // unlike > like 변환
                .orElseGet(() -> {
                    ArticleLike articleLike = ArticleLike.createArticleLike(article, member);
                    articleLikeRepository.save(articleLike);
                    return ArticleLikeToggleResponseDto.toDto(true, article, member);
                });
    }

    public ArticleLikeCountResponseDto getArticleLikeCount(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        int count = articleLikeRepository.countByArticle(article);

        return ArticleLikeCountResponseDto.toDto(articleId, count);
    }

    public List<ArticleLikeMemberResponseDto> getArticleLikedMembers(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        return articleLikeRepository.findAllByArticle(article).stream()
                .map(like -> ArticleLikeMemberResponseDto.toDto(like.getMember()))
                .toList();
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용
    }

    private Article findArticleOrThrow(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용
    }
}
