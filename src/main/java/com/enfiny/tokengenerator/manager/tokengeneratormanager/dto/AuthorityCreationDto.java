package com.enfiny.tokengenerator.manager.tokengeneratormanager.dto;

import javax.validation.constraints.NotNull;

public class AuthorityCreationDto {
    @NotNull(message = "Authority name cannot be null")
    private String authority;
    @NotNull(message = "GrantAccessId cannot be null")
    private Long grantAccessId;
    @NotNull(message = "AppId cannot be null")
    private Long appId;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getGrantAccessId() {
        return grantAccessId;
    }

    public void setGrantAccessId(Long grantAccessId) {
        this.grantAccessId = grantAccessId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
