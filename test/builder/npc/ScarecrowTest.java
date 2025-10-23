package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.Scarecrow;
import builder.entities.npc.enemies.EnemyManager;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the Scarecrow class.
 * Tests scarecrow behavior including bird scaring and static positioning.
 */
public class ScarecrowTest {

    private Scarecrow scarecrow;
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
        scarecrow = new Scarecrow(X_POS, Y_POS);
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
    }

    /**
     * Tests that scarecrow is constructed with correct position.
     */
    @Test
    public void testConstructorSetsPosition() {
        assertEquals(X_POS, scarecrow.getX());
        assertEquals(Y_POS, scarecrow.getY());
    }

    /**
     * Tests that scarecrow has zero speed (stationary).
     */
    @Test
    public void testConstructorSetsSpeedToZero() {
        assertEquals(0.0, scarecrow.getSpeed(), 0.01);
    }

    /**
     * Tests that scarecrow sprite is set.
     */
    @Test
    public void testConstructorSetsSprite() {
        assertNotNull(scarecrow.getSprite());
    }

    /**
     * Tests that scarecrow doesn't move when ticked.
     */
    @Test
    public void testTickDoesNotCauseMovement() {
        int initialX = scarecrow.getX();
        int initialY = scarecrow.getY();

        scarecrow.tick(mockEngine);

        assertEquals(initialX, scarecrow.getX());
        assertEquals(initialY, scarecrow.getY());
    }

    /**
     * Tests distance calculation from position.
     */
    @Test
    public void testDistanceFromPosition() {
        ChickenFarmer player = new ChickenFarmer(X_POS + 30, Y_POS + 40);
        int distance = scarecrow.distanceFrom(player);
        assertEquals(50, distance);
    }

    /**
     * Tests distance calculation from coordinates.
     */
    @Test
    public void testDistanceFromCoordinates() {
        int distance = scarecrow.distanceFrom(X_POS + 30, Y_POS + 40);
        assertEquals(50, distance);
    }

    /**
     * Tests that scarecrow is not marked for removal initially.
     */
    @Test
    public void testNotMarkedForRemovalInitially() {
        assertFalse(scarecrow.isMarkedForRemoval());
    }

    /**
     * Tests mark for removal.
     */
    @Test
    public void testMarkForRemoval() {
        scarecrow.markForRemoval();
        assertTrue(scarecrow.isMarkedForRemoval());
    }

    /**
     * Tests interact method doesn't throw exception.
     */
    @Test
    public void testInteract() {
        // Should not throw exception
        scarecrow.interact(mockEngine, gameState);
    }

    /**
     * Tests direction setting.
     */
    @Test
    public void testSetDirection() {
        scarecrow.setDirection(90);
        assertEquals(90, scarecrow.getDirection());
    }

    /**
     * Tests speed setting.
     */
    @Test
    public void testSetSpeed() {
        scarecrow.setSpeed(5);
        assertEquals(5.0, scarecrow.getSpeed(), 0.01);
    }

    /**
     * Tests that COIN_COST is defined correctly.
     */
    @Test
    public void testCoinCostConstant() {
        assertEquals(2, Scarecrow.COIN_COST);
    }

    /**
     * Tests sprite is not null.
     */
    @Test
    public void testSpriteNotNull() {
        assertNotNull(scarecrow.getSprite());
    }
}
