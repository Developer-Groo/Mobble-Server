package com.mobble.mobbleserver.domain.like.articleLike.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import com.mobble.mobbleserver.domain.like.articleLike.dto.response.ArticleLikeCountResponseDto;
import com.mobble.mobbleserver.domain.like.articleLike.dto.response.ArticleLikeMemberResponseDto;
import com.mobble.mobbleserver.domain.like.articleLike.dto.response.ArticleLikeToggleResponseDto;
import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
//        ClubMember clubMember = findClubMemberOrThrow(clubMemberId);

        return articleLikeRepository.findByArticleIdAndMemberId(articleId, memberId)
                // like > unlike 변환
                .map(existingLike -> {
                            articleLikeRepository.delete(existingLike);
                            return ArticleLikeToggleResponseDto.toDto(article, member, false);
                        }
                )
                // unlike > like 변환
                .orElseGet(() -> {
                            ArticleLike articleLike = ArticleLike.createArticleLike(article, member);
                            articleLikeRepository.save(articleLike);
                            return ArticleLikeToggleResponseDto.toDto(article, member, true);
                        }
                );
    }

    // 메서드 통합
    public ArticleLikeCountResponseDto getArticleLikeCount(Long articleId) {
        Article article = findArticleOrThrow(articleId);
        int likeCount = articleLikeRepository.countByArticleId(articleId);
        // likeCount & likedMembers 통합시 사용
//        List<ArticleLikeMemberResponseDto> members = articleLikeRepository.findAllArticleLikedMemberByArticle(article).stream()
//                .map(like -> ArticleLikeMemberResponseDto.toDto(like.getMember()))
//                .toList();

        return ArticleLikeCountResponseDto.toDto(article, likeCount, Collections.emptyList());
    }

    public List<ArticleLikeMemberResponseDto> getArticleLikedMembers(Long articleId) {
        Article article = findArticleOrThrow(articleId);

        return articleLikeRepository.findAllMemberByArticleId(articleId).stream()
                .map(like -> ArticleLikeMemberResponseDto.toDto(like.getMember()))
                .toList();
    }

    /**
     * 예외처리 메서드 정리
     */
    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용
    }

    private Article findArticleOrThrow(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용
    }

//    private ClubMember findClubMemberOrThrow(Long clubMemberId) {
//        return clubMemberRepository.findById(clubMemberId)
//                .orElseThrow(() -> new IllegalArgumentException(""));
//    }
}
