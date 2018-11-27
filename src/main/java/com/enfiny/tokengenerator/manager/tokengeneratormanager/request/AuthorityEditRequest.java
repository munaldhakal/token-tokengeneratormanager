package com.enfiny.tokengenerator.manager.tokengeneratormanager.request;

import javax.validation.constraints.NotNull;

public class AuthorityEditRequest {
    @NotNull(message = "AuthorityId cannot be null")
    private Long authorityId;
    @NotNull(message = "AppId cannot be null")
    private Long appId;
    @NotNull(message = "GrantAccess Id cannot be null")
    private Long grantAccessId;
    @NotNull(message = "Authority cannot be null")
    private String authority;

    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getGrantAccessId() {
        return grantAccessId;
    }

    public void setGrantAccessId(Long grantAccessId) {
        this.grantAccessId = grantAccessId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
