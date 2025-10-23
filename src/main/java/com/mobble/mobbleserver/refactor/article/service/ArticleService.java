package com.mobble.mobbleserver.refactor.article.service;

import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentQueryPort;
import com.mobble.mobbleserver.application.member.port.required.MemberReadPort;
import com.mobble.mobbleserver.domain.comment.Comment;
import com.mobble.mobbleserver.domain.member.Member;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.comment.JpaCommentRepository;
import com.mobble.mobbleserver.infrastructure.web.comment.dto.response.RootCommentResponseDto;
import com.mobble.mobbleserver.refactor.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.refactor.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.refactor.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.refactor.article.entity.Article;
import com.mobble.mobbleserver.refactor.article.entity.ArticleType;
import com.mobble.mobbleserver.refactor.article.repository.ArticleRepository;
import com.mobble.mobbleserver.refactor.article.repository.dto.ArticleLikeInfoDto;
import com.mobble.mobbleserver.refactor.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.refactor.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.refactor.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.refactor.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.refactor.like.commentLike.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final CommentQueryPort commentQueryPort;

    private final ArticleRepository articleRepository;
    private final JpaCommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArticleLikeRepository articleLikeRepository;

    private final ArticleValidator articleValidator;
    private final ClubMemberValidator clubMemberValidator;

    private final ClubReadPort clubReadPort;
    private final MemberReadPort memberReadPort;

    @Transactional
    public ArticleResponseDto createArticle(Long memberId, Long clubId, ArticleRequestDto dto) {
        Club club = findClubByClubIdOrThrow(clubId);
        Member member = findMemberByMemberIdOrThrow(memberId);
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        assertCanPost(clubMember, dto.articleType());
        Article article = dto.toEntity(club, member);

        return ArticleResponseDto.toDto(articleRepository.save(article));
    }

    public List<ArticleSummaryResponseDto> findArticlesByClubId(Long clubId, ArticleType articleType, Long memberId) {
        Club club = findClubByClubIdOrThrow(clubId);
        List<Article> articles = articleRepository.findArticlesByClubId(clubId, articleType);
        Map<Long, ArticleLikeInfoDto> likeInfoMap = getArticleLikeInfo(articles, memberId);
        Map<Long, Integer> commentCountMap = getArticleCommentCount(articles);

        return articles.stream()
                .map(article -> {
                    ArticleLikeInfoDto likeInfo = likeInfoMap.getOrDefault(article.getId(), ArticleLikeInfoDto.toDto(0, false));
                    int commentCount = commentCountMap.getOrDefault(article.getId(), 0);
                    return ArticleSummaryResponseDto.toDto(article, likeInfo, commentCount);
                })
                .toList();
    }

    public ArticleResponseDto findArticleById(Long articleId, Long memberId) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);

        return convertToArticleResponseDto(article, memberId);
    }

    @Transactional
    public ArticleResponseDto updateArticle(Long articleId, Long memberId, ArticleRequestDto dto) {
        Article article = articleValidator.findArticleByArticleIdAndMemberIdOrThrow(articleId, memberId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        assertCanPost(clubMember, dto.articleType());
        article.updateArticle(dto.articleType(), dto.title(), dto.content());

        return convertToArticleResponseDto(article, memberId);
    }

    @Transactional
    public void deleteArticle(Long articleId, Long memberId) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        boolean isOwner = articleRepository.existsArticleByIdAndMemberId(articleId, memberId);

        if (!isOwner && clubMember.getClubMemberRole() == ClubMemberRole.MEMBER) {
            throw new DomainException(ArticleErrorCode.NO_PERMISSION);
        }

        List<Comment> comments = commentRepository.findCommentsWithRepliesByArticleId(articleId);

        commentLikeRepository.deleteAllByArticleId(articleId);
        commentRepository.deleteAll(comments);
        articleLikeRepository.deleteAllByArticleId(articleId);
        articleRepository.delete(article);
    }

    private ArticleResponseDto convertToArticleResponseDto(Article article, Long memberId) {
        Map<Long, ArticleLikeInfoDto> likeInfoMap = getArticleLikeInfo(List.of(article), memberId);
        ArticleLikeInfoDto likeInfo = likeInfoMap.getOrDefault(article.getId(), new ArticleLikeInfoDto(0, false));
        List<RootCommentResponseDto> comments = commentQueryPort.getCommentListByArticle(article.getId(), memberId);
        int commentCount = comments.size();

        boolean isMine = articleRepository.existsArticleByIdAndMemberId(article.getId(), memberId);

        return ArticleResponseDto.toDto(article, isMine, likeInfo, commentCount, comments);
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException((ClubErrorCode.NOT_FOUND)));
    }

    private Map<Long, ArticleLikeInfoDto> getArticleLikeInfo(List<Article> articles, Long memberId) {
        List<Long> articleIds = articles.stream()
                .map(Article::getId)
                .distinct()
                .toList();

        return articleRepository.findLikeInfoByArticleIdsAndMemberId(articleIds, memberId);
    }

    private Map<Long, Integer> getArticleCommentCount(List<Article> articles) {
        List<Long> articleIds = articles.stream()
                .map(Article::getId)
                .distinct()
                .toList();

        return commentRepository.countCommentsByArticleIds(articleIds);
    }

    private void assertCanPost(ClubMember clubMember, ArticleType articleType) {
        if (!clubMember.canPost(articleType)) {
            throw new DomainException(ArticleErrorCode.NOTICE_NO_PERMISSION);
        }
    }

    private Member findMemberByMemberIdOrThrow(Long memberId) {
        return memberReadPort.findByIdAndIsDeletedFalse(memberId)
                .orElseThrow(() -> new DomainException(MemberErrorCode.NOT_FOUND_MEMBER));
    }
}
