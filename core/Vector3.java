package physics.core;

public class Vector3 {
    
    private double x, y, z;
    
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3() {
        this.x = 0; this.y = 0; this.z = 0;
    }
    
    public double x() {
        return x;
    }
    
    public double y() {
        return y;
    }
    
    public double z() {
        return z;
    }
    
    public void addUpdate(Vector3 v) {
        x += v.x; y += v.y; z += v.z;
    }
    
    public void addUpdate(double c) {
        x += c; y += c; z += c;
    }
    
    public Vector3 add(Vector3 v) {
        return new Vector3(x+v.x, y+v.y, z+v.z);
    }
    
    public Vector3 add(double c) {
        return new Vector3(x+c, y+c, z+c);
    }
    
    public void subUpdate(Vector3 v) {
        x -= v.x; y -= v.y; z -= v.z;
    }
    
    public Vector3 sub(Vector3 v) {
        return new Vector3(x-v.x, y-v.y, z-v.z);
    }
    
    public void multUpdate(Vector3 v) {
        x *= v.x; y *= v.y; z *= v.z;
    }
    
    public void multUpdate(double c) {
        x *= c; y *= c; z *= c;
    }
    
    public Vector3 mult(Vector3 v) {
        return new Vector3(x*v.x, y*v.y, z*v.z);
    }
    
    public Vector3 mult(double c) {
        return new Vector3(x*c, y*c, z*c);
    }
    
    public Vector3 mult(double a, double b, double c) {
        return new Vector3(x*a, y*b, z*c);
    }
    
    public boolean equals(Vector3 v) {
        return x == v.x && y == v.y && z == v.z;
    }
    
    public double dot(Vector3 v) {
        return x*v.x + y*v.y + z*v.z;
    }
    
    public double square_magnitude() {
        return x*x + y*y + z*z;
    }
    
    public double magnitude() {
        return Math.sqrt(square_magnitude());
    }
    
    public double square_distance(Vector3 v) {
        return sub(v).square_magnitude();
    }
    
    public void normalize() {
        double norm = magnitude();
        if (norm > 0) multUpdate(1.0/norm);
    }
    
    public Vector3 abs() {
        return new Vector3(Math.abs(x), Math.abs(y), Math.abs(z));
    }
    
    public boolean isLessThan(Vector3 v) {
        return x < v.x && y < v.y && z < v.z;
    }
    
    public void reset(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
    }
    
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
    
    
    public static void main(String[] args) {
        Vector3 v1 = new Vector3(.023, .21, .442);
        Vector3 v2 = new Vector3(.05, 1, 2);
        System.out.println(v2.add(v1));
    }
}