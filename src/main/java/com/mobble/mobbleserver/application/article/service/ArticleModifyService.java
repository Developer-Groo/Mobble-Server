package com.mobble.mobbleserver.application.article.service;

import com.mobble.mobbleserver.application.article.command.CreateArticleCommand;
import com.mobble.mobbleserver.application.article.command.UpdateArticleCommand;
import com.mobble.mobbleserver.application.article.error.ArticleBusinessError;
import com.mobble.mobbleserver.application.article.port.provided.ArticleCreatePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleDeletePort;
import com.mobble.mobbleserver.application.article.port.provided.ArticleUpdatePort;
import com.mobble.mobbleserver.application.article.port.required.ArticleReadPort;
import com.mobble.mobbleserver.application.article.port.required.ArticleWritePort;
import com.mobble.mobbleserver.application.club.error.ClubBusinessError;
import com.mobble.mobbleserver.application.club.port.required.ClubReadPort;
import com.mobble.mobbleserver.application.clubMember.error.ClubMemberBusinessError;
import com.mobble.mobbleserver.application.clubMember.port.required.ClubMemberReadPort;
import com.mobble.mobbleserver.application.comment.port.provided.CommentDeletePort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.image.error.ImageBusinessError;
import com.mobble.mobbleserver.application.image.port.provided.ImageDeletePort;
import com.mobble.mobbleserver.application.image.port.required.ImageReadPort;
import com.mobble.mobbleserver.application.like.port.provided.LikeModifyPort;
import com.mobble.mobbleserver.domain.article.Article;
import com.mobble.mobbleserver.domain.article.ArticleContent;
import com.mobble.mobbleserver.domain.article.ArticleType;
import com.mobble.mobbleserver.domain.club.Club;
import com.mobble.mobbleserver.domain.clubMember.ClubMember;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.like.LikeType;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleModifyService implements ArticleCreatePort, ArticleUpdatePort, ArticleDeletePort {

    private final CommentDeletePort commentDeletePort;
    private final LikeModifyPort likeModifyPort;
    private final ImageDeletePort imageDeletePort;

    private final ArticleWritePort articleWritePort;

    private final ArticleReadPort articleReadPort;
    private final ClubReadPort clubReadPort;
    private final ClubMemberReadPort clubMemberReadPort;
    private final ImageReadPort imageReadPort;

    @Override
    public Article create(CreateArticleCommand command) {
        ClubMember clubMember = assertMemberByClubIdAndMemberId(command.clubId(), command.memberId());

        assertCanPost(clubMember, command.type());

        ArticleContent content = ArticleContent.of(command.title(), command.content());

        Image image = resolveMainImage(command.imageId());

        Article article = Article.createArticle(
                clubMember.getClub(),
                clubMember.getMember(),
                command.type(),
                content,
                image
        );

        return articleWritePort.save(article);
    }

    @Override
    public Article update(UpdateArticleCommand command) {
        ClubMember clubMember = assertMemberByClubIdAndMemberId(command.clubId(), command.memberId());
        Club club = clubMember.getClub();
        Member member = clubMember.getMember();
        Article article = assertArticleByArticleIdAndClubId(command.articleId(), club.getId());

        assertCanUpdateArticle(member, article);

        ArticleContent content = ArticleContent.of(command.title(), command.content());

        Image image = resolveMainImage(command.imageId());

        return article.updateArticle(content, image);
    }

    @Override
    public void delete(Long clubId, Long articleId, Long memberId) {
        ClubMember clubMember = assertMemberByClubIdAndMemberId(clubId, memberId);
        Article article = assertArticleByArticleIdAndClubId(articleId, clubId);

        assertCanDeleteArticle(article, clubMember);

        commentDeletePort.deleteAll(clubMember.getId(), article.getId());
        likeModifyPort.delete(LikeType.ARTICLE, article.getId());

        if (article.getImage() != null) {
            imageDeletePort.delete(article.getImage().getId());
        }

        articleWritePort.delete(article);
    }

    @Override
    public void deleteAll(Long clubId) {
        Club club = assertClubByClubId(clubId);

        List<Long> articleIds = articleReadPort.findIdsByClubId(club.getId());
        if (articleIds.isEmpty()) return;

        List<Long> imageIds = articleReadPort.findImageIdsByClubId(club.getId());

        commentDeletePort.deleteAll(articleIds);
        likeModifyPort.deleteAll(LikeType.ARTICLE, articleIds);
        imageDeletePort.deleteAll(imageIds);
        articleWritePort.deleteAll(club.getId());
    }

    /* ==== Private Helper ==== */
    private Club assertClubByClubId(Long clubId) {
        return clubReadPort.findById(clubId)
                .orElseThrow(() -> new BusinessException(ClubBusinessError.NOT_FOUND));
    }

    private Article assertArticleByArticleIdAndClubId(Long articleId, Long clubId) {
        return articleReadPort.findByIdAndClubId(articleId, clubId)
                .orElseThrow(() -> new BusinessException(ArticleBusinessError.CLUB_MISMATCH));
    }

    private ClubMember assertMemberByClubIdAndMemberId(Long clubId, Long memberId) {
        return clubMemberReadPort.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new BusinessException(ClubMemberBusinessError.NOT_JOINED_CLUB));
    }

    private void assertCanPost(ClubMember clubMember, ArticleType articleType) {
        if (!clubMember.canPost(articleType)) throw new BusinessException(ArticleBusinessError.NO_PERMISSION);
    }

    private void assertCanUpdateArticle(Member member, Article article) {
        if (!article.isOwner(member.getId())) throw new BusinessException(ArticleBusinessError.NO_PERMISSION);
    }

    private void assertCanDeleteArticle(Article article, ClubMember clubMember) {
        if (clubMember.canManage()) return;

        if (article.isOwner(clubMember.getMember().getId())) return;

        throw new BusinessException(ArticleBusinessError.NO_PERMISSION);
    }

    private Image assertImageByImageId(Long imageId) {
        return imageReadPort.findById(imageId)
                .orElseThrow(() -> new BusinessException(ImageBusinessError.NOT_FOUND));
    }

    private Image resolveMainImage(Long imageId) {
        return (imageId == null)
                ? null
                : assertImageByImageId(imageId);
    }
}
