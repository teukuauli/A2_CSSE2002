package builder.npc;

import builder.entities.npc.Npc;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the Npc class.
 * Tests basic NPC functionality including movement and direction.
 */
public class NpcTest {

    private Npc npc;
    private MockEngineState mockEngine;
    private static final int X_POS = 100;
    private static final int Y_POS = 100;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        npc = new Npc(X_POS, Y_POS);
        mockEngine = new MockEngineState();
    }

    /**
     * Tests that NPC is constructed with correct position.
     */
    @Test
    public void testConstructorSetsPosition() {
        assertEquals(X_POS, npc.getX());
        assertEquals(Y_POS, npc.getY());
    }

    /**
     * Tests default speed is 1.
     */
    @Test
    public void testDefaultSpeed() {
        assertEquals(1.0, npc.getSpeed(), 0.01);
    }

    /**
     * Tests default direction is 0.
     */
    @Test
    public void testDefaultDirection() {
        assertEquals(0, npc.getDirection());
    }

    /**
     * Tests setting speed.
     */
    @Test
    public void testSetSpeed() {
        npc.setSpeed(5);
        assertEquals(5.0, npc.getSpeed(), 0.01);
    }

    /**
     * Tests setting direction.
     */
    @Test
    public void testSetDirection() {
        npc.setDirection(90);
        assertEquals(90, npc.getDirection());
        
        npc.setDirection(180);
        assertEquals(180, npc.getDirection());
    }

    /**
     * Tests move method changes position based on direction.
     */
    @Test
    public void testMoveRight() {
        npc.setDirection(0); // Move right
        npc.setSpeed(5);
        int initialX = npc.getX();
        
        npc.move();
        
        assertTrue(npc.getX() > initialX);
    }

    /**
     * Tests move method with 90 degree direction.
     */
    @Test
    public void testMoveDown() {
        npc.setDirection(90); // Move down
        npc.setSpeed(5);
        int initialY = npc.getY();
        
        npc.move();
        
        assertTrue(npc.getY() > initialY);
    }

    /**
     * Tests tick method causes movement.
     */
    @Test
    public void testTickCausesMovement() {
        npc.setDirection(0);
        npc.setSpeed(5);
        int initialX = npc.getX();
        
        npc.tick(mockEngine);
        
        assertTrue(npc.getX() != initialX);
    }

    /**
     * Tests distance calculation from position.
     */
    @Test
    public void testDistanceFromPosition() {
        Npc other = new Npc(X_POS + 30, Y_POS + 40);
        int distance = npc.distanceFrom(other);
        assertEquals(50, distance);
    }

    /**
     * Tests distance calculation from coordinates.
     */
    @Test
    public void testDistanceFromCoordinates() {
        int distance = npc.distanceFrom(X_POS + 30, Y_POS + 40);
        assertEquals(50, distance);
    }

    /**
     * Tests distance is zero for same position.
     */
    @Test
    public void testDistanceFromSamePosition() {
        int distance = npc.distanceFrom(X_POS, Y_POS);
        assertEquals(0, distance);
    }

    /**
     * Tests NPC is not marked for removal initially.
     */
    @Test
    public void testNotMarkedForRemovalInitially() {
        assertFalse(npc.isMarkedForRemoval());
    }

    /**
     * Tests mark for removal.
     */
    @Test
    public void testMarkForRemoval() {
        npc.markForRemoval();
        assertTrue(npc.isMarkedForRemoval());
    }

    /**
     * Tests setting and getting X coordinate.
     */
    @Test
    public void testSetAndGetX() {
        npc.setX(200);
        assertEquals(200, npc.getX());
    }

    /**
     * Tests setting and getting Y coordinate.
     */
    @Test
    public void testSetAndGetY() {
        npc.setY(200);
        assertEquals(200, npc.getY());
    }
}
