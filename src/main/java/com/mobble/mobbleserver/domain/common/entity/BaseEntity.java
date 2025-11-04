package com.mobble.mobbleserver.common.baseEntity;

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
