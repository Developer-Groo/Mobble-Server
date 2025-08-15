package com.mobble.mobbleserver.domain.article.service;

import com.mobble.mobbleserver.domain.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.article.repository.dto.ArticleLikeInfoDto;
import com.mobble.mobbleserver.domain.article.validator.ArticleValidator;
import com.mobble.mobbleserver.domain.club.club.entity.Club;
import com.mobble.mobbleserver.domain.club.club.repository.ClubRepository;
import com.mobble.mobbleserver.domain.club.club.validator.ClubValidator;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.dto.response.RootCommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.comment.service.CommentService;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.like.commentLike.repository.CommentLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final CommentService commentService;

    private final ArticleRepository articleRepository;
    private final ClubRepository clubRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArticleLikeRepository articleLikeRepository;

    private final ArticleValidator articleValidator;
    private final ClubValidator clubValidator;
    private final ClubMemberValidator clubMemberValidator;
    private final MemberValidator memberValidator;

    @Transactional
    public ArticleResponseDto createArticle(Long memberId, Long clubId, ArticleRequestDto dto) {
        Member member = memberValidator.findMemberByMemberIdOrThrow(memberId);
        Club club = clubValidator.findClubByClubIdOrThrow(clubId);
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        clubMember.assertCanPost(dto.articleType());
        Article article = dto.toEntity(club, member);

        return ArticleResponseDto.toDto(articleRepository.save(article));
    }

    public List<ArticleSummaryResponseDto> findArticlesByClubId(Long clubId, ArticleType articleType, Long memberId) {
        Club club = clubValidator.findClubByClubIdOrThrow(clubId);
        List<Article> articles = articleRepository.findArticlesByClubId(clubId, articleType);
        Map<Long, ArticleLikeInfoDto> likeInfoMap = getArticleLikeInfo(articles, memberId);
        Map<Long, Integer> commentCountMap = getArticleCommentCount(articles);

        return articles.stream()
                .map(article -> {
                    ArticleLikeInfoDto likeInfo = likeInfoMap.getOrDefault(article.getId(),ArticleLikeInfoDto.toDto(0,false));
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
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        article.assertOwnedBy(memberId);
        clubMember.assertCanPost(dto.articleType());

        article.updateArticle(dto.articleType(), dto.title(), dto.content());

        return convertToArticleResponseDto(article, memberId);
    }

    @Transactional
    public void deleteArticle(Long articleId, Long memberId) {
        Article article = articleValidator.findArticleByArticleIdOrThrow(articleId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        if (!article.isWrittenBy(memberId) && clubMember.getClubMemberRole() == ClubMemberRole.MEMBER) {
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
        Long writerId = article.getMember().getId();
        List<RootCommentResponseDto> comments = commentService.getCommentListByArticle(article.getId(), memberId);
        int commentCount = comments.size();

        boolean isMine = isWriter(writerId, memberId);

        return ArticleResponseDto.toDto(article, isMine, likeInfo, commentCount, comments);
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

    private boolean isWriter(Long articleWriterId, Long memberId) {
        return articleWriterId.equals(memberId);
    }
}
