package org.projetointegrador.unifio.projectintegratorviibackend.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum PermissionEnum implements GrantedAuthority {
    DOCTOR("ROLE_ADMIN"),
    PATIENT("ROLE_USER");

    private String description;

    PermissionEnum(String description) {
        this.description = description;
    }


    @Override
    public String getAuthority() {
        return description;
    }

    public String getDescription() {
        return this.description;
    }
}
