package com.boredream.bdcodehelper.entity;

import com.boredream.bdcodehelper.base.BoreBaseEntity;

public class AppUpdateInfo extends BoreBaseEntity {
    private int version;
    private String versionName;
    private FileInfo apkFile;
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

    public FileInfo getApkFile() {
        return apkFile;
    }

    public void setApkFile(FileInfo apkFile) {
        this.apkFile = apkFile;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    /**
     * 获取下载文件名
     * @return
     */
    public String getDownloadTitle() {
        return "lvyinxiaojiang_" + version + ".apk";
    }
}
