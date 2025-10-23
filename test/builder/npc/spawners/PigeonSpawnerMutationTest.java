package builder.npc.spawners;

import builder.entities.npc.spawners.PigeonSpawner;
import engine.timing.TickTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Mutation coverage tests for PigeonSpawner.
 */
public class PigeonSpawnerMutationTest {
    
    private PigeonSpawner spawner;
    
    @Before
    public void setUp() {
        spawner = new PigeonSpawner(800, 800, 200);
    }
    
    @Test
    public void testGetXReturnsCorrectValue() {
        assertEquals("getX should return constructor value", 800, spawner.getX());
    }
    
    @Test
    public void testGetXAfterSetX() {
        spawner.setX(100);
        assertEquals("getX should return value after setX", 100, spawner.getX());
    }
    
    @Test
    public void testGetXWithNegative() {
        spawner.setX(-50);
        assertEquals("getX should handle negative values", -50, spawner.getX());
    }
    
    @Test
    public void testGetXWithZero() {
        spawner.setX(0);
        assertEquals("getX should handle zero", 0, spawner.getX());
    }
    
    @Test
    public void testGetYReturnsCorrectValue() {
        assertEquals("getY should return constructor value", 800, spawner.getY());
    }
    
    @Test
    public void testGetYAfterSetY() {
        spawner.setY(200);
        assertEquals("getY should return value after setY", 200, spawner.getY());
    }
    
    @Test
    public void testGetYWithZero() {
        spawner.setY(0);
        assertEquals("getY should handle zero", 0, spawner.getY());
    }
    
    @Test
    public void testGetYWithNegative() {
        spawner.setY(-100);
        assertEquals("getY should handle negative values", -100, spawner.getY());
    }
    
    @Test
    public void testTimerNotNull() {
        TickTimer timer = spawner.getTimer();
        assertNotNull("Timer should not be null", timer);
    }
    
    @Test
    public void testTimerIsTickTimer() {
        TickTimer timer = spawner.getTimer();
        assertTrue("Timer should be TickTimer instance", timer instanceof TickTimer);
    }
    
    @Test
    public void testSetXMutationDetection() {
        spawner.setX(500);
        int result = spawner.getX();
        assertEquals("setX must actually set the value", 500, result);
        assertNotEquals("setX should change the value", 800, result);
    }
    
    @Test
    public void testSetYMutationDetection() {
        spawner.setY(300);
        int result = spawner.getY();
        assertEquals("setY must actually set the value", 300, result);
        assertNotEquals("setY should change the value", 800, result);
    }
    
    @Test
    public void testDefaultConstructor() {
        PigeonSpawner defaultSpawner = new PigeonSpawner(100, 200);
        assertEquals("Default constructor should set X", 100, defaultSpawner.getX());
        assertEquals("Default constructor should set Y", 200, defaultSpawner.getY());
        assertNotNull("Default constructor should create timer", defaultSpawner.getTimer());
    }
    
    @Test
    public void testConstructorWithDuration() {
        PigeonSpawner customSpawner = new PigeonSpawner(400, 600, 150);
        assertEquals("Constructor with duration should set X", 400, customSpawner.getX());
        assertEquals("Constructor with duration should set Y", 600, customSpawner.getY());
        assertNotNull("Constructor with duration should create timer", customSpawner.getTimer());
    }
    
    @Test
    public void testMultipleSetXCalls() {
        spawner.setX(100);
        spawner.setX(200);
        spawner.setX(300);
        assertEquals("Multiple setX calls should use last value", 300, spawner.getX());
    }
    
    @Test
    public void testMultipleSetYCalls() {
        spawner.setY(50);
        spawner.setY(150);
        spawner.setY(250);
        assertEquals("Multiple setY calls should use last value", 250, spawner.getY());
    }
    
    @Test
    public void testGetXReturnValue() {
        // Test that getX actually returns the x coordinate, not 0
        PigeonSpawner spawner1 = new PigeonSpawner(999, 100);
        int x = spawner1.getX();
        assertTrue("getX should return non-zero value when set", x != 0);
        assertEquals("getX should return exact value", 999, x);
    }
    
    @Test
    public void testGetYReturnValue() {
        // Test that getY actually returns the y coordinate, not 0
        PigeonSpawner spawner1 = new PigeonSpawner(100, 888);
        int y = spawner1.getY();
        assertTrue("getY should return non-zero value when set", y != 0);
        assertEquals("getY should return exact value", 888, y);
    }
}
