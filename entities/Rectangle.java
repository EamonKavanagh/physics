package physics.entities;

import edu.princeton.cs.algs4.StdDraw;
import physics.core.*;

public class Rectangle extends Entity {
    
    private Vector3 halfDimension;
    private Vector3[] axes = new Vector3[3]; //Orientation of OBB axes
    
    public Rectangle(Vector3 pos, Vector3 halfDimension, double inverseMass) {
        super(inverseMass);
        this.pos = pos;
        this.halfDimension = halfDimension;
        
        aabb = new AABB(this.pos, this.halfDimension);
        
        axes[0] = new Vector3(1, 0, 0);
        axes[1] = new Vector3(0, 1, 0);
        axes[2] = new Vector3(0, 0, 1);
    }
    
    public Rectangle(Vector3 pos, Vector3 halfDimension, double inverseMass, Vector3 u1, Vector3 u2, Vector3 u3) {
        super(inverseMass);
        this.pos = pos;
        this.halfDimension = halfDimension;
        
        axes[0] = u1;
        axes[1] = u2;
        axes[2] = u3;
        
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
    
    public Vector3 closestPtTo(Vector3 p) {
        Vector3 d = p.sub(pos);
        Vector3 q = pos.copy();
        
        // For each OBB axis...
        for (int i = 0; i < 3; i++) {
            // Project d onto axis to get the distance
            double dist = d.dot(axes[i]);
            
            //Clamp to the box
            if (dist > halfDimension.coord(i)) dist = halfDimension.coord(i);
            if (dist < -halfDimension.coord(i)) dist = -halfDimension.coord(i);
            
            q.addUpdate(axes[i].mult(dist));
        }
        return q;
    }
    
    public ContactData getContactData(Entity entity) {
        if (entity instanceof Rectangle) return getContactData((Rectangle) entity);
        else if (entity instanceof Ball) {
            ContactData contact =  ((Ball) entity).getContactData(this);
            if (contact == null) return null;
            contact.flip();
            return contact;
        }
        else return null;
    }
    
    public ContactData getContactData(Rectangle rect) {
        if (pos.sub(rect.pos).mult(2).isLessThan(halfDimension.add(rect.halfDimension))) {
            System.out.println("Rect collision");
            Vector3 pt = rect.closestPtTo(pos);
            double depth = pos.sub(pt).magnitude();
            Vector3 normal = rect.findNormal(pt);
            return new ContactData(depth, normal);
        } else return null;
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
        Vector3 normal = new Vector3(x, y, z);
        normal.normalize();
        return normal;
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