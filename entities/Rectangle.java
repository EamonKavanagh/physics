package physics.entities;

import edu.princeton.cs.algs4.StdDraw;
import physics.core.*;

public class Rectangle extends Entity {
    
    private Vector3 halfDimension;
    
    public Rectangle(Vector3 pos, Vector3 halfDimension, double inverseMass) {
        super(inverseMass);
        this.pos = pos;
        this.halfDimension = halfDimension;
        
        aabb = new AABB(this.pos, this.halfDimension);
    }
    
    public double min(int i) {
        if (i == 0) return pos.x() - halfDimension.x();
        else if (i == 1) return pos.y() - halfDimension.y();
        else if (i == 2) return pos.z() - halfDimension.z();
        else throw new IllegalArgumentException();
    }
    
    public double max(int i) {
        if (i == 0) return pos.x() + halfDimension.x();
        else if (i == 1) return pos.y() + halfDimension.y();
        else if (i == 2) return pos.z() + halfDimension.z();
        else throw new IllegalArgumentException();
    }
    
    public Vector3 getContactNormal(Entity entity) {
        if (entity instanceof Rectangle) return getContactNormal((Rectangle) entity);
        else if (entity instanceof Ball) return ((Ball) entity).getContactNormal(this).mult(-1);
        else return null;
    }
    
    public Vector3 getContactNormal(Rectangle rect) {
        return null;
    }
    
    public Vector3 findNormal(Vector3 pt) {
        int x, y, z;
        if (pt.x() == min(0)) x = -1;
        else if (pt.x() == max(0)) x = 1;
        else x = 0;
        if (pt.y() == min(1)) y = -1;
        else if (pt.y() == max(1)) y = 1;
        else y = 0;
        if (pt.z() == min(2)) z = -1;
        else if (pt.z() == max(2)) z = 1;
        else z = 0;
        return new Vector3(x, y, z);
    
    }
    
    public void draw() {
        StdDraw.rectangle(pos.x(), pos.y(), halfDimension.x(), halfDimension.y());
    }
    
    
    public static void main(String[] args) {
        Rectangle r = new Rectangle(new Vector3(.5, .5, .5), new Vector3(.35, .1, .1), 1);
        r.draw();
        r.aabb.draw();
        
        double t = 0.0;
        double dt = .002;
        while (t < 1) {
            StdDraw.clear();
            t += dt;
            r.integrate(dt);
            r.draw();
            r.aabb.draw();
            StdDraw.show(16);
        }
    }
}