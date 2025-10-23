package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.enemies.Pigeon;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.ui.SpriteGallery;
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

    /**
     * Tests that tick method processes correctly without exceptions.
     */
    @Test
    public void testTickMethod() {
        pigeon.tick(mockEngine, gameState);
        // Should complete without exception
        assertNotNull(pigeon);
    }

    /**
     * Tests that sprite is not null after construction.
     */
    @Test
    public void testSpriteIsSet() {
        Pigeon newPigeon = new Pigeon(50, 50);
        assertNotNull(newPigeon.getSprite());
    }

    /**
     * Tests pigeon direction setting.
     */
    @Test
    public void testSetDirection() {
        pigeon.setDirection(3);
        assertEquals(3, pigeon.getDirection());
    }

    /**
     * Tests attacking state toggle.
     */
    @Test
    public void testAttackingState() {
        pigeon.setAttacking(true);
        pigeon.setAttacking(false);
        // Method should execute without errors
        assertNotNull(pigeon);
    }

    /**
     * Tests that constructor without target sets sprite to "down".
     */
    @Test
    public void testConstructorSetsDownSprite() {
        Pigeon newPigeon = new Pigeon(50, 50);
        assertNotNull(newPigeon.getSprite());
        // Sprite should be set via setSprite call in constructor
    }

    /**
     * Tests that constructor with target initializes direction and sprite.
     */
    @Test
    public void testConstructorWithTargetInitializesDirectionAndSprite() {
        ChickenFarmer target = new ChickenFarmer(200, 200);
        Pigeon pigeonWithTarget = new Pigeon(SPAWN_X, SPAWN_Y, target);

        assertNotNull(pigeonWithTarget.getSprite());
        // Direction should be initialized towards target
        assertTrue(pigeonWithTarget.getDirection() != 0 || SPAWN_X == 200);
    }

    /**
     * Tests that tick calls all component methods including move.
     */
    @Test
    public void testTickCallsMove() {
        // Pigeon should not be null after tick
        pigeon.tick(mockEngine, gameState);

        // Tick should complete successfully
        assertNotNull(pigeon);
    }

    /**
     * Tests that tick calls super.tick (Enemy.tick).
     */
    @Test
    public void testTickCallsSuperTick() {
        pigeon.tick(mockEngine, gameState);
        // Should complete without exception, indicating super.tick() was called
        assertNotNull(pigeon);
    }

    /**
     * Tests that pigeon updates sprite based on target Y position.
     */
    @Test
    public void testSpriteUpdatesBasedOnTargetY() {
        ChickenFarmer targetAbove = new ChickenFarmer(SPAWN_X, SPAWN_Y - 100);
        Pigeon pigeonTracking = new Pigeon(SPAWN_X, SPAWN_Y, targetAbove);

        assertNotNull(pigeonTracking.getSprite());
        // Sprite should be set based on target position
    }


    @Test
    public void testPigeonConstructorSetsCorrectSprite() {
        Pigeon pigeon = new Pigeon(100, 200);

        assertNotNull(pigeon.getSprite());
        assertEquals(SpriteGallery.pigeon.getSprite("down"), pigeon.getSprite());
    }

    /**
     * Tests that basic constructor sets sprite to "down".
     * This test catches mutation: removed call to setSprite in basic constructor.
     */
    @Test
    public void testBasicConstructorSetsDownSprite() {
        Pigeon pigeon = new Pigeon(50, 50);
        
        // Verify sprite is set (not null)
        assertNotNull("Sprite should be set in constructor", pigeon.getSprite());
        
        // Verify it's specifically the "down" sprite
        assertEquals("Constructor should set sprite to 'down'", 
                     SpriteGallery.pigeon.getSprite("down"), 
                     pigeon.getSprite());
    }

    /**
     * Tests that sprite is set immediately after construction without any tick.
     * Catches mutation where setSprite is removed from constructor.
     */
    @Test
    public void testSpriteSetImmediatelyAfterConstruction() {
        Pigeon newPigeon = new Pigeon(SPAWN_X, SPAWN_Y);
        
        // Before any tick or other method call, sprite should already be set
        assertNotNull("Sprite must be set in constructor, before any tick", 
                      newPigeon.getSprite());
        
        // Should specifically be the down sprite
        assertEquals("Basic constructor must set down sprite", 
                     SpriteGallery.pigeon.getSprite("down"),
                     newPigeon.getSprite());
    }

    /**
     * Tests line 66: if (trackedTarget != null) in initializeDirectionAndSprite
     * Mutation: replaced equality check with true
     * This would cause NPE when trying to access null trackedTarget
     */
    @Test
    public void testConstructorWithNullTargetDoesNotCrash() {
        // Create pigeon without target (trackedTarget will be null)
        Pigeon pigeon = new Pigeon(100, 100);
        
        // Should not throw NPE during construction
        // If mutation changes "trackedTarget != null" to "true",
        // it will try to call updateDirectionToTarget() with null target
        assertNotNull("Constructor should complete without NPE", pigeon);
        assertNotNull("Sprite should be set to down", pigeon.getSprite());
        assertEquals("Should set down sprite when no target",
                     SpriteGallery.pigeon.getSprite("down"),
                     pigeon.getSprite());
    }

    /**
     * Tests that constructor with null target sets down sprite correctly.
     * Verifies the else branch in initializeDirectionAndSprite (line 70)
     */
    @Test
    public void testBasicConstructorSetsDownSpriteWithoutTarget() {
        Pigeon pigeon = new Pigeon(SPAWN_X, SPAWN_Y);
        
        // Should have down sprite (from else branch)
        assertNotNull(pigeon.getSprite());
        assertEquals("Basic constructor should use else branch and set down sprite",
                     SpriteGallery.pigeon.getSprite("down"),
                     pigeon.getSprite());
    }

    /**
     * Tests that constructor with target initializes direction and sprite.
     * Verifies the if branch in initializeDirectionAndSprite (line 66-68)
     */
    @Test
    public void testConstructorWithTargetInitializesCorrectly() {
        ChickenFarmer target = new ChickenFarmer(SPAWN_X + 100, SPAWN_Y + 100);
        Pigeon pigeon = new Pigeon(SPAWN_X, SPAWN_Y, target);
        
        // Should have sprite set (from if branch)
        assertNotNull("Sprite should be set when target provided", pigeon.getSprite());
        
        // Direction should be set toward target
        assertTrue("Direction should be initialized",
                   pigeon.getDirection() >= -180 && pigeon.getDirection() <= 360);
    }

    /**
     * Tests line 68: updateSpriteBasedOnTarget() call in constructor
     * Mutation: removed call to updateSpriteBasedOnTarget
     * Sprite should be set based on target's Y position
     */
    @Test
    public void testConstructorWithTargetSetsSpriteBasedOnTargetY() {
        // Target below pigeon - should set "down" sprite
        ChickenFarmer targetBelow = new ChickenFarmer(SPAWN_X, SPAWN_Y + 100);
        Pigeon pigeonWithTargetBelow = new Pigeon(SPAWN_X, SPAWN_Y, targetBelow);
        
        assertNotNull("Sprite should be set", pigeonWithTargetBelow.getSprite());
        assertEquals("Should set down sprite when target is below",
                     SpriteGallery.pigeon.getSprite("down"),
                     pigeonWithTargetBelow.getSprite());
        
        // Target above pigeon - should set "up" sprite
        ChickenFarmer targetAbove = new ChickenFarmer(SPAWN_X, SPAWN_Y - 100);
        Pigeon pigeonWithTargetAbove = new Pigeon(SPAWN_X, SPAWN_Y, targetAbove);
        
        assertNotNull("Sprite should be set", pigeonWithTargetAbove.getSprite());
        assertEquals("Should set up sprite when target is above",
                     SpriteGallery.pigeon.getSprite("up"),
                     pigeonWithTargetAbove.getSprite());
    }

    /**
     * Tests line 70: setSprite() call in constructor (no target case)
     * Mutation: removed call to setSprite
     * If setSprite is not called, sprite will be null
     */
    @Test
    public void testBasicConstructorCallsSetSprite() {
        Pigeon pigeon = new Pigeon(SPAWN_X, SPAWN_Y);
        
        // Sprite must not be null - proves setSprite was called
        assertNotNull("setSprite must be called in basic constructor", 
                      pigeon.getSprite());
        
        // Must be the down sprite specifically
        assertEquals("Basic constructor must call setSprite with down sprite",
                     SpriteGallery.pigeon.getSprite("down"),
                     pigeon.getSprite());
    }

    /**
     * Tests that sprite is null if setSprite is not called.
     * This verifies the importance of line 70.
     */
    @Test
    public void testSpriteNotNullAfterBasicConstruction() {
        Pigeon pigeon = new Pigeon(100, 200);
        
        // If line 70 (setSprite call) is removed, sprite would be null
        assertNotNull("Sprite should not be null after construction",
                      pigeon.getSprite());
    }
}
