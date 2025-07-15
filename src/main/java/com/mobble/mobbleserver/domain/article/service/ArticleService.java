package com.mobble.mobbleserver.domain.article.service;

import com.mobble.mobbleserver.domain.article.dto.request.ArticleRequestDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleDetailDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleResponseDto;
import com.mobble.mobbleserver.domain.article.dto.response.ArticleSummaryResponseDto;
import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.entity.ArticleType;
import com.mobble.mobbleserver.domain.article.repository.ArticleQueryDslRepository;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.club.entity.Club;
import com.mobble.mobbleserver.domain.club.repository.ClubRepository;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.repository.ClubMemberRepository;
import com.mobble.mobbleserver.domain.comment.dto.response.RootCommentResponseDto;
import com.mobble.mobbleserver.domain.comment.service.CommentService;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final CommentService commentService;

    private final ArticleRepository articleRepository;
    private final ArticleQueryDslRepository articleQueryDslRepository;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ArticleLikeRepository articleLikeRepository;


    @Transactional
    public ArticleResponseDto createArticle(Long memberId, Long clubId, ArticleRequestDto dto) {
        Member member = findMemberOrThrow(memberId);
        Club club = findClubOrThrow(clubId);
        Article article = dto.toEntity(club, member);
        ClubMember clubMember = findClubMemberOrThrow(clubId, memberId);

        if (dto.articleType() == ArticleType.NOTICE && clubMember.getClubMemberRole() == ClubMemberRole.MEMBER) {
            throw new IllegalArgumentException(""); // Todo: Custom 예외 적용 및 validator 접근
        }

        return ArticleResponseDto.toDto(articleRepository.save(article));
    }

    public List<ArticleSummaryResponseDto> findArticlesByClubId(Long clubId, ArticleType articleType) {
        Club club = findClubOrThrow(clubId);
        List<ArticleSummaryResponseDto> articles = articleQueryDslRepository.findArticlesByClubId(clubId, articleType);

        return articles.stream()
                .map(dto -> new ArticleSummaryResponseDto(
                        dto.articleId(),
                        dto.title(),
                        summarize(dto.content()),
                        dto.articleType(),
                        dto.clubId(),
                        dto.memberName(),
                        dto.likeCount(),
                        dto.commentCount(),
                        dto.createdAt(),
                        dto.updatedAt()
                ))
                .toList();

    }

    public ArticleResponseDto findArticleById(Long articleId, Long memberId) {
        Article article = findArticleOrThrow(articleId);
        ArticleDetailDto dto = articleQueryDslRepository.findArticleDetailById(articleId);
        List<RootCommentResponseDto> commentListByArticle = commentService.getCommentListByArticle(articleId, memberId);

        boolean likedByMe = isArticleLikedByMember(articleId, memberId);
        boolean isMine = isWriter(article.getMember().getId(), memberId);

        return ArticleResponseDto.toDto(dto, likedByMe, isMine, commentListByArticle);
    }

    @Transactional
    public ArticleResponseDto updateArticle(Long articleId, Long memberId, ArticleRequestDto dto) {
        Article article = findArticleOrThrow(articleId);
        ClubMember clubMember = findClubMemberOrThrow(article.getClub().getId(), memberId);

        boolean isMine = isWriter(article.getMember().getId(), memberId);

        if (!isMine) {
            throw new IllegalArgumentException(""); // Todo: Custom 예외 적용 및 validator 접근
        }
        if (dto.articleType() == ArticleType.NOTICE && clubMember.getClubMemberRole() == ClubMemberRole.MEMBER) {
            throw new IllegalArgumentException(""); // Todo: Custom 예외 적용 및 validator 접근
        }

        article.updateArticle(dto.articleType(), dto.title(), dto.content());

        ArticleDetailDto detailDto = articleQueryDslRepository.findArticleDetailById(articleId);
        boolean likedByMe = isArticleLikedByMember(articleId, memberId);
        List<RootCommentResponseDto> comments = commentService.getCommentListByArticle(articleId, memberId);

        return ArticleResponseDto.toDto(
                detailDto,
                likedByMe,
                true,
                comments);
    }

    @Transactional
    public void deleteArticle(Long articleId, Long memberId) {
        Article article = findArticleOrThrow(articleId);
        ClubMember clubMember = findClubMemberOrThrow(article.getClub().getId(), memberId);
        boolean isMine = isWriter(article.getMember().getId(), memberId);

        if (!isMine && clubMember.getClubMemberRole().equals(ClubMemberRole.MEMBER)) {
            throw new IllegalArgumentException(""); // Todo: Custom 예외 적용 및 validator 접근
        }
        articleRepository.delete(article);
    }

    private String summarize(String content) {
        if (content == null) return "";
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }

    private boolean isWriter(Long articleWriterId, Long memberId) {
        return articleWriterId.equals(memberId);
    }

    private boolean isArticleLikedByMember(Long articleId, Long memberId) {
        return articleLikeRepository.findLikedByArticleIdAndMemberId(articleId, memberId).isPresent();
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용 및 validator 접근
    }

    private Club findClubOrThrow(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용 및 validator 접근
    }

    private ClubMember findClubMemberOrThrow(Long clubId, Long memberId) {
        return clubMemberRepository.findClubMemberByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용 및 validator 접근
    }

    private Article findArticleOrThrow(long ArticleId) {
        return articleRepository.findById(ArticleId)
                .orElseThrow(() -> new IllegalArgumentException(""));// Todo: Custom 예외 적용 및 validator 접근
    }
}
