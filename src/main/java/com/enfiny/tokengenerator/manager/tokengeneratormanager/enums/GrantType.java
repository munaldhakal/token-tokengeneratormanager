package com.enfiny.tokengenerator.manager.tokengeneratormanager.enums;

public enum GrantType {
    SUPERADMIN,
    ADMIN,
    USER,
    CLIENT;
    private String grantType;

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
