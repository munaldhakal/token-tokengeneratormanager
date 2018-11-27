package com.enfiny.tokengenerator.manager.tokengeneratormanager.request;

import javax.validation.constraints.NotNull;

public class UserEditRequest {
    @NotNull(message = "UserId is required")
    private Long id;
    private String username;
    private String email;
    private String fullName;
    @NotNull(message = "AppId is required")
    private Long appId;
    private String grantType;
//    @NotNull(message = "Enabled cannot be null")
//    private boolean enabled;
//    @NotNull(message = "Account Not Locked status cannot be null")
//    private boolean accountNonLocked;
//    @NotNull(message = "Account Not Expired cannot be null")
//    private boolean accountNonExpired;
//    @NotNull(message = "Credential Not Expired cannot be null")
//    private boolean credentialNonExpired;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
//
//    public boolean isAccountNonLocked() {
//        return accountNonLocked;
//    }
//
//    public void setAccountNonLocked(boolean accountNonLocked) {
//        this.accountNonLocked = accountNonLocked;
//    }
//
//    public boolean isAccountNonExpired() {
//        return accountNonExpired;
//    }
//
//    public void setAccountNonExpired(boolean accountNonExpired) {
//        this.accountNonExpired = accountNonExpired;
//    }
//
//    public boolean isCredentialNonExpired() {
//        return credentialNonExpired;
//    }
//
//    public void setCredentialNonExpired(boolean credentialNonExpired) {
//        this.credentialNonExpired = credentialNonExpired;
//   }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
