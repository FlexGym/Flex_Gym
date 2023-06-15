package com.ll.FlexGym.domain.Member.entitiy;

import com.ll.FlexGym.domain.Favorite.entity.Favorite;
import com.ll.FlexGym.global.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@RequiredArgsConstructor
@Entity
@SuperBuilder
@EqualsAndHashCode(of = {"id"}, callSuper = true)
public class Member extends BaseEntity {

    private String username;
    private String password;
    private String providerTypeCode;

    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<Favorite> favorites;

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