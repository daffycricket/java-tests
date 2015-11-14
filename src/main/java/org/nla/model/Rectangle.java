package org.nla.model;

public class Rectangle {
    private final int width;

    private final int length;

    public Rectangle(int length, int width) {
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        if (width < 0) {
            throw new IllegalArgumentException("width < 0");
        }

        this.length = length;
        this.width = width;
    }

    public int getArea() {
        return width * length;
    }

    @Override
    public String toString() {
        return this.length + " x " + this.width;
    }
}
