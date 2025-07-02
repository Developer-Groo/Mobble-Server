package com.mobble.mobbleserver.domain.comment.service;

import com.mobble.mobbleserver.domain.article.entity.Article;
import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.comment.dto.request.CommentRequestDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentListResponseDto;
import com.mobble.mobbleserver.domain.comment.dto.response.CommentResponseDto;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentResponseDto createRootComment(Long memberId, Long articleId, CommentRequestDto dto) {
        Member member = findMemberOrThrow(memberId);
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
        Member member = findMemberOrThrow(memberId);
        Article article = findArticleOrThrow(articleId);
        Comment parentComment = findCommentOrThrow(parentCommentId);
        Comment comment = dto.toEntity(member, article, parentComment);

        return CommentResponseDto.toDto(commentRepository.save(comment));
    }

    public List<CommentListResponseDto> getCommentListByArticle(Long articleId) {
        findArticleOrThrow(articleId);

        return commentRepository.findCommentsWithRepliesByArticleId(articleId).stream()
                .map(CommentListResponseDto::toDto)
                .toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long memberId, Long commentId, CommentRequestDto dto) {
        findMemberOrThrow(memberId);
        Comment comment = findCommentOrThrow(commentId);
        Comment updatedComment = comment.changeContent(dto.content());

        return CommentResponseDto.toDto(updatedComment);
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        findMemberOrThrow(memberId);
        Comment comment = findCommentOrThrow(commentId);
        commentRepository.delete(comment);
    }

    private Article findArticleOrThrow(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용
    }

    private Member findMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용
    }

    private Comment findCommentOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("")); // Todo: Custom 예외 적용
    }
}
