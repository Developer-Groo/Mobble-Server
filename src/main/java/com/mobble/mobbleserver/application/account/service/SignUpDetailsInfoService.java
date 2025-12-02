package com.mobble.mobbleserver.application.account.service;

import com.mobble.mobbleserver.application.account.command.SignUpCommand;
import com.mobble.mobbleserver.application.account.command.SocialUserInfo;
import com.mobble.mobbleserver.application.account.provided.SignUpDetailsPort;
import com.mobble.mobbleserver.application.account.required.JwtTokenIssuerPort;
import com.mobble.mobbleserver.application.account.required.SignUpTokenPort;
import com.mobble.mobbleserver.application.exception.BusinessException;
import com.mobble.mobbleserver.application.image.error.ImageBusinessError;
import com.mobble.mobbleserver.application.image.port.required.ImageReadPort;
import com.mobble.mobbleserver.application.member.port.required.MemberWritePort;
import com.mobble.mobbleserver.domain.common.Location;
import com.mobble.mobbleserver.domain.image.Image;
import com.mobble.mobbleserver.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpDetailsInfoService implements SignUpDetailsPort {

    private final SignUpTokenPort signUpTokenPort;
    private final JwtTokenIssuerPort jwtTokenIssuerPort;
    private final ImageReadPort imageReadPort;

    private final MemberWritePort memberWritePort;

    @Override
    public SocialUserInfo getSocialUserInfo(String signupToken) {

        return signUpTokenPort.extractSignUpInfo(signupToken);
    }

    @Override
    @Transactional
    public String signUp(SignUpCommand command) {
        SocialUserInfo userInfo = signUpTokenPort.extractSignUpInfo(command.signUpToken());
        Image profileImage = resolveProfileImage(command.profileImageId());

        Location location = Location.create(
                command.address1(),
                command.address2(),
                command.city(),
                command.district(),
                command.latitude(),
                command.longitude()
        );

        Member member = Member.create(
                command.name(),
                command.age(),
                command.gender(),
                userInfo.email(),
                command.phone(),
                location,
                profileImage,
                command.termsAgreed(),
                command.privacyAgreed(),
                userInfo.socialProvider(),
                userInfo.socialId()
        );

        memberWritePort.save(member);

        return jwtTokenIssuerPort.issueJwtToken(member.getId());
    }

    /* ==== Private Helper ==== */
    private Image resolveProfileImage(Long imageId) {
        return (imageId == null)
                ? null // Todo: getDefaultImage 메서드 호출
                : assertImageByImageId(imageId);
    }

    private Image assertImageByImageId(Long imageId) {
        return imageReadPort.findById(imageId)
                .orElseThrow(() -> new BusinessException(ImageBusinessError.NOT_FOUND));
    }
}
