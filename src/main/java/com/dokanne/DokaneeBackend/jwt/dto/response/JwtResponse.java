package com.dokanne.DokaneeBackend.jwt.dto.response;

import java.util.Set;

public class JwtResponse {
    private String massage;
    private String token;
    private String type = "Bearer";

    private Set<String> roles;

    public JwtResponse(String m, String accessToken, Set<String> roleResponses) {
        massage = m;
        this.token = accessToken;
        roles = roleResponses;
    }

    public JwtResponse(String m) {
        massage = m;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public Set<String> getRole() {
        return roles;
    }


    public void setRole(Set<String> role) {
        this.roles = role;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }
}