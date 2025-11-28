package com.mobble.mobbleserver.domain.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mobble.mobbleserver.domain.exception.DomainException;
import com.mobble.mobbleserver.domain.member.error.MemberError;

public enum Gender {
    MALE,
    FEMALE;

    @JsonCreator
    public static Gender from(String value) {
        return switch (value.toLowerCase()) {
            case "male" -> MALE;
            case "female" -> FEMALE;
            default -> throw new DomainException(MemberError.INVALID_GENDER);
        };
    }
}
