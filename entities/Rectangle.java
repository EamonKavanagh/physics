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
    
    private double transformToAxis(Vector3 axis) {
        return halfDimension.x()*Math.abs(axis.dot(axes[0])) + 
            halfDimension.y()*Math.abs(axis.dot(axes[1])) + 
            halfDimension.z()*Math.abs(axis.dot(axes[2]));
    }
    
    private boolean overlapOnAxis(Rectangle rect, Vector3 axis) {
        double project1 = this.transformToAxis(axis);
        double project2 = rect.transformToAxis(axis);
        
        Vector3 toCenter = rect.pos.sub(pos);
        
        double distance = Math.abs(toCenter.dot(axis));
        
        return (distance <= project1 + project2);
    }
    
    private boolean isOverlapping(Rectangle rect) {
        return overlapOnAxis(rect, axes[0]) &&
            overlapOnAxis(rect, axes[1]) &&
            overlapOnAxis(rect, axes[2]) &&
            overlapOnAxis(rect, rect.axes[0]) &&
            overlapOnAxis(rect, rect.axes[1]) &&
            overlapOnAxis(rect, rect.axes[2]) &&
            overlapOnAxis(rect, axes[0].cross(rect.axes[0])) &&
            overlapOnAxis(rect, axes[0].cross(rect.axes[1])) &&
            overlapOnAxis(rect, axes[0].cross(rect.axes[2])) &&
            overlapOnAxis(rect, axes[1].cross(rect.axes[0])) &&
            overlapOnAxis(rect, axes[1].cross(rect.axes[1])) &&
            overlapOnAxis(rect, axes[1].cross(rect.axes[2])) &&
            overlapOnAxis(rect, axes[2].cross(rect.axes[0])) &&
            overlapOnAxis(rect, axes[2].cross(rect.axes[1])) &&
            overlapOnAxis(rect, axes[2].cross(rect.axes[2]));
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
        if (isOverlapping(rect)) {
            Vector3 pt1 = rect.closestPtTo(pos);
            double depth = .0001;
            Vector3 normal = rect.findNormal(pt1);
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
        Rectangle r1 = new Rectangle(new Vector3(.2, .2, 1), new Vector3(.05, .1, 1), 1);
        Rectangle r2 = new Rectangle(new Vector3(.5, .5, 1), new Vector3(.2, .05, 1), 1);
        Rectangle r3 = new Rectangle(new Vector3(.1, .5, 1), new Vector3(.1, .04, 1), 1);
        Rectangle r4 = new Rectangle(new Vector3(.55, .5, 1), new Vector3(.1, .1, 1), 1);
        Rectangle r5 = new Rectangle(new Vector3(.33, .76, 1), new Vector3(.075, .025, 1), 1);
        Rectangle r6 = new Rectangle(new Vector3(.1, .87, 1), new Vector3(.05, .03, 1), 1);
        
        Rectangle[] r = {r1, r2, r3, r4, r5, r6};
        
        for (int i = 0; i < r.length; i++) {
            r[i].draw();
            for (int j = i+1; j < r.length; j++) {
                System.out.println(r[i].isOverlapping(r[j]));
            }
        }
        
        System.out.println("XXXX");
        System.out.println(r4.isOverlapping(r2));
    }
}