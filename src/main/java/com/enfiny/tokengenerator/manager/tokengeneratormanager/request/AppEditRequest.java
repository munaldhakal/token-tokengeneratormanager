package com.enfiny.tokengenerator.manager.tokengeneratormanager.request;

import javax.validation.constraints.NotNull;

public class AppEditRequest{
    @NotNull(message = "App Id cannot be null")
    private Long appId;
    @NotNull(message = "App Name cannot be null")
    private String appName;

        public Long getAppId() {
            return appId;
        }

        public void setAppId(Long appId) {
            this.appId = appId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }
}
