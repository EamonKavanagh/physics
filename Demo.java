import edu.princeton.cs.algs4.StdDraw;
import physics.core.*;
import physics.entities.*;
import java.util.Arrays;

public class Demo {
    
    public static void main(String[] args) {
        Ball ball = new Ball(new Vector3(Math.random()/10 +.4, .5, .5), .05, 1);
        Ball ball2 = new Ball(new Vector3(.47, .151, .5), .05, 0);
        Ball ball3 = new Ball(new Vector3(.71, .151, .5), .05, 0);
        Ball ball4 = new Ball(new Vector3(Math.random()/10 +.7, .5, .5), .05, 1);
        Rectangle bottom = new Rectangle(new Vector3(.5, 0, 1), new Vector3(.5, .1, 1), 0);
        Rectangle left = new Rectangle(new Vector3(0, .5, 1), new Vector3(.1, .399, 1), 0);
        Rectangle right = new Rectangle(new Vector3(1, .5, 1), new Vector3(.1, .399, 1), 0);
        
        Entity[] entities = {ball, ball2, ball3, ball4, bottom, left, right};
        Engine engine = new Engine(Arrays.asList(entities), new AABB(new Vector3(.5, .5, 0), new Vector3(.5, .5, .5)));
    }
    
}