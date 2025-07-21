package com.mobble.mobbleserver.domain.comment.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.dto.response.RootCommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.comment.repository.dto.CommentLikeInfoDto;
import com.mobble.mobbleserver.domain.comment.validator.CommentValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentValidator commentValidator;
    private final ClubMemberValidator clubMemberValidator;
    private final ArticleRepository articleRepository;

    @Transactional
    public CommentResponseDto createRootComment(Long memberId, Long articleId, CommentRequestDto dto) {
        Article article = findArticleOrThrow(articleId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);

        Member member = clubMember.getMember();
        Comment comment = dto.toEntity(member, article);

        return CommentResponseDto.toDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponseDto createReplyComment(
            Long memberId,
            Long articleId,
            Long parentCommentId,
            CommentRequestDto dto
    ) {
        Article article = findArticleOrThrow(articleId);
        Long clubId = article.getClub().getId();
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(clubId, memberId);
        Comment parentComment = commentValidator.findCommentByCommentIdOrThrow(parentCommentId);

        Member member = clubMember.getMember();
        Comment comment = dto.toEntity(member, article, parentComment);

        return CommentResponseDto.toDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, Long memberId, CommentRequestDto dto) {
        Comment comment = commentValidator.findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId);
        Comment updatedComment = comment.updateContent(dto.content());

        return CommentResponseDto.toDto(updatedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentValidator.findCommentByCommentIdAndMemberIdOrThrow(commentId, memberId);
        commentRepository.delete(comment);
    }

    public List<RootCommentResponseDto> getCommentListByArticle(Long articleId, Long memberId) {
        Article article = findArticleOrThrow(articleId);
        List<Comment> comments = commentRepository.findCommentsWithRepliesByArticleId(article.getId());
        Map<Long, CommentLikeInfoDto> likeInfoMap = getCommentLikeInfo(comments, memberId);

        return comments.stream()
                .map(comment -> RootCommentResponseDto.toDto(comment, likeInfoMap))
                .toList();
    }

    private Article findArticleOrThrow(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용 및 validator 접근
    }

    private Map<Long, CommentLikeInfoDto> getCommentLikeInfo(List<Comment> comments, Long memberId) {
        List<Long> commentIds = comments.stream()
                .flatMap(comment -> Stream.concat(
                        Stream.of(comment.getId()),
                        comment.getChildren().stream().map(Comment::getId)
                ))
                .distinct()
                .toList();

        return commentRepository.findLikeInfoByCommentIdsAndMemberId(commentIds, memberId);
    }

    public Map<Long, Integer> getArticleCommentCount(List<Article> articles) {
        List<Long> articleIds = articles.stream()
                .map(Article::getId)
                .distinct()
                .toList();

        return commentRepository.countCommentsByArticleIds(articleIds);
    }
}
