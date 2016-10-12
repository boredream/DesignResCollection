package com.boredream.bdcodehelper.base;


import com.boredream.bdcodehelper.entity.Pointer;

/**
 * 数据实体基类
 * <p>
 * 之所以继承Pointer对象,是因为接口用法的限制,具体可以参考Bmob/LeanCloud/Parse的相关文档<br/>
 * 这样的接口可以让对象在提交创建和获取的时候都更加方便<br/>
 * 提交时只要添加Pointer的type和className等所需字段即可,获取时基本不用做任何额外处理
 */
public class BoreBaseEntity extends Pointer {

    // 添加新数据时,返回为objectId + createdAt
    // 更新数据时,返回为updateAt

    private String createdAt;
    private String updatedAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BoreBaseEntity) {
            BoreBaseEntity oEntity = (BoreBaseEntity) o;
            return this.objectId.equals(oEntity.objectId);
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "objectId='" + objectId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}