package com.mobble.mobbleserver.domain.common;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity extends CreatedAtEntity {

    @LastModifiedDate
    protected LocalDateTime updatedAt;
}
