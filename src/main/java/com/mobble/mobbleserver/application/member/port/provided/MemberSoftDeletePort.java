package com.mobble.mobbleserver.application.member.port.provided;

public interface MemberSoftDeletePort {

    void softDeleteMember(Long memberId);
}
