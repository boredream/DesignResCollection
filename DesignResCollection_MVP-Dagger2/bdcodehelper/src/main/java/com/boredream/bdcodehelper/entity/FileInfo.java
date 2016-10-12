package com.boredream.bdcodehelper.entity;

import com.boredream.bdcodehelper.base.BoreBaseEntity;

public class FileInfo extends BoreBaseEntity {
    private String cdn;
    private String fiilename;
    private String url;

    public String getCdn() {
        return cdn;
    }

    public void setCdn(String cdn) {
        this.cdn = cdn;
    }

    public String getFiilename() {
        return fiilename;
    }

    public void setFiilename(String fiilename) {
        this.fiilename = fiilename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
