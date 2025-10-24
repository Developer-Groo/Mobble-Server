package com.mobble.mobbleserver.application.article.service;

import com.mobble.mobbleserver.application.article.port.provided.ArticleQueryPort;
import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.club.core.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentQueryPort;
import com.mobble.mobbleserver.application.comment.port.required.CommentReadPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.club.core.Club;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.article.ArticleErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.infrastructure.persistence.article.projection.ArticleLikeInfoDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.infrastructure.web.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.infrastructure.web.comment.dto.response.RootCommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleQueryService implements ArticleQueryPort {

    private final ArticleReadPort articleReadPort;
    private final ClubReadPort clubReadPort;
    private final CommentReadPort commentReadPort;

    private final CommentQueryPort commentQueryPort;

    @Override
    public List<ArticleSummaryResponseDto> findArticlesByClubId(Long clubId, ArticleType articleType, Long memberId) {
        Club club = findClubByClubIdOrThrow(clubId);
        List<Article> articles = articleReadPort.findArticlesByClubId(clubId, articleType);
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

    @Override
    public ArticleResponseDto findArticleById(Long articleId, Long memberId) {
        Article article = findArticleByArticleIdOrThrow(articleId);

        return convertToArticleResponseDto(article, memberId);
    }

    private Article findArticleByArticleIdOrThrow(Long articleId) {
        return articleReadPort.findById(articleId)
                .orElseThrow(() -> new DomainException(ArticleErrorCode.NOT_FOUND));
    }

    private Club findClubByClubIdOrThrow(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new DomainException((ClubErrorCode.NOT_FOUND)));
    }

    private ArticleResponseDto convertToArticleResponseDto(Article article, Long memberId) {
        Map<Long, ArticleLikeInfoDto> likeInfoMap = getArticleLikeInfo(List.of(article), memberId);
        ArticleLikeInfoDto likeInfo = likeInfoMap.getOrDefault(article.getId(), new ArticleLikeInfoDto(0, false));
        List<RootCommentResponseDto> comments = commentQueryPort.getCommentListByArticle(article.getId(), memberId);
        int commentCount = comments.size();

        boolean isMine = articleReadPort.existsArticleByIdAndMemberId(article.getId(), memberId);

        return ArticleResponseDto.toDto(article, isMine, likeInfo, commentCount, comments);
    }

    private Map<Long, ArticleLikeInfoDto> getArticleLikeInfo(List<Article> articles, Long memberId) {
        List<Long> articleIds = articles.stream()
                .map(Article::getId)
                .distinct()
                .toList();

        return articleReadPort.findLikeInfoByArticleIdsAndMemberId(articleIds, memberId);
    }

    private Map<Long, Integer> getArticleCommentCount(List<Article> articles) {
        List<Long> articleIds = articles.stream()
                .map(Article::getId)
                .distinct()
                .toList();

        return commentReadPort.countCommentsByArticleIds(articleIds);
    }
}
