package com.enfiny.tokengenerator.manager.tokengeneratormanager.dto;


import com.enfiny.tokengenerator.manager.tokengeneratormanager.enums.GrantType;

import javax.validation.constraints.NotNull;

public class GrantAccessCreationDto {
    @NotNull(message = "AppId cannot be null")
    private Long appId;
    @NotNull(message = "GrantType cannot be null")
    private GrantType grantType;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public GrantType getGrantType() {
        return grantType;
    }

    public void setGrantType(GrantType grantType) {
        this.grantType = grantType;
    }
}
