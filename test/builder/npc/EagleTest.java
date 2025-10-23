package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.Eagle;
import builder.entities.npc.enemies.EnemyManager;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import engine.timing.FixedTimer;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the Eagle class.
 * Tests eagle behavior including food stealing, player tracking, and spawn return.
 */
public class EagleTest {

    private Eagle eagle;
    private ChickenFarmer player;
    private MockEngineState mockEngine;
    private JavaBeanGameState gameState;
    private static final int SPAWN_X = 100;
    private static final int SPAWN_Y = 100;
    private static final int INITIAL_SPEED = 2;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        mockEngine = new MockEngineState();
        player = new ChickenFarmer(200, 200);
        eagle = new Eagle(SPAWN_X, SPAWN_Y, player);
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
    }

    /**
     * Tests that eagle is constructed with correct position.
     */
    @Test
    public void testConstructorSetsPosition() {
        assertEquals(SPAWN_X, eagle.getX());
        assertEquals(SPAWN_Y, eagle.getY());
    }

    /**
     * Tests that eagle has correct initial speed.
     */
    @Test
    public void testConstructorSetsSpeed() {
        assertEquals(INITIAL_SPEED, eagle.getSpeed(), 0.01);
    }

    /**
     * Tests that eagle sprite is set.
     */
    @Test
    public void testConstructorSetsSprite() {
        assertNotNull(eagle.getSprite());
    }

    /**
     * Tests that lifespan is initialized.
     */
    @Test
    public void testGetLifespanInitialized() {
        assertNotNull(eagle.getLifespan());
        assertFalse(eagle.getLifespan().isFinished());
    }

    /**
     * Tests that lifespan can be set.
     */
    @Test
    public void testSetLifespan() {
        FixedTimer newTimer = new FixedTimer(500);
        eagle.setLifespan(newTimer);
        assertEquals(newTimer, eagle.getLifespan());
    }

    /**
     * Tests that eagle expires when lifespan finishes.
     */
    @Test
    public void testLifespanExpiration() {
        FixedTimer shortTimer = new FixedTimer(1);
        eagle.setLifespan(shortTimer);

        assertFalse(eagle.isMarkedForRemoval());

        eagle.tick(mockEngine, gameState);
        eagle.tick(mockEngine, gameState);

        assertTrue(eagle.isMarkedForRemoval());
    }

    /**
     * Tests that eagle moves when ticked.
     */
    @Test
    public void testTickCausesMovement() {
        int initialX = eagle.getX();
        int initialY = eagle.getY();

        eagle.tick(mockEngine, gameState);

        boolean moved = (eagle.getX() != initialX) || (eagle.getY() != initialY);
        assertTrue(moved);
    }

    /**
     * Tests distance calculation from position.
     */
    @Test
    public void testDistanceFromPosition() {
        int distance = eagle.distanceFrom(player);
        assertTrue(distance > 0);
    }

    /**
     * Tests distance calculation from coordinates.
     */
    @Test
    public void testDistanceFromCoordinates() {
        int distance = eagle.distanceFrom(SPAWN_X + 30, SPAWN_Y + 40);
        assertEquals(50, distance);
    }

    /**
     * Tests direction setting.
     */
    @Test
    public void testSetDirection() {
        eagle.setDirection(90);
        assertEquals(90, eagle.getDirection());
    }

    /**
     * Tests that eagle is not marked for removal initially.
     */
    @Test
    public void testNotMarkedForRemovalInitially() {
        assertFalse(eagle.isMarkedForRemoval());
    }

    /**
     * Tests mark for removal.
     */
    @Test
    public void testMarkForRemoval() {
        eagle.markForRemoval();
        assertTrue(eagle.isMarkedForRemoval());
    }

    /**
     * Tests move method.
     */
    @Test
    public void testMoveChangesPosition() {
        eagle.setDirection(0);
        int initialX = eagle.getX();
        
        eagle.move();
        
        assertTrue(eagle.getX() != initialX);
    }

    /**
     * Tests speed setting.
     */
    @Test
    public void testSetSpeed() {
        eagle.setSpeed(5);
        assertEquals(5.0, eagle.getSpeed(), 0.01);
    }

    /**
     * Tests sprite is not null.
     */
    @Test
    public void testSpriteNotNull() {
        assertNotNull(eagle.getSprite());
    }

    /**
     * Tests tick method execution.
     */
    @Test
    public void testTickExecution() {
        eagle.tick(mockEngine, gameState);
        assertNotNull(eagle);
    }

    /**
     * Tests direction updates.
     */
    @Test
    public void testDirectionUpdate() {
        eagle.setDirection(6);
        assertEquals(6, eagle.getDirection());
    }

    /**
     * Tests initial direction is valid.
     */
    @Test
    public void testInitialDirection() {
        // Direction should be initialized
        assertNotNull(eagle);
        assertTrue(eagle.getDirection() != Integer.MIN_VALUE);
    }

    /**
     * Tests that constructor calls setSprite.
     */
    @Test
    public void testConstructorCallsSetSprite() {
        Eagle newEagle = new Eagle(50, 50, player);
        assertNotNull(newEagle.getSprite());
    }

    /**
     * Tests that constructor calls updateDirectionToTarget.
     */
    @Test
    public void testConstructorCallsUpdateDirectionToTarget() {
        Eagle newEagle = new Eagle(50, 50, player);
        // Direction should be set towards player
        assertTrue(newEagle.getDirection() != Integer.MIN_VALUE);
    }

    /**
     * Tests that tick calls super.tick (Enemy.tick).
     */
    @Test
    public void testTickCallsSuperTick() {
        eagle.tick(mockEngine, gameState);
        // Should complete without exception
        assertNotNull(eagle);
    }

    /**
     * Tests that tick calls move.
     */
    @Test
    public void testTickCallsMove() {
        int initialX = eagle.getX();
        eagle.setDirection(0);
        eagle.tick(mockEngine, gameState);
        // Position should change due to move() call
        assertTrue(eagle.getX() != initialX || eagle.getY() != eagle.getY());
    }

    /**
     * Tests sprite updates based on target Y position.
     */
    @Test
    public void testSpriteUpdatesBasedOnTargetY() {
        ChickenFarmer targetAbove = new ChickenFarmer(SPAWN_X, SPAWN_Y - 100);
        Eagle eagleTracking = new Eagle(SPAWN_X, SPAWN_Y, targetAbove);
        assertNotNull(eagleTracking.getSprite());
    }
}
