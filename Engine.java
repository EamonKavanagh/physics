import java.util.LinkedList;
import physics.core.*;
import physics.entities.*;
import edu.princeton.cs.algs4.StdDraw;

public class Engine {
    
    private Iterable<Entity> entities;
    private Octree octree;
    private LinkedList<Entity> moved = new LinkedList<Entity>();
    private double dt = .002;
    
    public Engine(Iterable<Entity> entities, AABB world) {
        this.entities = entities;
        octree = new Octree(world);
        
        for (Entity entity : entities) {
            octree.insert(entity);
        }
        run();
    }
    
    private void run() {
        while (true) {
            StdDraw.clear();
            update();
            draw();
            StdDraw.show(16);
        }
    }
    
    private void run(double end) {
        double t = 0;
        while (t < end) {
            t += dt;
            StdDraw.clear();
            update();
            draw();
            StdDraw.show(16);
        }
    }
    
    private void update() {
        while (moved.size() > 0) {
            octree.update(moved.remove());
        }
        Collision.handleCollisions(entities, octree);
        
        boolean didMove;
        for (Entity entity: entities) {
            didMove = entity.integrate(dt);
            if (didMove) moved.add(entity);
        }
    }
    
    private void draw() {
        for (Entity entity : entities) {
            entity.draw();
        }
    }
    
}