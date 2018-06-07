package com.spcl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/6/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Record
{
    int id;
    String name;

    public Record() {
    }

    public Record(int id,String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
