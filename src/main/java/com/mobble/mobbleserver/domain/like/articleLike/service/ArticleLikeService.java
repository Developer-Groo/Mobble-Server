package com.mobble.mobbleserver.domain.like.articleLike.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.like.articleLike.dto.response.ArticleLikeInfoResponseDto;
import com.mobble.mobbleserver.domain.like.articleLike.entity.ArticleLike;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikeService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
//    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public ArticleLikeInfoResponseDto toggleLike(Long articleId, Long memberId) {
        Member member = findMemberOrThrow(memberId);
        Article article = findArticleOrThrow(articleId);
//        ClubMember clubMember = findClubMemberOrThrow(clubMemberId);

        Optional<ArticleLike> checkLiked = articleLikeRepository.findLikedByArticleIdAndMemberId(article.getId(), member.getId());

        if (checkLiked.isPresent()) {
            articleLikeRepository.delete(checkLiked.get());
        } else {
            ArticleLike articleLike = ArticleLike.createArticleLike(article, member);
            articleLikeRepository.save(articleLike);
        }

        boolean isLiked = checkLiked.isEmpty();
        List<ArticleLike> articleLikes = articleLikeRepository.findAllByArticleId(article.getId());

        return ArticleLikeInfoResponseDto.toDto(article.getId(), isLiked, articleLikes);
    }

    public ArticleLikeInfoResponseDto getArticleLikeCountAndLikedMembers(Long articleId, Long memberId) {
        Article article = findArticleOrThrow(articleId);
        List<ArticleLike> articleLikes = articleLikeRepository.findAllByArticleId(article.getId());
        boolean isLiked = (memberId != null) && articleLikeRepository.existsLikedByArticleIdAndMemberId(article.getId(), memberId);

        return ArticleLikeInfoResponseDto.toDto(article.getId(), isLiked, articleLikes);
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
