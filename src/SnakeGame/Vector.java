package SnakeGame;

public class Vector {
    Vector() {}

    //If single value provided, set both x and y to the value
    Vector(double a) {
        this.x = a;
        this.y = a;
    }
    Vector(int a) {
        this.intX = a;
        this.intY = a;
    }

    //Set the value based on 2 inputs, for doubles and ints
    Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    Vector(int x, int y) {
        this.intX = x;
        this.intY = y;
    }

    //Instantiate values for double and integer storage in the vector
    double x;
    double y;
    Integer intX;
    Integer intY;

    //Divide v1 by v2, checking if dividing the int or the double.
    static Vector divide(Vector v1, Vector v2) {
        if(v1.intX != null && v1.intY != null){
            return new Vector(v1.intX / v2.intX, v1.intY / v2.intY);
        }
        else {
            return new Vector(v1.x / v2.x, v1.y / v2.y);
        }
    }

    //Returns the product of both axis
    public double area() {
        if(this.intX != null && this.intY != null){
            return (double) this.intX * this.intY;
        }
        else {
            return this.x * this.y;
        }
    }

    //Adding v1 by v2, checking if adding the int or the double.
    static Vector add(Vector v1, Vector v2) {
        if(v1.intX != null && v1.intY != null){
            return new Vector(v1.intX + v2.intX, v1.intY + v2.intY);
        }
        else {
            return new Vector(v1.x + v2.x, v1.y + v2.y);
        }
    }
}
