package io.eskay.ticket_system.entity;

import jakarta.persistence.Embeddable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Embeddable
public class Authority implements GrantedAuthority {

    private String role;

    public Authority() {}

    public Authority(Role role) {
        this.role = role.name();
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authority authority)) return false;
        return Objects.equals(role, authority.role);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(role);
    }
}
