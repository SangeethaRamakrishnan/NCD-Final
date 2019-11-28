package com.mfc.ncd;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppVersionModel
{
    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    @SerializedName("app_version")
    @Expose
    private String app_version;

}
