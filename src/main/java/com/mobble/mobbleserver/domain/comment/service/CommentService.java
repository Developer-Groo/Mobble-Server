package com.mobble.mobbleserver.domain.comment.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentListResponseDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.comment.validator.CommentValidator;
import com.mobble.mobbleserver.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(memberId, memberId);
        Member member = clubMember.getMember();
        Article article = findArticleOrThrow(articleId);
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
        ClubMember clubMember = clubMemberValidator.findClubMemberByClubIdAndMemberIdOrThrow(memberId, memberId);
        Member member = clubMember.getMember();
        Article article = findArticleOrThrow(articleId);

        Comment parentComment = commentValidator.findCommentByCommentIdOrThrow(parentCommentId);
        Comment comment = dto.toEntity(member, article, parentComment);

        return CommentResponseDto.toDto(commentRepository.save(comment));
    }

    public List<CommentListResponseDto> getCommentListByArticle(Long articleId) {
        Article article = findArticleOrThrow(articleId);
        // Todo: 좋아요 갯수 반환 필요
        return commentRepository.findCommentsWithRepliesByArticleId(article.getId()).stream()
                .map(CommentListResponseDto::toDto)
                .toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long memberId, Long commentId, CommentRequestDto dto) {
        Comment comment = commentValidator.findCommentByCommentIdAndMemberIdOrThrow(memberId, commentId);
        Comment updatedComment = comment.updateContent(dto.content());

        return CommentResponseDto.toDto(updatedComment);
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        Comment comment = commentValidator.findCommentByCommentIdAndMemberIdOrThrow(memberId, commentId);
        commentRepository.delete(comment);
    }

    private Article findArticleOrThrow(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용 및 validator 접근
    }
}
