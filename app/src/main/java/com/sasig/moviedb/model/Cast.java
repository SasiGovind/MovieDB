package com.sasig.moviedb.model;

import com.google.gson.annotations.SerializedName;

public class Cast {
    @SerializedName("name")
    private String actorName;

    @SerializedName("profile_path")
    private String profileImagePath;

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

}
