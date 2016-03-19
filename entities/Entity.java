package physics.entities;

import physics.core.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {
    
    static AtomicInteger nextId = new AtomicInteger();
    private int id;
    
    protected double inverseMass;
    protected double restitution = .95;
    
    protected Vector3 pos;
    protected Vector3 vel = new Vector3();
    protected Vector3 acc = new Vector3(0, -9.81, 0);
    protected Vector3 force = new Vector3();
    
    protected AABB aabb;
    
    public Entity(double inverseMass) {
        id = nextId.incrementAndGet();
        this.inverseMass = inverseMass;
    }
    
    public boolean equals(Entity that) {
        return this.id == that.id;
    }
    
    public boolean lessThan(Entity that) {
        return this.id < that.id;
    }
    
    public abstract void draw();
    
    public boolean intersects(Entity that) {
        return aabb.intersects(that.aabb);
    }
    
    public boolean intersects(AABB aabb) {
        return this.aabb.intersects(aabb);
    }
    
    public abstract ContactData getContactData(Entity entity);
    
    public double calcSeparatingVel(Entity entity, Vector3 contactNormal) {
        return (vel.sub(entity.vel)).dot(contactNormal);
    }
    
    public void resolveCollision(Entity that, Vector3 contactNormal, double penetrationDepth) {
        double separatingVel = calcSeparatingVel(that, contactNormal);
        if (separatingVel > 0) return;
        
        double deltaVel = -separatingVel*(1+restitution);
        
        double totalInverseMass = this.inverseMass + that.inverseMass;
        if (totalInverseMass <= 0) return;
        
        // Resolve interpenetration
        this.pos.addUpdate(contactNormal.mult(penetrationDepth*this.inverseMass/totalInverseMass));
        that.pos.addUpdate(contactNormal.mult(-penetrationDepth*that.inverseMass/totalInverseMass));
        
        // Resolve collision
        double impulse = deltaVel/totalInverseMass;
        Vector3 impulsePerMass = contactNormal.mult(impulse);
        if (this.inverseMass > 0) this.vel.addUpdate(impulsePerMass.mult(this.inverseMass));
        if (that.inverseMass > 0) that.vel.addUpdate(impulsePerMass.mult(-that.inverseMass));
    }
    
    public void addForce(Vector3 force) {
        this.force.addUpdate(force);
    }
    
    public void addImpulse(Vector3 impulse) {
        vel.addUpdate(impulse.mult(inverseMass));
    }
    
    public boolean integrate(double dt) {
        if (inverseMass > 0) {
            acc.addUpdate(force.mult(inverseMass));
            vel.addUpdate(acc.mult(dt));
            pos.addUpdate(vel.mult(dt).add(acc.mult(dt*dt*.5)));
            return true;
        } else return false;
    }
    
    public void resetAccumulators() {
        acc.reset(0, 9.81, 0);
        force.reset(0, 0, 0);
    }
    
    
    public static class ContactData {
        
        public final double depth;
        public final Vector3 normal;

        public ContactData(double depth, Vector3 normal) {
            this.depth = depth;
            this.normal = normal;
        }
        
        public void flip() {
            normal.multUpdate(-1);
        }
    }
}