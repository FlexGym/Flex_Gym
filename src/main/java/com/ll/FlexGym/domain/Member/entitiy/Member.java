package com.ll.FlexGym.domain.Member.entitiy;

import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
@EqualsAndHashCode(of = {"id"}, callSuper = true)
public class Member extends BaseEntity {

    private String username;
    private String password;

    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("MEMBER"));

        if (isAdmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        }

        return grantedAuthorities;
    }

    public boolean isAdmin() {
        return "admin".equals(username);
    }
}