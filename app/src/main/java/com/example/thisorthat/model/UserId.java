package com.example.thisorthat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserId {

    @SerializedName("__type")
    @Expose
    private String type = "Pointer";
    @SerializedName("className")
    @Expose
    private String className = "_User";
    @SerializedName("objectId")
    @Expose
    private String objectId;

    public UserId(String objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}