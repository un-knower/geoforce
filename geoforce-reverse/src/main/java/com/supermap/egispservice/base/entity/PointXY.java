package com.supermap.egispservice.base.entity;

import java.io.Serializable;

public class PointXY implements Serializable {

    private static final long serialVersionUID = 1L;
    private double x;
    private double y;

    public PointXY() {
    }

    public PointXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "{x:" + x + ", y:" + y + '}';
    }
}
