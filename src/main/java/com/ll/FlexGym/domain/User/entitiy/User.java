package com.ll.FlexGym.domain.User.entitiy;

import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
public class User extends BaseEntity {

    private String username;
    private String password;
}