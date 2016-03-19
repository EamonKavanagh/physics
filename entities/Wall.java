package physics.entities;

import edu.princeton.cs.algs4.StdDraw;
import physics.core.*;

public class Wall extends Rectangle {
    
    private Vector3 halfDimension;
    
    public Wall(Vector3 pos, Vector3 halfDimension) {
        super(pos, halfDimension, 1.0);
        
        aabb = new AABB(pos, halfDimension);
    }
    
    public static void main(String[] args) {
        Wall r = new Wall(new Vector3(.5, .5, .5), new Vector3(.35, .1, .1));
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