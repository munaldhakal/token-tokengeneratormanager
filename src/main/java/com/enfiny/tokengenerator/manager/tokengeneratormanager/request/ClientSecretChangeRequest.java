package com.enfiny.tokengenerator.manager.tokengeneratormanager.request;

import javax.validation.constraints.NotNull;

public class ClientSecretChangeRequest {
    @NotNull(message = "Secret cannot be null")
    private String secret;
    @NotNull(message = "Repeat Secret cannot be null")
    private String repeatSecret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRepeatSecret() {
        return repeatSecret;
    }

    public void setRepeatSecret(String repeatSecret) {
        this.repeatSecret = repeatSecret;
    }
}
