package physics.core;

import physics.entities.Entity;
import java.util.LinkedList;

public class Collision {
    
    private static Iterable<Event> broadPhase(Iterable<Entity> entities, Octree octree) {
        Iterable<Entity> queryResults;
        LinkedList<Event> possibleCollisions = new LinkedList<Event>();
        for (Entity entity: entities) {
            queryResults = octree.query(entity);
            for (Entity result : queryResults) {
                if (entity.lessThan(result) && entity.intersects(result)) possibleCollisions.add(new Event(entity, result));
            }
        }
        return possibleCollisions;
    }
    
    private static Iterable<Event> narrowPhase(Iterable<Event> possibleCollisions) {
        LinkedList<Event> collisions = new LinkedList<Event>();
        for (Event possibleCollision : possibleCollisions) {
            if (possibleCollision.isCollision()) collisions.add(possibleCollision);
        }
        return collisions;
    }
    
    private static void resolveCollisions(Iterable<Event> collisions) {
        for (Event collision: collisions) {
            collision.resolve();
        }
    }
    
    public static void handleCollisions(Iterable<Entity> entities, Octree octree) {
        Iterable<Event> possibleCollisions = broadPhase(entities, octree);
        Iterable<Event> collisions = narrowPhase(possibleCollisions);
        resolveCollisions(collisions);
    }
    
    private static class Event {
        private Entity a;
        private Entity b;
        private Vector3 contactNormal;
        private double penetrationDepth;
        
        public Event(Entity a, Entity b) {
            this.a = a;
            this.b = b;
        }
        
        public void setContactNormal(Vector3 normal) {
            contactNormal = normal;
        }
        
        public boolean isCollision() {
            Entity.ContactData contact = a.getContactData(b);
            if (contact != null) {
                penetrationDepth = contact.depth;
                contactNormal = contact.normal;
                return true;
            } else return false;
        }
        
        public void resolve() {
            a.resolveCollision(b, contactNormal, penetrationDepth);
        }
    }
}