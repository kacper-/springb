package com.km.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DBMsg {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String mkey;
    private String val;
    private boolean status;

    public DBMsg() {
    }

    public DBMsg(Long id, String mkey, String val, boolean status) {
        this.id = id;
        this.mkey = mkey;
        this.val = val;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getMkey() {
        return mkey;
    }

    public String getVal() {
        return val;
    }

    public boolean isStatus() {
        return status;
    }
}
