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
    
    public Vector3 getContactNormal(Entity entity) {
        if (entity instanceof Ball) return getContactNormal((Ball) entity);
        else if (entity instanceof Rectangle) return getContactNormal((Rectangle) entity);
        else return null;
    }
    
    public Vector3 getContactNormal(Ball ball) {
        Vector3 contactNormal = pos.sub(ball.pos);
        if (contactNormal.square_magnitude() < (r + ball.r)*(r + ball.r)) {
            contactNormal.normalize();
            return contactNormal;
        } else {
            return null;
        }
    }
    
    public Vector3 getContactNormal(Rectangle rect) {
        double x, y, z;
        x = pos.x(); y = pos.y(); z = pos.z();
        if (x < rect.min(0)) x = rect.min(0);
        if (x > rect.max(0)) x = rect.max(0);
        if (y < rect.min(1)) y = rect.min(1);
        if (y > rect.max(1)) y = rect.max(1);
        if (z < rect.min(2)) z = rect.min(2);
        if (z > rect.max(2)) z = rect.max(2);
        Vector3 pt = new Vector3(x, y, z);
        return rect.findNormal(pt);
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