package com.enfiny.tokengenerator.manager.tokengeneratormanager.request;


import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.GrantType;

import javax.validation.constraints.NotNull;

public class GrantAccessEditRequest {
    @NotNull(message = "AppId cannot be null")
    private Long appId;
    @NotNull(message = "GrantId cannot be null")
    private Long grantId;
    @NotNull(message = "GrantType cannot be null")
    private GrantType grantType;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getGrantId() {
        return grantId;
    }

    public void setGrantId(Long grantId) {
        this.grantId = grantId;
    }

    public GrantType getGrantType() {
        return grantType;
    }

    public void setGrantType(GrantType grantType) {
        this.grantType = grantType;
    }
}
