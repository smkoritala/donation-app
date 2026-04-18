package com.snowbird.donations.security.model;

import java.util.Collection;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Builder
public class CurrentUser {

    private String userId;
    private String name;
    private String email;
    private Set<String> roles;
    private Collection<? extends GrantedAuthority> authorities;
}
