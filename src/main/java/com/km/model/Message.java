package com.km.model;

import java.util.Objects;

public class Message {
    private String val;
    private boolean status;

    public Message() {
    }

    public Message(String val, boolean status) {
        this.val = val;
        this.status = status;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return status == message.status && Objects.equals(val, message.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val, status);
    }

    @Override
    public String toString() {
        return "Message{" +
                "val='" + val + '\'' +
                ", status=" + status +
                '}';
    }
}
