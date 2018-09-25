package com.gohenry.oauth2;

/**
 * Class for the foreseeable future as a holding place for the userId
 * which is currently present in PHP based tokens. We need this for now
 * whilst the PHP servers are active.
 */
public class UserAuthenticationDetails {

    private long userId;

    public UserAuthenticationDetails(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
