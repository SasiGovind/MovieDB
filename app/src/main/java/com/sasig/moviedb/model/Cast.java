package com.sasig.moviedb.model;

import com.google.gson.annotations.SerializedName;

public class Cast {
    @SerializedName("name")
    private String actorName;

    @SerializedName("id")
    private int actorId;

    @SerializedName("profile_path")
    private String profileImagePath;

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

}
