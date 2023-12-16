package com.km.model;

import jakarta.persistence.*;

@Entity
@NamedQuery(name = "DBMsg.countByStatus", query = "select count(*) from DBMsg t where t.status = ?1")
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
