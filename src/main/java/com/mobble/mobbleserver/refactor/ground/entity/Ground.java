package com.mobble.mobbleserver.refactor.ground.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ground")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ground {

    @Id
    @Column(name = "ground_code", length = 20)
    private Long code;

    private String sido;

    private String sigungu;

    private String eubmyeondong;

}
