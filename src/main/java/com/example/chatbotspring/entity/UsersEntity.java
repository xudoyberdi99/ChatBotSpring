package com.example.chatbotspring.entity;

import com.example.chatbotspring.entity.base.BaseEntity;
import com.example.chatbotspring.entity.state.BotState;
import com.example.chatbotspring.entity.role.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handle"})
public class UsersEntity extends BaseEntity<Long> implements UserDetails {


    private String name;
    private String username;
    private String password;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum  role = UserRoleEnum.USER;

    @Enumerated(EnumType.STRING)
    private BotState botState=BotState.START;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<RoomEntity> rooms;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));
    }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public String getUsername() { return this.username; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
