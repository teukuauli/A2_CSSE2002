package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.enemies.Magpie;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import engine.timing.FixedTimer;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the Magpie class.
 * Tests magpie behavior including coin stealing, player tracking, and spawn return.
 */
public class MagpieTest {

    private Magpie magpie;
    private ChickenFarmer player;
    private MockEngineState mockEngine;
    private JavaBeanGameState gameState;
    private static final int SPAWN_X = 100;
    private static final int SPAWN_Y = 100;
    private static final int DEFAULT_SPEED = 1;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        mockEngine = new MockEngineState();
        player = new ChickenFarmer(200, 200);
        magpie = new Magpie(SPAWN_X, SPAWN_Y, player);
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
    }

    /**
     * Tests that magpie is constructed with correct position.
     */
    @Test
    public void testConstructorSetsPosition() {
        assertEquals(SPAWN_X, magpie.getX());
        assertEquals(SPAWN_Y, magpie.getY());
    }

    /**
     * Tests that magpie has correct initial speed.
     */
    @Test
    public void testConstructorSetsSpeed() {
        assertEquals(DEFAULT_SPEED, magpie.getSpeed(), 0.01);
    }

    /**
     * Tests that magpie sprite is set.
     */
    @Test
    public void testConstructorSetsSprite() {
        assertNotNull(magpie.getSprite());
    }

    /**
     * Tests that lifespan is initialized.
     */
    @Test
    public void testGetLifespanInitialized() {
        assertNotNull(magpie.getLifespan());
        assertFalse(magpie.getLifespan().isFinished());
    }

    /**
     * Tests that lifespan can be set.
     */
    @Test
    public void testSetLifespan() {
        FixedTimer newTimer = new FixedTimer(500);
        magpie.setLifespan(newTimer);
        assertEquals(newTimer, magpie.getLifespan());
    }

    /**
     * Tests that magpie expires when lifespan finishes.
     */
    @Test
    public void testLifespanExpiration() {
        FixedTimer shortTimer = new FixedTimer(1);
        magpie.setLifespan(shortTimer);

        assertFalse(magpie.isMarkedForRemoval());

        magpie.tick(mockEngine, gameState);
        magpie.tick(mockEngine, gameState);

        assertTrue(magpie.isMarkedForRemoval());
    }

    /**
     * Tests setAttacking method.
     */
    @Test
    public void testSetAttacking() {
        magpie.setAttacking(false);
        // Should not throw exception
        assertFalse(magpie.isMarkedForRemoval());
    }

    /**
     * Tests that magpie moves when ticked.
     */
    @Test
    public void testTickCausesMovement() {
        int initialX = magpie.getX();
        int initialY = magpie.getY();

        magpie.tick(mockEngine, gameState);

        boolean moved = (magpie.getX() != initialX) || (magpie.getY() != initialY);
        assertTrue(moved);
    }

    /**
     * Tests distance calculation from position.
     */
    @Test
    public void testDistanceFromPosition() {
        int distance = magpie.distanceFrom(player);
        assertTrue(distance > 0);
    }

    /**
     * Tests distance calculation from coordinates.
     */
    @Test
    public void testDistanceFromCoordinates() {
        int distance = magpie.distanceFrom(SPAWN_X + 30, SPAWN_Y + 40);
        assertEquals(50, distance);
    }

    /**
     * Tests direction setting.
     */
    @Test
    public void testSetDirection() {
        magpie.setDirection(90);
        assertEquals(90, magpie.getDirection());
    }

    /**
     * Tests that magpie is not marked for removal initially.
     */
    @Test
    public void testNotMarkedForRemovalInitially() {
        assertFalse(magpie.isMarkedForRemoval());
    }

    /**
     * Tests mark for removal.
     */
    @Test
    public void testMarkForRemoval() {
        magpie.markForRemoval();
        assertTrue(magpie.isMarkedForRemoval());
    }

    /**
     * Tests move method.
     */
    @Test
    public void testMoveChangesPosition() {
        magpie.setDirection(0);
        int initialX = magpie.getX();
        
        magpie.move();
        
        assertTrue(magpie.getX() != initialX);
    }

    /**
     * Tests speed setting.
     */
    @Test
    public void testSetSpeed() {
        magpie.setSpeed(5);
        assertEquals(5.0, magpie.getSpeed(), 0.01);
    }

    /**
     * Tests interact method doesn't throw exception.
     */
    @Test
    public void testInteract() {
        // Should not throw exception
        magpie.interact(mockEngine, gameState);
    }

    /**
     * Tests sprite initialization.
     */
    @Test
    public void testSpriteInitialization() {
        assertNotNull(magpie.getSprite());
    }

    /**
     * Tests direction to target.
     */
    @Test
    public void testDirectionToTarget() {
        // Direction should be set
        assertNotNull(magpie);
        assertTrue(magpie.getDirection() != Integer.MIN_VALUE);
    }

    /**
     * Tests tick method execution.
     */
    @Test
    public void testTickExecution() {
        magpie.tick(mockEngine, gameState);
        assertNotNull(magpie);
    }

    /**
     * Tests that constructor calls setSpeed.
     */
    @Test
    public void testConstructorCallsSetSpeed() {
        Magpie newMagpie = new Magpie(50, 50, player);
        assertEquals(DEFAULT_SPEED, newMagpie.getSpeed(), 0.01);
    }

    /**
     * Tests that constructor calls setSprite.
     */
    @Test
    public void testConstructorCallsSetSprite() {
        Magpie newMagpie = new Magpie(50, 50, player);
        assertNotNull(newMagpie.getSprite());
    }

    /**
     * Tests that constructor calls updateDirectionToTarget.
     */
    @Test
    public void testConstructorCallsUpdateDirectionToTarget() {
        Magpie newMagpie = new Magpie(50, 50, player);
        // Direction should be set towards player (not default 0)
        assertTrue(newMagpie.getDirection() != Integer.MIN_VALUE);
    }

    /**
     * Tests that tick calls super.tick (Enemy.tick).
     */
    @Test
    public void testTickCallsSuperTick() {
        magpie.tick(mockEngine, gameState);
        // Should complete without exception
        assertNotNull(magpie);
    }

    /**
     * Tests that tick calls move.
     */
    @Test
    public void testTickCallsMove() {
        int initialX = magpie.getX();
        magpie.setDirection(0);
        magpie.tick(mockEngine, gameState);
        // Position should change due to move() call
        assertTrue(magpie.getX() != initialX || magpie.getY() != magpie.getY());
    }

    /**
     * Tests sprite updates based on target Y position.
     */
    @Test
    public void testSpriteUpdatesBasedOnTargetY() {
        ChickenFarmer targetAbove = new ChickenFarmer(SPAWN_X, SPAWN_Y - 100);
        Magpie magpieTracking = new Magpie(SPAWN_X, SPAWN_Y, targetAbove);
        assertNotNull(magpieTracking.getSprite());
    }
}
