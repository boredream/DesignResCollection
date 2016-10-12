package com.boredream.bdcodehelper.entity;

import java.util.Map;

public class Where {
    public static final String OP_INQUERY = "$inQuery";

    private Map<String, String> where;
    private String className;

    public Map<String, String> getWhere() {
        return where;
    }

    public void setWhere(Map<String, String> where) {
        this.where = where;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
