package com.enfiny.tokengenerator.manager.tokengeneratormanager.dto;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public class AppCreationDto {
    @NotNull(message = "App Name cannot be null")
    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
