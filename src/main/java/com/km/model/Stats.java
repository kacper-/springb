package com.km.model;

public class Stats {
    private int positive;
    private int negative;

    public Stats() {
    }

    public Stats(int positive, int negative) {
        this.positive = positive;
        this.negative = negative;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public int getNegative() {
        return negative;
    }

    public void setNegative(int negative) {
        this.negative = negative;
    }
}
