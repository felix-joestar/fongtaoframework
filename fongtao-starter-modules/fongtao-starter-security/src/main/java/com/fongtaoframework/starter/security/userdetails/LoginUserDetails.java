package com.fongtaoframework.starter.security.userdetails;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class LoginUserDetails implements UserDetails {

    private final String userId;
    private final String username;
    private final String name;
    private final String mobile;
    private final String email;
    private final String avatarFileId;
    private final List<GrantedAuthority> authorities;

    public LoginUserDetails(
            String userId,
            String username,
            String name,
            String mobile,
            String email,
            String avatarFileId,
            List<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.avatarFileId = avatarFileId;
        this.authorities = List.copyOf(authorities);
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarFileId() {
        return avatarFileId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
