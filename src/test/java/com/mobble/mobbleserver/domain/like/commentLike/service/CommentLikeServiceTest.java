package com.mobble.mobbleserver.domain.like.commentLike.service;

import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.entity.Comment;
import com.mobble.mobbleserver.domain.comment.validator.CommentValidator;
import com.mobble.mobbleserver.domain.like.commentLike.entity.CommentLike;
import com.mobble.mobbleserver.domain.like.commentLike.repository.CommentLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CommentLikeServiceTest {

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private CommentValidator commentValidator;

    @Mock
    private ClubMemberValidator clubMemberValidator;

    @InjectMocks
    CommentLikeService commentLikeService;

    private static final Long CLUB_ID = 1L;
    private static final Long MEMBER_Id = 2L;

    private Comment mockComment;
    private Member mockMember;
    private CommentLike mockCommentLike;

    @BeforeEach
    void setUP() {
        mockComment = mock(Comment.class);
        mockMember = mock(Member.class);
        mockCommentLike = mock(CommentLike.class);
    }
    
}
