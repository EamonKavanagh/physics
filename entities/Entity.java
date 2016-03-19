package physics.entities;

import physics.core.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {
    
    static AtomicInteger nextId = new AtomicInteger();
    private int id;
    
    protected double inverseMass;
    protected double restitution = 1;
    
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
    
    public abstract Vector3 getContactNormal(Entity entity);
    
    public double calcSeparatingVel(Entity entity, Vector3 contactNormal) {
        return (vel.sub(entity.vel)).dot(contactNormal);
    }
    
    public void resolveCollision(Entity entity, Vector3 contactNormal) {
        double separatingVel = calcSeparatingVel(entity, contactNormal);
        if (separatingVel > 0) return;
        
        //RESTITUTION?
        double deltaVel = -separatingVel*(1+restitution);
        
        double totalInverseMass = inverseMass + entity.inverseMass;
        double impulse = deltaVel/totalInverseMass;
        Vector3 impulsePerMass = contactNormal.mult(impulse);
        if (inverseMass > 0) vel.addUpdate(impulsePerMass.mult(inverseMass));
        if (entity.inverseMass > 0) entity.vel.addUpdate(impulsePerMass.mult(-entity.inverseMass));
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
}