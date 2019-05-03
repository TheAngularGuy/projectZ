package com.projectz.teamz.projectZ.classUtils;

/**
 * Coordinate (X,Y)
 * Created by musta on 19/04/2017.
 */
public class Coordinate {
    private double x;
    private double y;

    public Coordinate() {
    }

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(String str) {
        String[] tokens = str.split("[,]");
        this.x = Double.parseDouble(tokens[0]);
        this.y = Double.parseDouble(tokens[1]);
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    /**
     * See if two coordinate are equal. use only on points
     * @param o coordinate
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        return y == that.y;

    }
}