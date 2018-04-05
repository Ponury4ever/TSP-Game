package models;

import java.util.Locale;

public class Edge {
    int id;
    double x; //model.City one
    double y; //model.City two
    double w; //weight

    public Edge(int id, double x, double y, double w) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.w = w;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%o\t%o\t%o\t%o\n", id, (int) x, (int) y, (int) w);
    }
}
