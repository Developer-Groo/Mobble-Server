package com.mobble.mobbleserver.domain.club.core.service;

import com.mobble.mobbleserver.domain.article.repository.ArticleRepository;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.dto.response.ClubChatRoomPreviewResponseDto;
import com.mobble.mobbleserver.domain.chat.clubChatRoom.service.ClubChatRoomService;
import com.mobble.mobbleserver.domain.club.ageGroup.entity.AgeGroup;
import com.mobble.mobbleserver.domain.club.ageGroup.entity.AgeGroupType;
import com.mobble.mobbleserver.domain.club.ageGroup.repository.AgeGroupRepository;
import com.mobble.mobbleserver.domain.club.core.dto.request.ClubRequestDto;
import com.mobble.mobbleserver.domain.club.core.dto.response.ClubResponseDto;
import com.mobble.mobbleserver.domain.club.core.entity.Club;
import com.mobble.mobbleserver.domain.club.core.repository.ClubRepository;
import com.mobble.mobbleserver.domain.club.core.repository.dto.ClubLikeInfoDto;
import com.mobble.mobbleserver.domain.club.core.validator.ClubValidator;
import com.mobble.mobbleserver.domain.clubCategory.entity.ClubCategory;
import com.mobble.mobbleserver.domain.clubCategory.repository.ClubCategoryRepository;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMember;
import com.mobble.mobbleserver.domain.clubMember.entity.ClubMemberRole;
import com.mobble.mobbleserver.domain.clubMember.repository.ClubMemberRepository;
import com.mobble.mobbleserver.domain.clubMember.validator.ClubMemberValidator;
import com.mobble.mobbleserver.domain.comment.repository.CommentRepository;
import com.mobble.mobbleserver.domain.like.articleLike.repository.ArticleLikeRepository;
import com.mobble.mobbleserver.domain.like.clubLike.repository.ClubLikeRepository;
import com.mobble.mobbleserver.domain.like.commentLike.repository.CommentLikeRepository;
import com.mobble.mobbleserver.domain.member.entity.Member;
import com.mobble.mobbleserver.domain.member.validator.MemberValidator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubErrorCode;
import com.mobble.mobbleserver.global.exception.errorCode.club.ClubMemberErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {

    @Mock
    private ClubChatRoomService clubChatRoomService;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private ClubCategoryRepository clubCategoryRepository;

    @Mock
    private ClubMemberRepository clubMemberRepository;

    @Mock
    private AgeGroupRepository ageGroupRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private ArticleLikeRepository articleLikeRepository;

    @Mock
    private ClubLikeRepository clubLikeRepository;

    @Mock
    private ClubValidator clubValidator;

    @Mock
    private MemberValidator memberValidator;

    @Mock
    private ClubMemberValidator clubMemberValidator;

    @InjectMocks
    private ClubService clubService;
