package physics.entities;

import edu.princeton.cs.algs4.StdDraw;
import physics.core.*;

public class Ball extends Entity {
    
    private double r;
    
    public Ball(Vector3 pos, double r, double inverseMass) {
        super(inverseMass);
        this.pos = pos;
        this.r = r;
        
        aabb = new AABB(this.pos, new Vector3(r, r, r));
    }
    
    public ContactData getContactData(Entity entity) {
        if (entity instanceof Ball) return getContactData((Ball) entity);
        else if (entity instanceof Rectangle) return getContactData((Rectangle) entity);
        else return null;
    }
    
    public ContactData getContactData(Ball ball) {
        Vector3 contactNormal = pos.sub(ball.pos);
        double depth = (r + ball.r)*(r + ball.r) - contactNormal.square_magnitude();
        if (depth >= 0) {
            double magnitude = contactNormal.magnitude();
            depth = r + ball.r - magnitude;
            contactNormal.multUpdate(1.0/magnitude);
            return new ContactData(depth, contactNormal);
        } else return null;
    }
    
    public ContactData getContactData(Rectangle rect) {
        double x, y, z;
        Vector3 pt = rect.closestPtTo(pos);
        double depth = r*r - pt.sub(pos).square_magnitude();
        if (depth >= 0) {
            double magnitude = pt.sub(pos).magnitude();
            depth = r - magnitude;
            return new ContactData(depth, rect.findNormal(pt));
        }
        else return null;
        
    }
    
    public void draw() {
        StdDraw.circle(pos.x(), pos.y(), r);
    }
    
    
    public static void main(String[] args) {
        Ball b = new Ball(new Vector3(.5, .5, .5), .1, 1);
        b.draw();
        b.aabb.draw();
        
        double t = 0.0;
        double dt = .002;
        while (t < 1) {
            StdDraw.clear();
            t += dt;
            b.integrate(dt);
            b.draw();
            b.aabb.draw();
            StdDraw.show(16);
        }
    }
}