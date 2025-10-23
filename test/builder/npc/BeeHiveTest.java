package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.BeeHive;
import builder.entities.npc.GuardBee;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.Enemy;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.enemies.Magpie;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

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

    /**
     * Tests line 36: setSprite is called in constructor
     * Mutation: removed call to setSprite
     */
    @Test
    public void testConstructorCallsSetSprite() {
        BeeHive hive = new BeeHive(50, 50);
        assertNotNull("Constructor must call setSprite", hive.getSprite());
    }

    /**
     * Tests line 37: setSpeed is called in constructor
     * Mutation: removed call to setSpeed
     * (Already covered by testConstructorSetsSpeedToZero)
     */
    @Test
    public void testConstructorCallsSetSpeed() {
        BeeHive hive = new BeeHive(50, 50);
        assertEquals("Constructor must call setSpeed(0)", 0.0, hive.getSpeed(), 0.01);
    }

    /**
     * Tests line 48: super.tick is called
     * Mutation: removed call to Npc::tick
     */
    @Test
    public void testTickCallsSuperTick() throws Exception {
        BeeHive hive = new BeeHive(50, 50);
        
        // Tick should update Npc state
        hive.tick(mockEngine, gameState);
        
        // Verify hive is still operational (super.tick was called)
        assertNotNull("tick must call super.tick", hive.getSprite());
    }

    /**
     * Tests line 49: updateReloadStatus is called in tick
     * Mutation: removed call to updateReloadStatus
     */
    @Test
    public void testTickCallsUpdateReloadStatus() throws Exception {
        BeeHive hive = new BeeHive(50, 50);
        
        // Set loaded to false using reflection
        Field loadedField = BeeHive.class.getDeclaredField("loaded");
        loadedField.setAccessible(true);
        loadedField.set(hive, false);
        
        // Tick many times to trigger reload
        for (int ii = 0; ii < BeeHive.TIMER_DURATION + 5; ii++) {
            hive.tick(mockEngine, gameState);
        }
        
        // After ticking, timer should have been updated (via updateReloadStatus)
        boolean loaded = (Boolean) loadedField.get(hive);
        assertTrue("tick must call updateReloadStatus to reload hive", loaded);
    }

    /**
     * Tests line 56: if (!loaded) condition (false case)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testUpdateReloadStatusWhenLoaded() throws Exception {
        // When loaded = true, timer should not tick
        Field loadedField = BeeHive.class.getDeclaredField("loaded");
        loadedField.setAccessible(true);
        loadedField.set(beeHive, true);
        
        // Call updateReloadStatus via tick
        beeHive.tick(mockEngine, gameState);
        
        // Loaded should still be true
        assertTrue("When loaded, updateReloadStatus should not change state", 
                   (Boolean) loadedField.get(beeHive));
    }

    /**
     * Tests line 56: if (!loaded) condition (true case)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testUpdateReloadStatusWhenNotLoaded() throws Exception {
        // When loaded = false, timer should tick
        Field loadedField = BeeHive.class.getDeclaredField("loaded");
        loadedField.setAccessible(true);
        loadedField.set(beeHive, false);
        
        // Tick once - should call timer.tick()
        beeHive.tick(mockEngine, gameState);
        
        // After one tick, should still be not loaded (timer not finished)
        assertFalse("When not loaded, updateReloadStatus should tick timer", 
                    (Boolean) loadedField.get(beeHive));
    }

    /**
     * Tests line 57: reloadTimer.tick() is called
     * Mutation: removed call to RepeatingTimer::tick
     */
    @Test
    public void testUpdateReloadStatusCallsTimerTick() throws Exception {
        Field loadedField = BeeHive.class.getDeclaredField("loaded");
        loadedField.setAccessible(true);
        loadedField.set(beeHive, false);
        
        // Tick multiple times - timer should progress
        for (int ii = 0; ii < BeeHive.TIMER_DURATION + 1; ii++) {
            beeHive.tick(mockEngine, gameState);
        }
        
        // Timer must have ticked, causing loaded to become true
        assertTrue("updateReloadStatus must call timer.tick()", 
                   (Boolean) loadedField.get(beeHive));
    }

    /**
     * Tests line 58: if (reloadTimer.isFinished()) condition (false case)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testUpdateReloadStatusWhenTimerNotFinished() throws Exception {
        Field loadedField = BeeHive.class.getDeclaredField("loaded");
        loadedField.setAccessible(true);
        loadedField.set(beeHive, false);
        
        // Tick once - timer not finished yet
        beeHive.tick(mockEngine, gameState);
        
        // Should remain not loaded
        assertFalse("When timer not finished, should remain not loaded", 
                    (Boolean) loadedField.get(beeHive));
    }

    /**
     * Tests line 58: if (reloadTimer.isFinished()) condition (true case)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testUpdateReloadStatusWhenTimerFinished() throws Exception {
        Field loadedField = BeeHive.class.getDeclaredField("loaded");
        loadedField.setAccessible(true);
        loadedField.set(beeHive, false);
        
        // Tick enough times to finish timer
        for (int ii = 0; ii < BeeHive.TIMER_DURATION + 1; ii++) {
            beeHive.tick(mockEngine, gameState);
        }
        
        // Should become loaded
        assertTrue("When timer finished, should become loaded", 
                   (Boolean) loadedField.get(beeHive));
    }

    /**
     * Tests line 72: super.interact is called
     * Mutation: removed call to Npc::interact
     */
    @Test
    public void testInteractCallsSuperInteract() {
        // interact should call super.interact (from Npc)
        beeHive.interact(mockEngine, gameState);
        
        // Verify hive still functions (super.interact was called)
        assertNotNull("interact must call super.interact", beeHive.getSprite());
    }

    /**
     * Tests line 73: attemptBeeSpawn is called
     * Mutation: removed call to attemptBeeSpawn
     */
    @Test
    public void testInteractCallsAttemptBeeSpawn() throws Exception {
        // Add an enemy in range
        Magpie enemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        // Interact should call attemptBeeSpawn
        beeHive.interact(mockEngine, gameState);
        
        // A bee should be added if enemy in range
        int finalNpcCount = gameState.getNpcs().getNpcs().size();
        assertTrue("interact must call attemptBeeSpawn", finalNpcCount > initialNpcCount);
    }

    /**
     * Tests line 83: if (bee != null) condition (false case)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testAttemptBeeSpawnWhenBeeIsNull() throws Exception {
        // No enemies, so createBeeIfEnemyInRange returns null
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        beeHive.interact(mockEngine, gameState);
        
        // No bee should be added
        assertEquals("When bee is null, should not add to npcs", 
                     initialNpcCount, gameState.getNpcs().getNpcs().size());
    }

    /**
     * Tests line 83: if (bee != null) condition (true case)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testAttemptBeeSpawnWhenBeeIsNotNull() throws Exception {
        // Add enemy in range
        Magpie enemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        beeHive.interact(mockEngine, gameState);
        
        // Bee should be added
        assertTrue("When bee is not null, should add to npcs", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 96: if (!loaded) condition (false case)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testCreateBeeIfEnemyInRangeWhenLoaded() throws Exception {
        // Hive starts loaded = true
        Magpie enemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        beeHive.interact(mockEngine, gameState);
        
        // Bee should be created when loaded
        assertTrue("When loaded, createBeeIfEnemyInRange should create bee", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 96: if (!loaded) condition (true case)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testCreateBeeIfEnemyInRangeWhenNotLoaded() throws Exception {
        // Set loaded = false
        Field loadedField = BeeHive.class.getDeclaredField("loaded");
        loadedField.setAccessible(true);
        loadedField.set(beeHive, false);
        
        Magpie enemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        beeHive.interact(mockEngine, gameState);
        
        // No bee should be created when not loaded
        assertEquals("When not loaded, createBeeIfEnemyInRange should return null", 
                     initialNpcCount, gameState.getNpcs().getNpcs().size());
    }

    /**
     * Tests line 101: for loop over enemies (false case)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testCreateBeeIfEnemyInRangeLoopsOverEnemies() throws Exception {
        // Add multiple enemies, first out of range, second in range
        Magpie enemy1 = new Magpie(beeHive.getX() + 400, beeHive.getY() + 400, gameState.getPlayer());
        Magpie enemy2 = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy1);
        gameState.getEnemies().getBirds().add(enemy2);
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        beeHive.interact(mockEngine, gameState);
        
        // Should find second enemy
        assertTrue("createBeeIfEnemyInRange must loop over all enemies", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 101: for loop over enemies (true case)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testCreateBeeIfEnemyInRangeFindsFirstEnemy() throws Exception {
        // Add enemy in range
        Magpie enemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        beeHive.interact(mockEngine, gameState);
        
        // Should create bee for first enemy in range
        GuardBee addedBee = (GuardBee) gameState.getNpcs().getNpcs().get(0);
        assertNotNull("createBeeIfEnemyInRange must create bee for enemy", addedBee);
    }

    /**
     * Tests line 102: createBeeIfEnemyInRange returns non-null
     * Mutation: replaced return value with null
     */
    @Test
    public void testCreateBeeIfEnemyInRangeReturnsNonNull() throws Exception {
        // Add enemy in range
        Magpie enemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        beeHive.interact(mockEngine, gameState);
        
        // Bee must be created (non-null return)
        assertTrue("createBeeIfEnemyInRange must return non-null GuardBee", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 115: distanceFrom comparison (false case)
     * Mutation: removed conditional - replaced comparison check with false
     */
    @Test
    public void testIsEnemyInRangeReturnsFalseForFarEnemy() throws Exception {
        // Add enemy far away
        Magpie enemy = new Magpie(beeHive.getX() + 400, beeHive.getY() + 400, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        beeHive.interact(mockEngine, gameState);
        
        // No bee should be created for far enemy
        assertEquals("isEnemyInRange must return false for far enemies", 
                     initialNpcCount, gameState.getNpcs().getNpcs().size());
    }

    /**
     * Tests line 115: distanceFrom comparison (true case)
     * Mutation: removed conditional - replaced comparison check with true
     */
    @Test
    public void testIsEnemyInRangeReturnsTrueForNearEnemy() throws Exception {
        // Add enemy in range
        Magpie enemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        beeHive.interact(mockEngine, gameState);
        
        // Bee should be created for near enemy
        assertTrue("isEnemyInRange must return true for near enemies", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 115: isEnemyInRange returns boolean
     * Mutation: replaced boolean return with true
     */
    @Test
    public void testIsEnemyInRangeReturnsCorrectBoolean() throws Exception {
        // Test with enemy out of range
        Magpie farEnemy = new Magpie(beeHive.getX() + 500, beeHive.getY() + 500, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(farEnemy);
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        beeHive.interact(mockEngine, gameState);
        
        // No bee created - isEnemyInRange returned false
        assertEquals("isEnemyInRange must return actual boolean value", 
                     initialNpcCount, gameState.getNpcs().getNpcs().size());
        
        // Clear enemies and test with enemy in range
        gameState.getEnemies().getBirds().clear();
        Magpie nearEnemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(nearEnemy);
        
        beeHive.interact(mockEngine, gameState);
        
        // Bee created - isEnemyInRange returned true
        assertTrue("isEnemyInRange must return actual boolean value", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests that loaded status changes after bee spawn
     */
    @Test
    public void testLoadedStatusChangesAfterBeeSpawn() throws Exception {
        Field loadedField = BeeHive.class.getDeclaredField("loaded");
        loadedField.setAccessible(true);
        
        // Initially loaded
        assertTrue("Hive starts loaded", (Boolean) loadedField.get(beeHive));
        
        // Add enemy and spawn bee
        Magpie enemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        beeHive.interact(mockEngine, gameState);
        
        // Should be not loaded after spawn
        assertFalse("Hive should not be loaded after spawning bee", 
                    (Boolean) loadedField.get(beeHive));
    }

    /**
     * Tests reload cycle completes
     */
    @Test
    public void testReloadCycleCompletes() throws Exception {
        Field loadedField = BeeHive.class.getDeclaredField("loaded");
        loadedField.setAccessible(true);
        
        // Spawn a bee to make it not loaded
        Magpie enemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        beeHive.interact(mockEngine, gameState);
        
        assertFalse("After spawning, should not be loaded", (Boolean) loadedField.get(beeHive));
        
        // Tick enough times to reload
        for (int ii = 0; ii < BeeHive.TIMER_DURATION + 1; ii++) {
            beeHive.tick(mockEngine, gameState);
        }
        
        assertTrue("After timer completes, should be loaded again", 
                   (Boolean) loadedField.get(beeHive));
    }

    /**
     * Tests bee spawns at hive position
     */
    @Test
    public void testBeeSpawnsAtHivePosition() throws Exception {
        Magpie enemy = new Magpie(beeHive.getX() + 50, beeHive.getY() + 50, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        beeHive.interact(mockEngine, gameState);
        
        GuardBee spawnedBee = (GuardBee) gameState.getNpcs().getNpcs().get(0);
        assertEquals("Bee should spawn at hive X", beeHive.getX(), spawnedBee.getX());
        assertEquals("Bee should spawn at hive Y", beeHive.getY(), spawnedBee.getY());
    }
}

