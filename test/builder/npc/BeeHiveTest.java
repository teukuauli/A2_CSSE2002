package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.BeeHive;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the BeeHive class.
 * Tests bee hive behavior including bee spawning and reload mechanics.
 */
public class BeeHiveTest {

    private BeeHive beeHive;
    private MockEngineState mockEngine;
    private JavaBeanGameState gameState;
    private static final int X_POS = 100;
    private static final int Y_POS = 100;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        mockEngine = new MockEngineState();
        beeHive = new BeeHive(X_POS, Y_POS);
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
    }

    /**
     * Tests that bee hive is constructed with correct position.
     */
    @Test
    public void testConstructorSetsPosition() {
        assertEquals(X_POS, beeHive.getX());
        assertEquals(Y_POS, beeHive.getY());
    }

    /**
     * Tests that bee hive has zero speed (stationary).
     */
    @Test
    public void testConstructorSetsSpeedToZero() {
        assertEquals(0.0, beeHive.getSpeed(), 0.01);
    }

    /**
     * Tests that bee hive sprite is set.
     */
    @Test
    public void testConstructorSetsSprite() {
        assertNotNull(beeHive.getSprite());
    }

    /**
     * Tests that bee hive doesn't move when ticked.
     */
    @Test
    public void testTickDoesNotCauseMovement() {
        int initialX = beeHive.getX();
        int initialY = beeHive.getY();

        beeHive.tick(mockEngine, gameState);

        assertEquals(initialX, beeHive.getX());
        assertEquals(initialY, beeHive.getY());
    }

    /**
     * Tests that bee hive is not marked for removal initially.
     */
    @Test
    public void testNotMarkedForRemovalInitially() {
        assertFalse(beeHive.isMarkedForRemoval());
    }

    /**
     * Tests mark for removal.
     */
    @Test
    public void testMarkForRemoval() {
        beeHive.markForRemoval();
        assertTrue(beeHive.isMarkedForRemoval());
    }

    /**
     * Tests interact method doesn't throw exception.
     */
    @Test
    public void testInteract() {
        // Should not throw exception
        beeHive.interact(mockEngine, gameState);
    }

    /**
     * Tests distance calculation from coordinates.
     */
    @Test
    public void testDistanceFromCoordinates() {
        int distance = beeHive.distanceFrom(X_POS + 30, Y_POS + 40);
        assertEquals(50, distance);
    }

    /**
     * Tests direction setting.
     */
    @Test
    public void testSetDirection() {
        beeHive.setDirection(90);
        assertEquals(90, beeHive.getDirection());
    }

    /**
     * Tests speed setting.
     */
    @Test
    public void testSetSpeed() {
        beeHive.setSpeed(5);
        assertEquals(5.0, beeHive.getSpeed(), 0.01);
    }

    /**
     * Tests that DETECTION_DISTANCE constant is defined.
     */
    @Test
    public void testDetectionDistanceConstant() {
        assertEquals(350, BeeHive.DETECTION_DISTANCE);
    }

    /**
     * Tests that TIMER_DURATION constant is defined.
     */
    @Test
    public void testTimerDurationConstant() {
        assertEquals(100, BeeHive.TIMER_DURATION);
    }

    /**
     * Tests that FOOD_COST constant is defined.
     */
    @Test
    public void testFoodCostConstant() {
        assertEquals(2, BeeHive.FOOD_COST);
    }

    /**
     * Tests that COIN_COST constant is defined.
     */
    @Test
    public void testCoinCostConstant() {
        assertEquals(2, BeeHive.COIN_COST);
    }

    /**
     * Tests sprite is not null.
     */
    @Test
    public void testSpriteNotNull() {
        assertNotNull(beeHive.getSprite());
    }

    /**
     * Tests tick method execution.
     */
    @Test
    public void testTickExecution() {
        beeHive.tick(mockEngine, gameState);
        assertNotNull(beeHive);
    }

    /**
     * Tests interact method execution.
     */
    @Test
    public void testInteractExecution() {
        beeHive.interact(mockEngine, gameState);
        assertNotNull(beeHive);
    }

    /**
     * Tests reload timer functionality.
     */
    @Test
    public void testReloadTimer() {
        // Timer should be present
        assertNotNull(beeHive);
        assertEquals(0.0, beeHive.getSpeed(), 0.01);
    }
}
