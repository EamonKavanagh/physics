package physics.core;
import edu.princeton.cs.algs4.StdDraw;

public class AABB {
    
    private Vector3 center, halfDimension;
    
    public AABB(Vector3 center, Vector3 halfDimension) {
        this.center = center;
        this.halfDimension = halfDimension;
    }
    
    public Vector3 getCenter() {
        return center;
    }
    
    public Vector3 getHalfDimension() {
        return halfDimension;
    }
    
    public boolean intersects(AABB that) {
        Vector3 v = center.sub(that.center);
        return v.abs().isLessThan(halfDimension.add(that.halfDimension));
    }
    
    public void draw() {
        StdDraw.rectangle(center.x(), center.y(), halfDimension.x(), halfDimension.y());
    }
    
    
    public static void main(String[] args) {
        Vector3 center1 = new Vector3(.25, .25, .25);
        Vector3 center2 = new Vector3(.4, .55, .25);
        Vector3 halfDim1 = new Vector3(.15, .15, .25);
        Vector3 halfDim2 = new Vector3(.05, .35, .25);
        
        AABB aabb1 = new AABB(center1, halfDim1);
        AABB aabb2 = new AABB(center2, halfDim2);
        
        aabb1.draw();
        aabb2.draw();
        System.out.println(aabb1.intersects(aabb2));
    }
}