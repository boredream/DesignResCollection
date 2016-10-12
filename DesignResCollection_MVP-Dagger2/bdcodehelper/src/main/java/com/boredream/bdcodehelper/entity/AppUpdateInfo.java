package com.boredream.bdcodehelper.entity;

import com.boredream.bdcodehelper.base.BoreBaseEntity;

public class AppUpdateInfo extends BoreBaseEntity {
    private int version;
    private String versionName;
    private String fileUrl;
    private String updateInfo;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }
}
