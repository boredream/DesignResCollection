package com.boredream.designrescollection.base;


import com.boredream.bdcodehelper.base.BoreBaseEntity;

public class BaseEntity extends BoreBaseEntity {

    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseEntity) {
            BaseEntity oEntity = (BaseEntity) o;
            return this.objectId.equals(oEntity.objectId);
        }
        return super.equals(o);
    }
}