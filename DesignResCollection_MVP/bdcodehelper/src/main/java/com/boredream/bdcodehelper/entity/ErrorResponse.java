package com.boredream.bdcodehelper.entity;

public class ErrorResponse {

    private int code;
    private String error;

    public void setCode(int code) {
        this.code = code;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code=" + code +
                ", error='" + error + '\'' +
                '}';
    }
}

