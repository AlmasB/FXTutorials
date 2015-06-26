package com.almasb.tutorial27;

public class DataMessage implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public final double x1, y1, x2, y2;

    public DataMessage(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}
