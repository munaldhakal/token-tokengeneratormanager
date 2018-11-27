package com.enfiny.tokengenerator.manager.tokengeneratormanager.request;

import javax.validation.constraints.NotNull;

public class UserCreationRequest {
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "_id cannot be null")
    private String _id;
    @NotNull(message = "Password cannot be null")
    private String password;
    private String email;
    @NotNull(message = "AppId cannot be null")
    private Long appId;
    @NotNull(message = "GrantAccess cannot be null")
    private String grantAccess;
    @NotNull(message = "Full Name cannot be null")
    private String fullName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getGrantAccess() {
        return grantAccess;
    }

    public void setGrantAccess(String grantAccess) {
        this.grantAccess = grantAccess;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
