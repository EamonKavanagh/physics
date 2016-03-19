package physics.core;

import java.util.LinkedList;
import edu.princeton.cs.algs4.StdDraw;
import physics.entities.*;

public class Octree {
    
    private LinkedList<Entity> entities;
    private boolean isBranch;
    private final AABB aabb;
    private final int MAXSIZE = 8;
    private Octree[] children;
    
    public Octree(AABB aabb) {
        entities = new LinkedList<Entity>();
        isBranch = false;
        this.aabb = aabb;
    }
    
    public void insert(Entity entity) {
        if (entity.intersects(aabb)) {
            if (entities.size() < MAXSIZE) entities.add(entity);
            else if (isBranch) insertIntoOctants(entity);
            else {
                subdivide();
                insertIntoOctants(entity);
            }
        }
    }
            
    private void subdivide() {
        Vector3 center = aabb.getCenter();
        Vector3 ohd = aabb.getHalfDimension().mult(.5);
        children = new Octree[8];
        children[0] = new Octree(new AABB(center.add(ohd), ohd));
        children[1] = new Octree(new AABB(center.add(ohd.mult(-1, +1, +1)), ohd));
        children[2] = new Octree(new AABB(center.add(ohd.mult(-1, -1, +1)), ohd));
        children[3] = new Octree(new AABB(center.add(ohd.mult(+1, -1, +1)), ohd));
        children[4] = new Octree(new AABB(center.add(ohd.mult(+1, +1, -1)), ohd));
        children[5] = new Octree(new AABB(center.add(ohd.mult(-1, +1, -1)), ohd));
        children[6] = new Octree(new AABB(center.add(ohd.mult(-1, -1, -1)), ohd));
        children[7] = new Octree(new AABB(center.add(ohd.mult(+1, -1, -1)), ohd));
        for (Entity entity : entities) {
            for (Octree child : children) {
                child.insert(entity);
            }
        }
        isBranch = true;
        entities.clear();
    }
    
    public void delete(Entity entity) {
        if (entity.intersects(aabb)) {
            if (isBranch) {
                for (Octree child: children) {
                    child.delete(entity);
                }
            } else {
                entities.remove(entity);
            }
        }
    }
    
    public void update(Entity entity) {
        delete(entity);
        insert(entity);
    }
    
    private void insertIntoOctants(Entity entity) {
        for (Octree child : children) {
            child.insert(entity);
        }
    }
    
    public Iterable<Entity> query(Entity entity) {
        LinkedList<Entity> list = new LinkedList<Entity>();
        return query(entity, list);
    }
    
    private LinkedList<Entity> query(Entity entity, LinkedList<Entity> list) {
        if (entity.intersects(aabb)) {
            if (isBranch) {
                for (Octree child : children) {
                    child.query(entity, list);
                }
            } else {
                for (Entity e : entities) {
                    if (e!= null && !(e.equals(entity))) list.add(e);
                }
            }
        }
        return list;
    }
    
    public void draw() {
        aabb.draw();
        if (isBranch) {
            for (Octree child : children) {
                child.draw();
            }
        } else {
            for (Entity entity: entities) {
                if (entity != null) entity.draw();
            }
        }
    }
    
    
    public static void main(String[] args) {
        Octree o = new Octree(new AABB(new Vector3(.5, .5, 0), new Vector3(.5, .5, .5)));
        
        for (int i = 0; i < 500; i++) {
            o.insert(new Ball(new Vector3(Math.random()/1.2 + .1, Math.random()/1.2 + .1, 0), .015));
        }
        o.draw();
    }
    
    
}