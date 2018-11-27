package com.enfiny.tokengenerator.manager.tokengeneratormanager.response;

public class GrantAccessResponseDto {
    private Long id;
    private String grantName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrantName() {
        return grantName;
    }

    public void setGrantName(String grantName) {
        this.grantName = grantName;
    }
}
