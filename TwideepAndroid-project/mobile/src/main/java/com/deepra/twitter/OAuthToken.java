package com.deepra.twitter;


public class OAuthToken {

    private String accessToken;

    private String tokenType;



    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAuthorization() {
        return getTokenType() + " " + getAccessToken();
    }

    public OAuthToken setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public OAuthToken setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

}
