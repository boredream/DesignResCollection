package com.boredream.bdcodehelper.entity;

import com.google.gson.Gson;

import java.io.Serializable;

public class Pointer implements Serializable {
    public static final String TYPE = "Pointer";

    protected String __type;
    protected String className;
    protected String objectId;

    public Pointer() {
    }

    public Pointer(String className, String objectId) {
        this.__type = TYPE;
        this.setClassName(className);
        this.setObjectId(objectId);
    }

    public String get__type() {
        return __type;
    }

    public void set__type(String __type) {
        this.__type = __type;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * 将Pointer转换为所需对象
     *
     * @param clazz 转换目标类型
     * @return 转换后的对象, 转换失败时为null
     */
    public <T> T parseToObject(Class<T> clazz) {
        T t = null;
        Gson gson = new Gson();
        try {
            // 原Pointer对象转为json, 如果用include查询的话, Pointer中除了className还会包含目标对象的数据
            String json = gson.toJson(this);
            // 转为目标对象, 多余的className正好通过转换过滤掉

            t = gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
