package com.mobble.mobbleserver.domain.article.service;

import com.mobble.mobbleserver.domain.article.dto.request.ArticleRequestDto;
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
import com.mobble.mobbleserver.domain.comment.dto.response.CommentListResponseDto;
import com.mobble.mobbleserver.domain.comment.service.CommentService;
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

        return ArticleResponseDto.toDto(
                articleRepository.save(article),
                false,
                true,
                0,
                List.of());
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

        List<CommentListResponseDto> commentListByArticle = commentService.getCommentListByArticle(articleId);
        Optional<ArticleLike> checkLiked = articleLikeRepository.findLikedByArticleIdAndMemberId(article.getId(),
                memberId);

        boolean likedByMe = checkLiked.isPresent();
        boolean isMine = article.getMember().getId().equals(memberId);

        return ArticleResponseDto.toDto(
                article,
                likedByMe,
                isMine,
                commentListByArticle.size(),
                commentListByArticle);
    }

    private String summarize(String content) {
        if (content == null) return "";
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
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
        return clubMemberRepository.findByClubIdAndMemberId(clubId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용 및 validator 접근
    }

    private Article findArticleOrThrow(long ArticleId) {
        return articleRepository.findById(ArticleId)
                .orElseThrow(() -> new IllegalArgumentException(""));// Todo: Custom 예외 적용 및 validator 접근
    }
}
