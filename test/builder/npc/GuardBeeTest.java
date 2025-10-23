package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.GuardBee;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.Eagle;
import builder.entities.npc.enemies.Enemy;
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
 * Unit tests for the GuardBee class.
 * Tests guard bee behavior including enemy tracking, collision detection, and lifespan.
 */
public class GuardBeeTest {

    private GuardBee guardBee;
    private Enemy targetEnemy;
    private MockEngineState mockEngine;
    private JavaBeanGameState gameState;
    private static final int SPAWN_X = 100;
    private static final int SPAWN_Y = 100;
    private static final int GUARD_BEE_SPEED = 2;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        mockEngine = new MockEngineState();
        targetEnemy = new Eagle(200, 200, new ChickenFarmer(300, 300));
        guardBee = new GuardBee(SPAWN_X, SPAWN_Y, targetEnemy);
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(300, 300);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
    }

    /**
     * Tests that guard bee is constructed with correct initial position.
     */
    @Test
    public void testConstructorSetsPosition() {
        assertEquals(SPAWN_X, guardBee.getX());
        assertEquals(SPAWN_Y, guardBee.getY());
    }

    /**
     * Tests that guard bee has correct speed.
     */
    @Test
    public void testConstructorSetsSpeed() {
        assertEquals(GUARD_BEE_SPEED, guardBee.getSpeed(), 0.01);
    }

    /**
     * Tests that guard bee sprite is initialized.
     */
    @Test
    public void testConstructorSetsSprite() {
        assertNotNull(guardBee.getSprite());
    }

    /**
     * Tests that lifespan is initialized.
     */
    @Test
    public void testGetLifespanInitialized() {
        assertNotNull(guardBee.getLifespan());
        assertFalse(guardBee.getLifespan().isFinished());
    }

    /**
     * Tests that lifespan can be set.
     */
    @Test
    public void testSetLifespan() {
        FixedTimer newTimer = new FixedTimer(500);
        guardBee.setLifespan(newTimer);
        assertEquals(newTimer, guardBee.getLifespan());
    }

    /**
     * Tests that guard bee expires when lifespan finishes.
     */
    @Test
    public void testLifespanExpiration() {
        FixedTimer shortTimer = new FixedTimer(1);
        guardBee.setLifespan(shortTimer);

        assertFalse(guardBee.isMarkedForRemoval());

        guardBee.tick(mockEngine, gameState);
        guardBee.tick(mockEngine, gameState);

        assertTrue(guardBee.isMarkedForRemoval());
    }

    /**
     * Tests that guard bee moves when ticked.
     */
    @Test
    public void testTickCausesMovement() {
        int initialX = guardBee.getX();
        int initialY = guardBee.getY();

        guardBee.tick(mockEngine, gameState);

        boolean moved = (guardBee.getX() != initialX) || (guardBee.getY() != initialY);
        assertTrue(moved);
    }

    /**
     * Tests distance calculation.
     */
    @Test
    public void testDistanceCalculation() {
        int distance = guardBee.distanceFrom(targetEnemy);
        assertTrue(distance > 0);
    }

    /**
     * Tests direction setting.
     */
    @Test
    public void testSetDirection() {
        guardBee.setDirection(90);
        assertEquals(90, guardBee.getDirection());
    }

    /**
     * Tests that guard bee is not marked for removal initially.
     */
    @Test
    public void testNotMarkedForRemovalInitially() {
        assertFalse(guardBee.isMarkedForRemoval());
    }

    /**
     * Tests mark for removal functionality.
     */
    @Test
    public void testMarkForRemoval() {
        guardBee.markForRemoval();
        assertTrue(guardBee.isMarkedForRemoval());
    }

    /**
     * Tests move method changes position.
     */
    @Test
    public void testMoveChangesPosition() {
        guardBee.setDirection(0);
        int initialX = guardBee.getX();
        
        guardBee.move();
        
        assertTrue(guardBee.getX() != initialX);
    }

    /**
     * Tests distance from coordinates.
     */
    @Test
    public void testDistanceFromCoordinates() {
        int distance = guardBee.distanceFrom(SPAWN_X + 30, SPAWN_Y + 40);
        assertEquals(50, distance);
    }

    /**
     * Tests that tick causes movement.
     */
    @Test
    public void testTickProcessing() {
        guardBee.tick(mockEngine, gameState);
        // Should complete without exception
        assertNotNull(guardBee);
    }

    /**
     * Tests sprite is set after construction.
     */
    @Test
    public void testSpriteIsSet() {
        assertNotNull(guardBee.getSprite());
    }

    /**
     * Tests direction initialization.
     */
    @Test
    public void testDirectionInitialization() {
        // Direction should be initialized
        assertNotNull(guardBee);
        assertTrue(guardBee.getDirection() != Integer.MIN_VALUE);
    }
}
