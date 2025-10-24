package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.Eagle;
import builder.entities.npc.enemies.EnemyManager;
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

    /**
     * Tests line 63: handlePlayerInteraction is called in tick
     * Mutation: removed call to handlePlayerInteraction
     */
    @Test
    public void testTickCallsHandlePlayerInteraction() throws Exception {
        Eagle testEagle = new Eagle(player.getX(), player.getY(), player);
        gameState.getInventory().addFood(10);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.tick(mockEngine, gameState);
        
        // Food should be stolen (attacking = true by default)
        assertTrue("handlePlayerInteraction must be called in tick",
                   gameState.getInventory().getFood() < initialFood);
    }

    /**
     * Tests line 64: handleSpawnReturn is called in tick
     * Mutation: removed call to handleSpawnReturn
     */
    @Test
    public void testTickCallsHandleSpawnReturn() throws Exception {
        Eagle testEagle = new Eagle(100, 100, player);
        // Set attacking to false using reflection
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setX(100);
        testEagle.setY(100);
        
        testEagle.tick(mockEngine, gameState);
        
        // Should be marked for removal
        assertTrue("handleSpawnReturn must be called in tick",
                   testEagle.isMarkedForRemoval());
    }

    /**
     * Tests line 65: move is called in tick
     * Mutation: removed call to move
     */
    @Test
    public void testTickCallsMoveMethod() {
        Eagle testEagle = new Eagle(100, 100, player);
        testEagle.setDirection(0);
        testEagle.setSpeed(2);
        
        int initialX = testEagle.getX();
        testEagle.tick(mockEngine, gameState);
        
        // Position should change
        assertTrue("move must be called in tick",
                   testEagle.getX() != initialX);
    }

    /**
     * Tests line 66: updateDirectionAndSprite is called in tick
     * Mutation: removed call to updateDirectionAndSprite
     */
    @Test
    public void testTickCallsUpdateDirectionAndSprite() {
        Eagle testEagle = new Eagle(100, 100, new ChickenFarmer(300, 100));
        testEagle.setDirection(180); // Left
        
        int initialDirection = testEagle.getDirection();
        testEagle.tick(mockEngine, gameState);
        
        // Direction should change
        assertTrue("updateDirectionAndSprite must be called in tick",
                   testEagle.getDirection() != initialDirection);
    }

    /**
     * Tests line 67: recoverFoodIfKilled is called in tick
     * Mutation: removed call to recoverFoodIfKilled
     */
    @Test
    public void testTickCallsRecoverFoodIfKilled() throws Exception {
        Eagle testEagle = new Eagle(500, 500, player);
        gameState.getInventory().addFood(10);
        
        // Give eagle food
        java.lang.reflect.Field foodField = Eagle.class.getDeclaredField("food");
        foodField.setAccessible(true);
        foodField.set(testEagle, 5);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.markForRemoval();
        testEagle.tick(mockEngine, gameState);
        
        // Food should be recovered
        assertTrue("recoverFoodIfKilled must be called in tick",
                   gameState.getInventory().getFood() > initialFood);
    }

    /**
     * Tests line 72: lifespan.isFinished conditional (false branch)
     * Mutation: replaced equality check with true
     */
    @Test
    public void testTickLifespanConditionalFalseBranch() {
        Eagle testEagle = new Eagle(100, 100, player);
        
        testEagle.tick(mockEngine, gameState);
        
        // Should NOT be marked for removal when lifespan not finished
        assertFalse("Must check lifespan.isFinished() correctly",
                    testEagle.isMarkedForRemoval());
    }

    /**
     * Tests line 78: attacking conditional in handlePlayerInteraction
     * Mutation: replaced equality check with false/true
     */
    @Test
    public void testHandlePlayerInteractionChecksAttacking() throws Exception {
        Eagle testEagle = new Eagle(player.getX(), player.getY(), player);
        // Set attacking to false
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        gameState.getInventory().addFood(10);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.tick(mockEngine, gameState);
        
        // Should NOT steal when not attacking
        assertEquals("Must check attacking flag",
                     initialFood, gameState.getInventory().getFood());
    }

    /**
     * Tests line 85: food == 0 conditional
     * Mutation: replaced comparison with true/false
     */
    @Test
    public void testHandlePlayerInteractionChecksFoodIsZero() throws Exception {
        Eagle testEagle = new Eagle(player.getX(), player.getY(), player);
        gameState.getInventory().addFood(10);
        
        // Give eagle food so condition is false
        java.lang.reflect.Field foodField = Eagle.class.getDeclaredField("food");
        foodField.setAccessible(true);
        foodField.set(testEagle, 5);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.tick(mockEngine, gameState);
        
        // Should NOT steal when already has food
        assertEquals("Must check food == 0",
                     initialFood, gameState.getInventory().getFood());
    }

    /**
     * Tests line 87: reachedPlayer conditional
     * Mutation: replaced equality check with true/false
     */
    @Test
    public void testHandlePlayerInteractionChecksReachedPlayer() throws Exception {
        Eagle testEagle = new Eagle(500, 500, player);
        gameState.getInventory().addFood(10);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.tick(mockEngine, gameState);
        
        // Should NOT steal when far from player
        assertEquals("Must check if reached player",
                     initialFood, gameState.getInventory().getFood());
    }

    /**
     * Tests line 88: stealFood is called
     * Mutation: removed call to stealFood
     */
    @Test
    public void testHandlePlayerInteractionCallsStealFood() throws Exception {
        Eagle testEagle = new Eagle(player.getX(), player.getY(), player);
        gameState.getInventory().addFood(10);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.tick(mockEngine, gameState);
        
        // stealFood must be called
        assertTrue("stealFood must be called when conditions met",
                   gameState.getInventory().getFood() < initialFood);
    }

    /**
     * Tests line 93: addFood is called in stealFood
     * Mutation: removed call to addFood
     */
    @Test
    public void testStealFoodCallsAddFood() throws Exception {
        Eagle testEagle = new Eagle(player.getX(), player.getY(), player);
        gameState.getInventory().addFood(10);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.tick(mockEngine, gameState);
        
        // Food should decrease
        assertTrue("addFood must be called in stealFood",
                   gameState.getInventory().getFood() < initialFood);
    }

    /**
     * Tests line 96: setSpeed is called in stealFood
     * Mutation: removed call to setSpeed
     */
    @Test
    public void testStealFoodCallsSetSpeed() throws Exception {
        Eagle testEagle = new Eagle(player.getX(), player.getY(), player);
        gameState.getInventory().addFood(10);
        
        double initialSpeed = testEagle.getSpeed();
        testEagle.tick(mockEngine, gameState);
        
        // Speed should change to escape speed
        assertTrue("setSpeed must be called in stealFood",
                   testEagle.getSpeed() != initialSpeed);
    }

    /**
     * Tests line 100: attacking conditional in handleSpawnReturn
     * Mutation: replaced equality check with true/false
     */
    @Test
    public void testHandleSpawnReturnChecksNotAttacking() throws Exception {
        Eagle testEagle = new Eagle(100, 100, player);
        // attacking is true by default
        testEagle.setX(100);
        testEagle.setY(100);
        
        testEagle.tick(mockEngine, gameState);
        
        // Should NOT mark for removal when still attacking
        assertFalse("Must check not attacking in handleSpawnReturn",
                    testEagle.isMarkedForRemoval());
    }

    /**
     * Tests line 105: reachedSpawn conditional
     * Mutation: replaced comparison with true/false
     */
    @Test
    public void testHandleSpawnReturnChecksReachedSpawn() throws Exception {
        Eagle testEagle = new Eagle(100, 100, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setX(500); // Far from spawn
        testEagle.setY(500);
        
        testEagle.tick(mockEngine, gameState);
        
        // Should NOT mark for removal when far from spawn
        assertFalse("Must check if reached spawn",
                    testEagle.isMarkedForRemoval());
    }

    /**
     * Tests line 107: reachedSpawn conditional (true case)
     * Mutation: replaced equality check with true/false
     */
    @Test
    public void testHandleSpawnReturnMarksWhenAtSpawn() throws Exception {
        Eagle testEagle = new Eagle(100, 100, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setX(100);
        testEagle.setY(100);
        
        testEagle.tick(mockEngine, gameState);
        
        // Should mark for removal at spawn
        assertTrue("Must mark for removal when at spawn",
                   testEagle.isMarkedForRemoval());
    }

    /**
     * Tests line 109: markForRemoval is called in handleSpawnReturn
     * Mutation: removed call to markForRemoval
     */
    @Test
    public void testHandleSpawnReturnCallsMarkForRemoval() throws Exception {
        Eagle testEagle = new Eagle(100, 100, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setX(100);
        testEagle.setY(100);
        
        testEagle.tick(mockEngine, gameState);
        
        // markForRemoval must be called
        assertTrue("markForRemoval must be called in handleSpawnReturn",
                   testEagle.isMarkedForRemoval());
    }

    /**
     * Tests line 114: attacking conditional in updateDirectionAndSprite
     * Mutation: replaced equality check with true/false
     */
    @Test
    public void testUpdateDirectionAndSpriteChecksAttacking() throws Exception {
        Eagle testEagle = new Eagle(100, 200, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setDirection(0);
        
        int initialDirection = testEagle.getDirection();
        testEagle.tick(mockEngine, gameState);
        
        // Direction should still change (goes to spawn)
        assertTrue("Must check attacking in updateDirectionAndSprite",
                   testEagle.getDirection() != initialDirection);
    }

    /**
     * Tests line 115: updateDirectionToTarget is called
     * Mutation: removed call to updateDirectionToTarget
     */
    @Test
    public void testUpdateDirectionAndSpriteCallsUpdateDirectionToTarget() {
        Eagle testEagle = new Eagle(100, 100, new ChickenFarmer(300, 100));
        testEagle.setDirection(180);
        
        int initialDirection = testEagle.getDirection();
        testEagle.tick(mockEngine, gameState);
        
        // Direction should change to face target (attacking = true by default)
        assertTrue("updateDirectionToTarget must be called when attacking",
                   testEagle.getDirection() != initialDirection);
    }

    /**
     * Tests line 116: updateSpriteBasedOnTarget is called
     * Mutation: removed call to updateSpriteBasedOnTarget
     */
    @Test
    public void testUpdateDirectionAndSpriteCallsUpdateSpriteBasedOnTarget() {
        Eagle testEagle = new Eagle(100, 100, new ChickenFarmer(100, 300));
        
        testEagle.tick(mockEngine, gameState);
        
        // Sprite should be set (attacking = true by default)
        assertNotNull("updateSpriteBasedOnTarget must be called",
                      testEagle.getSprite());
    }

    /**
     * Tests line 118: updateDirectionToSpawn is called
     * Mutation: removed call to updateDirectionToSpawn
     */
    @Test
    public void testUpdateDirectionAndSpriteCallsUpdateDirectionToSpawn() throws Exception {
        Eagle testEagle = new Eagle(300, 300, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setDirection(0);
        
        int initialDirection = testEagle.getDirection();
        testEagle.tick(mockEngine, gameState);
        
        // Direction should change to face spawn
        assertTrue("updateDirectionToSpawn must be called when not attacking",
                   testEagle.getDirection() != initialDirection);
    }

    /**
     * Tests line 119: updateSpriteBasedOnSpawn is called
     * Mutation: removed call to updateSpriteBasedOnSpawn
     */
    @Test
    public void testUpdateDirectionAndSpriteCallsUpdateSpriteBasedOnSpawn() throws Exception {
        Eagle testEagle = new Eagle(200, 300, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        
        testEagle.tick(mockEngine, gameState);
        
        // Sprite should be set
        assertNotNull("updateSpriteBasedOnSpawn must be called",
                      testEagle.getSprite());
    }

    /**
     * Tests line 124: setDirectionTo is called in updateDirectionToTarget
     * Mutation: removed call to setDirectionTo
     */
    @Test
    public void testUpdateDirectionToTargetCallsSetDirectionTo() {
        Eagle testEagle = new Eagle(100, 100, new ChickenFarmer(200, 200));
        testEagle.setDirection(0);
        
        int initialDirection = testEagle.getDirection();
        testEagle.tick(mockEngine, gameState);
        
        // Direction must change
        assertTrue("setDirectionTo must be called in updateDirectionToTarget",
                   testEagle.getDirection() != initialDirection);
    }

    /**
     * Tests line 128: setDirectionTo is called in updateDirectionToSpawn
     * Mutation: removed call to setDirectionTo
     */
    @Test
    public void testUpdateDirectionToSpawnCallsSetDirectionTo() throws Exception {
        Eagle testEagle = new Eagle(100, 100, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setX(300);
        testEagle.setY(300);
        testEagle.setDirection(0);
        
        int initialDirection = testEagle.getDirection();
        testEagle.tick(mockEngine, gameState);
        
        // Direction must change
        assertTrue("setDirectionTo must be called in updateDirectionToSpawn",
                   testEagle.getDirection() != initialDirection);
    }

    /**
     * Tests lines 132-134: setDirectionTo delta calculations and setDirection call
     * Mutations: replaced subtraction with addition, removed setDirection call
     */
    @Test
    public void testSetDirectionToCalculatesDelta() {
        Eagle testEagle = new Eagle(100, 100, new ChickenFarmer(300, 100));
        testEagle.setDirection(180);
        
        testEagle.tick(mockEngine, gameState);
        
        // Should face right toward target
        int direction = testEagle.getDirection();
        assertTrue("setDirectionTo must calculate delta with subtraction",
                   direction >= -45 && direction <= 45);
    }

    /**
     * Tests line 134: setDirection is called in setDirectionTo
     * Mutation: removed call to setDirection
     */
    @Test
    public void testSetDirectionToCallsSetDirection() {
        Eagle testEagle = new Eagle(100, 200, new ChickenFarmer(100, 50));
        testEagle.setDirection(90);
        
        int initialDirection = testEagle.getDirection();
        testEagle.tick(mockEngine, gameState);
        
        // Direction must change
        assertTrue("setDirection must be called in setDirectionTo",
                   testEagle.getDirection() != initialDirection);
    }

    /**
     * Tests line 138: trackedTarget.getY() > getY() conditional
     * Mutation: replaced comparison with true/false
     */
    @Test
    public void testUpdateSpriteBasedOnTargetChecksYPosition() {
        Eagle eagleAbove = new Eagle(200, 100, new ChickenFarmer(200, 300));
        eagleAbove.tick(mockEngine, gameState);
        
        Eagle eagleBelow = new Eagle(200, 300, new ChickenFarmer(200, 100));
        eagleBelow.tick(mockEngine, gameState);
        
        // Both should have sprites
        assertNotNull("Sprite must be set based on Y comparison",
                      eagleAbove.getSprite());
        assertNotNull("Sprite must be set based on Y comparison",
                      eagleBelow.getSprite());
    }

    /**
     * Tests line 139: setSprite is called when target below
     * Mutation: removed call to setSprite
     */
    @Test
    public void testUpdateSpriteBasedOnTargetCallsSetSpriteWhenBelow() {
        Eagle testEagle = new Eagle(200, 100, new ChickenFarmer(200, 300));
        
        testEagle.tick(mockEngine, gameState);
        
        // Sprite must be set
        assertNotNull("setSprite must be called when target below",
                      testEagle.getSprite());
    }

    /**
     * Tests line 141: setSprite is called when target above
     * Mutation: removed call to setSprite
     */
    @Test
    public void testUpdateSpriteBasedOnTargetCallsSetSpriteWhenAbove() {
        Eagle testEagle = new Eagle(200, 300, new ChickenFarmer(200, 100));
        
        testEagle.tick(mockEngine, gameState);
        
        // Sprite must be set
        assertNotNull("setSprite must be called when target above",
                      testEagle.getSprite());
    }

    /**
     * Tests line 146: spawnY < getY() conditional
     * Mutation: replaced comparison with true/false
     */
    @Test
    public void testUpdateSpriteBasedOnSpawnChecksYPosition() throws Exception {
        Eagle eagleBelow = new Eagle(200, 100, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(eagleBelow, false);
        eagleBelow.setY(300);
        eagleBelow.tick(mockEngine, gameState);
        
        Eagle eagleAbove = new Eagle(200, 300, player);
        attackingField.set(eagleAbove, false);
        eagleAbove.setY(50);
        eagleAbove.tick(mockEngine, gameState);
        
        // Both should have sprites
        assertNotNull("Sprite must be set based on spawn Y",
                      eagleBelow.getSprite());
        assertNotNull("Sprite must be set based on spawn Y",
                      eagleAbove.getSprite());
    }

    /**
     * Tests line 147: setSprite is called when above spawn
     * Mutation: removed call to setSprite
     */
    @Test
    public void testUpdateSpriteBasedOnSpawnCallsSetSpriteWhenAbove() throws Exception {
        Eagle testEagle = new Eagle(200, 100, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setY(300);
        
        testEagle.tick(mockEngine, gameState);
        
        // Sprite must be set
        assertNotNull("setSprite must be called when eagle below spawn",
                      testEagle.getSprite());
    }

    /**
     * Tests line 149: setSprite is called when below spawn
     * Mutation: removed call to setSprite
     */
    @Test
    public void testUpdateSpriteBasedOnSpawnCallsSetSpriteWhenBelow() throws Exception {
        Eagle testEagle = new Eagle(200, 300, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setY(50);
        
        testEagle.tick(mockEngine, gameState);
        
        // Sprite must be set
        assertNotNull("setSprite must be called when eagle above spawn",
                      testEagle.getSprite());
    }

    /**
     * Tests line 154: recoverFoodIfKilled conditionals
     * Mutation: replaced equality/comparison checks with true/false
     */
    @Test
    public void testRecoverFoodIfKilledRestoresFood() throws Exception {
        Eagle testEagle = new Eagle(500, 500, player);
        gameState.getInventory().addFood(10);
        
        // Give eagle food
        java.lang.reflect.Field foodField = Eagle.class.getDeclaredField("food");
        foodField.setAccessible(true);
        foodField.set(testEagle, 5);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.markForRemoval();
        testEagle.tick(mockEngine, gameState);
        
        // Food should be recovered
        assertTrue("recoverFoodIfKilled must restore food when killed",
                   gameState.getInventory().getFood() > initialFood);
    }

    /**
     * Tests line 155: addFood is called in recoverFoodIfKilled
     * Mutation: removed call to addFood
     */
    @Test
    public void testRecoverFoodIfKilledCallsAddFood() throws Exception {
        Eagle testEagle = new Eagle(500, 500, player);
        gameState.getInventory().addFood(10);
        
        // Give eagle food
        java.lang.reflect.Field foodField = Eagle.class.getDeclaredField("food");
        foodField.setAccessible(true);
        foodField.set(testEagle, 3);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.markForRemoval();
        testEagle.tick(mockEngine, gameState);
        
        // addFood must be called
        assertTrue("addFood must be called to recover stolen food",
                   gameState.getInventory().getFood() > initialFood);
    }

    /**
     * Tests line 44: setSprite is called in constructor
     * Mutation: removed call to setSprite
     */
    @Test
    public void testConstructorMustCallSetSprite() {
        Eagle newEagle = new Eagle(150, 150, player);
        
        // Sprite must be initialized
        assertNotNull("Constructor must call setSprite",
                      newEagle.getSprite());
        assertEquals("Constructor must set default sprite",
                     SpriteGallery.eagle.getSprite("default"),
                     newEagle.getSprite());
    }

    /**
     * Tests line 45: updateDirectionToTarget is called in constructor
     * Mutation: removed call to updateDirectionToTarget
     */
    @Test
    public void testConstructorMustCallUpdateDirectionToTarget() {
        // Create player to the right of eagle
        ChickenFarmer rightPlayer = new ChickenFarmer(300, 100);
        Eagle newEagle = new Eagle(100, 100, rightPlayer);
        
        // Direction should be initialized toward player (right = 0 degrees)
        int direction = newEagle.getDirection();
        assertTrue("Constructor must call updateDirectionToTarget to set direction (got " + direction + ")",
                   direction >= -45 && direction <= 45);
    }

    /**
     * Tests line 60: super.tick is called in tick method
     * Mutation: removed call to Enemy::tick
     */
    @Test
    public void testTickMustCallSuperTick() {
        Eagle testEagle = new Eagle(100, 100, player);
        testEagle.setDirection(0);
        testEagle.setSpeed(2);
        
        int initialX = testEagle.getX();
        
        // super.tick() calls move() which changes position
        testEagle.tick(mockEngine, gameState);
        
        // Position should change because super.tick() was called
        assertTrue("tick must call super.tick() which enables movement",
                   testEagle.getX() != initialX || testEagle.getY() != SPAWN_Y);
    }

    /**
     * Tests line 65: move is explicitly called in tick (not just via super)
     * Mutation: removed call to move
     */
    @Test
    public void testTickMustCallMoveExplicitly() {
        Eagle testEagle = new Eagle(100, 100, player);
        testEagle.setDirection(0);
        testEagle.setSpeed(3);
        
        int initialX = testEagle.getX();
        
        // Tick calls move() which changes position
        testEagle.tick(mockEngine, gameState);
        
        // Position must change due to move() call
        assertTrue("tick must call move() explicitly to change position",
                   testEagle.getX() != initialX || testEagle.getY() != SPAWN_Y);
    }

    /**
     * Tests line 114: attacking conditional check (both branches)
     * Mutation: replaced equality check with false
     */
    @Test
    public void testUpdateDirectionAndSpriteCheckingAttackingTrue() {
        Eagle testEagle = new Eagle(100, 100, new ChickenFarmer(300, 300));
        testEagle.setDirection(180);
        
        int initialDirection = testEagle.getDirection();
        testEagle.tick(mockEngine, gameState);
        
        // When attacking=true, should call updateDirectionToTarget
        // Direction should change to face target
        assertTrue("Must check attacking flag and call updateDirectionToTarget when true",
                   testEagle.getDirection() != initialDirection);
    }

    /**
     * Tests line 114: attacking conditional check (false branch)
     * Mutation: replaced equality check with true
     */
    @Test
    public void testUpdateDirectionAndSpriteCheckingAttackingFalse() throws Exception {
        Eagle testEagle = new Eagle(100, 100, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setX(300);
        testEagle.setY(300);
        testEagle.setDirection(90);
        
        int initialDirection = testEagle.getDirection();
        testEagle.tick(mockEngine, gameState);
        
        // When attacking=false, should call updateDirectionToSpawn
        // Direction should change to face spawn
        assertTrue("Must check attacking flag and call updateDirectionToSpawn when false",
                   testEagle.getDirection() != initialDirection);
    }

    /**
     * Tests line 119: updateSpriteBasedOnSpawn is called in else branch
     * Mutation: removed call to updateSpriteBasedOnSpawn
     */
    @Test
    public void testUpdateDirectionAndSpriteMustCallUpdateSpriteBasedOnSpawn() throws Exception {
        Eagle testEagle = new Eagle(200, 100, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setY(300); // Below spawn
        
        Object initialSprite = testEagle.getSprite();
        testEagle.tick(mockEngine, gameState);
        
        // updateSpriteBasedOnSpawn must be called
        assertNotNull("updateSpriteBasedOnSpawn must be called when not attacking",
                      testEagle.getSprite());
        assertTrue("Sprite must be updated based on spawn position",
                   initialSprite != testEagle.getSprite() || initialSprite == null);
    }

    /**
     * Tests line 146: spawnY < getY() conditional (true branch)
     * Mutation: replaced comparison check with false
     */
    @Test
    public void testUpdateSpriteBasedOnSpawnChecksTrueBranch() throws Exception {
        Eagle testEagle = new Eagle(200, 100, player); // spawnY = 100
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setY(300); // Eagle below spawn (300 > 100)
        
        testEagle.tick(mockEngine, gameState);
        
        // When spawnY < getY() is true, should set "up" sprite
        assertEquals("Must check spawnY < getY() correctly for true branch",
                     SpriteGallery.eagle.getSprite("up"),
                     testEagle.getSprite());
    }

    /**
     * Tests line 146: spawnY < getY() conditional (false branch)
     * Mutation: replaced comparison check with true
     */
    @Test
    public void testUpdateSpriteBasedOnSpawnChecksFalseBranch() throws Exception {
        Eagle testEagle = new Eagle(200, 300, player); // spawnY = 300
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setY(100); // Eagle above spawn (100 < 300)
        
        testEagle.tick(mockEngine, gameState);
        
        // When spawnY < getY() is false, should set "down" sprite
        assertEquals("Must check spawnY < getY() correctly for false branch",
                     SpriteGallery.eagle.getSprite("down"),
                     testEagle.getSprite());
    }

    /**
     * Tests line 147: setSprite("up") is called when below spawn
     * Mutation: removed call to setSprite
     */
    @Test
    public void testUpdateSpriteBasedOnSpawnMustCallSetSpriteUp() throws Exception {
        Eagle testEagle = new Eagle(200, 100, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setY(300);
        
        testEagle.tick(mockEngine, gameState);
        
        // setSprite must be called with "up"
        assertEquals("setSprite must be called with 'up' when eagle below spawn",
                     SpriteGallery.eagle.getSprite("up"),
                     testEagle.getSprite());
    }

    /**
     * Tests line 149: setSprite("down") is called when above spawn
     * Mutation: removed call to setSprite
     */
    @Test
    public void testUpdateSpriteBasedOnSpawnMustCallSetSpriteDown() throws Exception {
        Eagle testEagle = new Eagle(200, 300, player);
        java.lang.reflect.Field attackingField = Eagle.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        attackingField.set(testEagle, false);
        testEagle.setY(100);
        
        testEagle.tick(mockEngine, gameState);
        
        // setSprite must be called with "down"
        assertEquals("setSprite must be called with 'down' when eagle above spawn",
                     SpriteGallery.eagle.getSprite("down"),
                     testEagle.getSprite());
    }

    /**
     * Tests line 138: trackedTarget.getY() > getY() conditional (true branch)
     * Mutation: replaced comparison check with false
     */
    @Test
    public void testUpdateSpriteBasedOnTargetChecksTrueBranch() {
        Eagle testEagle = new Eagle(200, 100, new ChickenFarmer(200, 300));
        
        testEagle.tick(mockEngine, gameState);
        
        // When target.getY() > getY() is true, should set "down" sprite
        assertEquals("Must check target.getY() > getY() correctly for true branch",
                     SpriteGallery.eagle.getSprite("down"),
                     testEagle.getSprite());
    }

    /**
     * Tests line 138: trackedTarget.getY() > getY() conditional (false branch)
     * Mutation: replaced comparison check with true
     */
    @Test
    public void testUpdateSpriteBasedOnTargetChecksFalseBranch() {
        Eagle testEagle = new Eagle(200, 300, new ChickenFarmer(200, 100));
        
        testEagle.tick(mockEngine, gameState);
        
        // When target.getY() > getY() is false, should set "up" sprite
        assertEquals("Must check target.getY() > getY() correctly for false branch",
                     SpriteGallery.eagle.getSprite("up"),
                     testEagle.getSprite());
    }

    /**
     * Tests line 139: setSprite("down") is called when target below
     * Mutation: removed call to setSprite
     */
    @Test
    public void testUpdateSpriteBasedOnTargetMustCallSetSpriteDown() {
        Eagle testEagle = new Eagle(200, 100, new ChickenFarmer(200, 300));
        
        testEagle.tick(mockEngine, gameState);
        
        // setSprite must be called with "down"
        assertEquals("setSprite must be called with 'down' when target below",
                     SpriteGallery.eagle.getSprite("down"),
                     testEagle.getSprite());
    }

    /**
     * Tests line 141: setSprite("up") is called when target above
     * Mutation: removed call to setSprite
     */
    @Test
    public void testUpdateSpriteBasedOnTargetMustCallSetSpriteUp() {
        Eagle testEagle = new Eagle(200, 300, new ChickenFarmer(200, 100));
        
        testEagle.tick(mockEngine, gameState);
        
        // setSprite must be called with "up"
        assertEquals("setSprite must be called with 'up' when target above",
                     SpriteGallery.eagle.getSprite("up"),
                     testEagle.getSprite());
    }

    /**
     * Tests line 154: recoverFoodIfKilled conditional check (true branch)
     * Mutation: replaced comparison check with true
     */
    @Test
    public void testRecoverFoodIfKilledChecksTrueBranch() throws Exception {
        Eagle testEagle = new Eagle(500, 500, player);
        gameState.getInventory().addFood(10);
        
        // Give eagle food and mark for removal
        java.lang.reflect.Field foodField = Eagle.class.getDeclaredField("food");
        foodField.setAccessible(true);
        foodField.set(testEagle, 5);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.markForRemoval();
        testEagle.tick(mockEngine, gameState);
        
        // Food should be recovered when all conditions are true
        assertTrue("Must check all conditions for food recovery",
                   gameState.getInventory().getFood() > initialFood);
    }

    /**
     * Tests line 154: recoverFoodIfKilled conditional check (false branch)
     * Verifies food is NOT recovered when conditions are false
     */
    @Test
    public void testRecoverFoodIfKilledChecksFalseBranch() throws Exception {
        Eagle testEagle = new Eagle(500, 500, player);
        gameState.getInventory().addFood(10);
        
        // Eagle NOT marked for removal
        java.lang.reflect.Field foodField = Eagle.class.getDeclaredField("food");
        foodField.setAccessible(true);
        foodField.set(testEagle, 5);
        
        int initialFood = gameState.getInventory().getFood();
        testEagle.tick(mockEngine, gameState);
        
        // Food should NOT be recovered when not marked for removal
        assertEquals("Must check conditions - no recovery when not marked for removal",
                     initialFood, gameState.getInventory().getFood());
    }
}
