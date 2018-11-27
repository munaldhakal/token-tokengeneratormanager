package com.enfiny.tokengenerator.manager.tokengeneratormanager.request;

import javax.validation.constraints.NotNull;

public class ClientEditRequest {
    @NotNull(message = "Client Id cannot be null")
    private String username;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
