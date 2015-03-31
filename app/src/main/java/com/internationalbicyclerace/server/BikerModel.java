package com.internationalbicyclerace.server;

/**
 * Created by johnny on 15. 3. 23.
 */
public class BikerModel {
    private String name;
    private String email;
    private String facebookProfileLink;
    private String nationality;
    private String profileImageUrl;
    private String speed;
    private String facebookId;
    private boolean isFacebookProfileOpen;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFacebookProfileLink() {
        return facebookProfileLink;
    }

    public void setFacebookProfileLink(String facebookProfileLink) {
        this.facebookProfileLink = facebookProfileLink;
    }

    public boolean getIsFacebookProfileOpen() {
        return isFacebookProfileOpen;
    }

    public void setIsFacebookProfileOpen(boolean isFacebookProfileOpen) {
        this.isFacebookProfileOpen = isFacebookProfileOpen;
    }
}
