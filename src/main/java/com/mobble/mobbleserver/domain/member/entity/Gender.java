package com.mobble.mobbleserver.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mobble.mobbleserver.global.exception.common.DomainException;
import com.mobble.mobbleserver.global.exception.errorCode.member.MemberValidationErrorCode;

public enum Gender {
    MALE,
    FEMALE;

    @JsonCreator
    public static Gender from(String value) {
        return switch (value.toLowerCase()) {
            case "male" -> MALE;
            case "female" -> FEMALE;
            default -> throw new DomainException(MemberValidationErrorCode.INVALID_GENDER);
        };
    }
}
