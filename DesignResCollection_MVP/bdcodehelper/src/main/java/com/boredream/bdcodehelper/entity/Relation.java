package com.boredream.bdcodehelper.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Relation implements Serializable {
    private String __op = "AddRelation";
    private List<Pointer> objects = new ArrayList();

    public Relation(Pointer pointer) {
        this.objects.add(pointer);
    }

    public Relation() {
    }

    public void add(Pointer pointer) {
        this.objects.add(pointer);
    }

    public void remove(Pointer pointer) {
        this.__op = "RemoveRelation";
        this.objects.add(pointer);
    }

    public String get__op() {
        return this.__op;
    }

    public List<Pointer> getObjects() {
        return this.objects;
    }

    public void setObjects(List<Pointer> objects) {
        this.objects = objects;
    }
}
