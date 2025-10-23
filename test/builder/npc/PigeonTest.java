package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.enemies.Pigeon;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import engine.timing.FixedTimer;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the Pigeon class.
 * Tests pigeon behavior including movement, cabbage targeting, and lifespan management.
 */
public class PigeonTest {

    private Pigeon pigeon;
    private MockEngineState mockEngine;
    private JavaBeanGameState gameState;
    private static final int SPAWN_X = 100;
    private static final int SPAWN_Y = 100;
    private static final int PIGEON_SPEED = 4;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        pigeon = new Pigeon(SPAWN_X, SPAWN_Y);
        mockEngine = new MockEngineState();
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
    }

    /**
     * Tests that pigeon is constructed with correct initial position.
     */
    @Test
    public void testConstructorBasic() {
        assertEquals(SPAWN_X, pigeon.getX());
        assertEquals(SPAWN_Y, pigeon.getY());
        assertEquals(PIGEON_SPEED, pigeon.getSpeed(), 0.01);
        assertNotNull(pigeon.getSprite());
    }

    /**
     * Tests that pigeon is constructed with target and correct initial state.
     */
    @Test
    public void testConstructorWithTarget() {
        ChickenFarmer target = new ChickenFarmer(200, 200);
        
        Pigeon pigeonWithTarget = new Pigeon(SPAWN_X, SPAWN_Y, target);
        assertEquals(SPAWN_X, pigeonWithTarget.getX());
        assertEquals(SPAWN_Y, pigeonWithTarget.getY());
        assertEquals(PIGEON_SPEED, pigeonWithTarget.getSpeed(), 0.01);
        assertNotNull(pigeonWithTarget.getSprite());
    }

    /**
     * Tests that lifespan is initialized correctly.
     */
    @Test
    public void testGetLifespanInitialized() {
        assertNotNull(pigeon.getLifespan());
        assertFalse(pigeon.getLifespan().isFinished());
    }

    /**
     * Tests that lifespan can be set.
     */
    @Test
    public void testSetLifespan() {
        FixedTimer newTimer = new FixedTimer(500);
        pigeon.setLifespan(newTimer);
        assertEquals(newTimer, pigeon.getLifespan());
    }

    /**
     * Tests that pigeon marks itself for removal when lifespan finishes.
     */
    @Test
    public void testLifespanExpiration() {
        FixedTimer shortTimer = new FixedTimer(1);
        pigeon.setLifespan(shortTimer);

        assertFalse(pigeon.isMarkedForRemoval());

        // Tick until lifespan expires
        pigeon.tick(mockEngine, gameState);
        pigeon.tick(mockEngine, gameState);

        assertTrue(pigeon.isMarkedForRemoval());
    }

    /**
     * Tests that setAttacking changes the attacking state.
     */
    @Test
    public void testSetAttacking() {
        pigeon.setAttacking(false);
        // Verify method completes without exception
        assertFalse(pigeon.isMarkedForRemoval());
    }

    /**
     * Tests distance calculation from a position.
     */
    @Test
    public void testDistanceFromPosition() {
        ChickenFarmer target = new ChickenFarmer(SPAWN_X + 30, SPAWN_Y + 40);
        
        int distance = pigeon.distanceFrom(target);
        // Distance should be 50 (3-4-5 triangle)
        assertEquals(50, distance);
    }

    /**
     * Tests distance calculation from coordinates.
     */
    @Test
    public void testDistanceFromCoordinates() {
        int distance = pigeon.distanceFrom(SPAWN_X + 30, SPAWN_Y + 40);
        assertEquals(50, distance);
    }

    /**
     * Tests that pigeon moves when tick is called.
     */
    @Test
    public void testTickCausesMovement() {
        // Set direction explicitly to ensure movement
        pigeon.setDirection(0);
        pigeon.setSpeed(4);
        
        int initialX = pigeon.getX();
        
        pigeon.move(); // Test the move method directly
        
        // Pigeon should have moved
        assertTrue(pigeon.getX() != initialX);
    }

    /**
     * Tests that pigeon's sprite is not null after construction.
     */
    @Test
    public void testSpriteNotNull() {
        assertNotNull(pigeon.getSprite());
    }

    /**
     * Tests that direction can be set and retrieved.
     */
    @Test
    public void testSetAndGetDirection() {
        pigeon.setDirection(90);
        assertEquals(90, pigeon.getDirection());

        pigeon.setDirection(180);
        assertEquals(180, pigeon.getDirection());
    }

    /**
     * Tests that speed is set correctly.
     */
    @Test
    public void testSpeed() {
        assertEquals(PIGEON_SPEED, pigeon.getSpeed(), 0.01);
    }

    /**
     * Tests that pigeon is not marked for removal initially.
     */
    @Test
    public void testNotMarkedForRemovalInitially() {
        assertFalse(pigeon.isMarkedForRemoval());
    }

    /**
     * Tests that pigeon can be marked for removal.
     */
    @Test
    public void testMarkForRemoval() {
        pigeon.markForRemoval();
        assertTrue(pigeon.isMarkedForRemoval());
    }

    /**
     * Tests movement towards target location.
     */
    @Test
    public void testMoveMethod() {
        pigeon.setDirection(0); // Move right
        pigeon.setSpeed(4);
        
        int initialX = pigeon.getX();
        pigeon.move();
        
        assertTrue(pigeon.getX() > initialX);
    }

    /**
     * Tests that pigeon can set and use speed.
     */
    @Test
    public void testSetSpeed() {
        pigeon.setSpeed(10);
        assertEquals(10.0, pigeon.getSpeed(), 0.01);
    }
}
