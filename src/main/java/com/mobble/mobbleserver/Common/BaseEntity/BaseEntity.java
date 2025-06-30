package com.mobble.mobbleserver.Common.BaseEntity;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass
public class BaseEntity extends CreatedAtEntity {

    @LastModifiedDate
    protected LocalDateTime updatedAt;

}
