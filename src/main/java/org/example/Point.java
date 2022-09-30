package org.example;

public class Point {
    Integer x, y;
    int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public void setCoord(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }


    public Point(){}

    public Point(int id, Integer x, Integer y){
        this.id = id;
        this.x = x;
        this.y = y;
    }
}
