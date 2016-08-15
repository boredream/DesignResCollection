package com.boredream.bdcodehelper.entity;

import java.util.ArrayList;

public class ListResponse<T> {
    private ArrayList<T> results;

    public ArrayList<T> getResults() {
        return results;
    }

    public void setResults(ArrayList<T> results) {
        this.results = results;
    }
}
