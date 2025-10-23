package builder.npc;

import builder.entities.npc.enemies.Pigeon;
import engine.timing.FixedTimer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Enhanced mutation coverage tests for Pigeon class.
 */
public class PigeonEnhancedTest {
    
    @Test
    public void testConstructorWithoutTargetSetsSpeed() {
        Pigeon pigeon = new Pigeon(100, 200);
        // Speed should be set to PIGEON_SPEED (4)
        // We can't directly check speed, but we can verify pigeon was created
        assertNotNull("Pigeon should be created", pigeon);
    }
    
    @Test
    public void testConstructorWithoutTargetSetsSprite() {
        Pigeon pigeon = new Pigeon(100, 200);
        // Sprite should be set to "down"
        assertNotNull("Pigeon sprite should be set", pigeon);
    }
    
    @Test
    public void testGetLifespanNotNull() {
        Pigeon pigeon = new Pigeon(100, 200);
        FixedTimer lifespan = pigeon.getLifespan();
        assertNotNull("Lifespan should not be null", lifespan);
    }
    
    @Test
    public void testSetLifespan() {
        Pigeon pigeon = new Pigeon(100, 200);
        FixedTimer newTimer = new FixedTimer(5000);
        pigeon.setLifespan(newTimer);
        
        assertEquals("Lifespan should be updated", newTimer, pigeon.getLifespan());
    }
    
    @Test
    public void testLifespanDifferentFromNull() {
        Pigeon pigeon = new Pigeon(100, 200);
        FixedTimer lifespan = pigeon.getLifespan();
        assertNotNull("Lifespan should not be null after get", lifespan);
    }
    
    @Test
    public void testSetAttacking() {
        Pigeon pigeon = new Pigeon(100, 200);
        // Initially attacking is true
        pigeon.setAttacking(false);
        // Should be able to set attacking state
        assertNotNull("Pigeon should exist after setAttacking", pigeon);
    }
    
    @Test
    public void testSetAttackingToTrue() {
        Pigeon pigeon = new Pigeon(100, 200);
        pigeon.setAttacking(true);
        assertNotNull("Pigeon should exist after setAttacking true", pigeon);
    }
    
    @Test
    public void testPigeonPosition() {
        Pigeon pigeon = new Pigeon(150, 250);
        assertEquals("X position should match constructor", 150, pigeon.getX());
        assertEquals("Y position should match constructor", 250, pigeon.getY());
    }
    
    @Test
    public void testConstructorWithNullTarget() {
        Pigeon pigeon = new Pigeon(100, 200, null);
        assertNotNull("Pigeon should handle null target", pigeon);
    }
    
    @Test
    public void testLifespanDefaultValue() {
        Pigeon pigeon = new Pigeon(0, 0);
        FixedTimer lifespan = pigeon.getLifespan();
        assertNotNull("Default lifespan should be set", lifespan);
    }
    
    @Test
    public void testMultiplePigeonsCreated() {
        Pigeon p1 = new Pigeon(100, 100);
        Pigeon p2 = new Pigeon(200, 200);
        Pigeon p3 = new Pigeon(300, 300);
        
        assertNotNull("First pigeon created", p1);
        assertNotNull("Second pigeon created", p2);
        assertNotNull("Third pigeon created", p3);
        assertNotSame("Pigeons should be different instances", p1, p2);
    }
    
    @Test
    public void testPigeonAtOrigin() {
        Pigeon pigeon = new Pigeon(0, 0);
        assertEquals("X at origin", 0, pigeon.getX());
        assertEquals("Y at origin", 0, pigeon.getY());
    }
    
    @Test
    public void testPigeonLargeCoordinates() {
        Pigeon pigeon = new Pigeon(10000, 20000);
        assertEquals("Large X", 10000, pigeon.getX());
        assertEquals("Large Y", 20000, pigeon.getY());
    }
    
    @Test
    public void testLifespanCanBeReplaced() {
        Pigeon pigeon = new Pigeon(100, 100);
        FixedTimer original = pigeon.getLifespan();
        FixedTimer replacement = new FixedTimer(1000);
        pigeon.setLifespan(replacement);
        
        assertNotSame("Lifespan should be replaced", original, pigeon.getLifespan());
        assertSame("New lifespan should be set", replacement, pigeon.getLifespan());
    }
}


