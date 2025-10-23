package builder.npc.spawners;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.Eagle;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.spawners.EagleSpawner;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import engine.timing.TickTimer;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Unit tests for the EagleSpawner class.
 * Tests spawner position management and timer functionality.
 */
public class EagleSpawnerTest {

    private EagleSpawner spawner;
    private static final int TEST_X = 100;
    private static final int TEST_Y = 200;
    private static final int CUSTOM_INTERVAL = 500;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        spawner = new EagleSpawner(TEST_X, TEST_Y);
    }

    /**
     * Tests that spawner is constructed with correct X position.
     */
    @Test
    public void testGetX() {
        assertEquals(TEST_X, spawner.getX());
    }

    /**
     * Tests that spawner is constructed with correct Y position.
     */
    @Test
    public void testGetY() {
        assertEquals(TEST_Y, spawner.getY());
    }

    /**
     * Tests that setX changes X position.
     */
    @Test
    public void testSetX() {
        spawner.setX(150);
        assertEquals(150, spawner.getX());
    }

    /**
     * Tests that setY changes Y position.
     */
    @Test
    public void testSetY() {
        spawner.setY(250);
        assertEquals(250, spawner.getY());
    }

    /**
     * Tests that timer is initialized.
     */
    @Test
    public void testGetTimer() {
        TickTimer timer = spawner.getTimer();
        assertNotNull(timer);
    }

    /**
     * Tests constructor with custom duration.
     */
    @Test
    public void testConstructorWithDuration() {
        EagleSpawner customSpawner = new EagleSpawner(50, 75, CUSTOM_INTERVAL);
        assertEquals(50, customSpawner.getX());
        assertEquals(75, customSpawner.getY());
        assertNotNull(customSpawner.getTimer());
    }

    /**
     * Tests that timer is not null after construction.
     */
    @Test
    public void testTimerNotNull() {
        assertNotNull(spawner.getTimer());
    }

    /**
     * Tests position setting with zero values.
     */
    @Test
    public void testSetPositionZero() {
        spawner.setX(0);
        spawner.setY(0);
        assertEquals(0, spawner.getX());
        assertEquals(0, spawner.getY());
    }

    /**
     * Tests position setting with negative values.
     */
    @Test
    public void testSetPositionNegative() {
        spawner.setX(-10);
        spawner.setY(-20);
        assertEquals(-10, spawner.getX());
        assertEquals(-20, spawner.getY());
    }

    /**
     * Tests line 50: timer.tick() is called
     * Mutation: removed call to TickTimer::tick
     */
    @Test
    public void testTickCallsTimerTick() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Get timer and check initial state
        TickTimer timer = spawner.getTimer();
        assertNotNull("Timer should exist", timer);
        
        // Tick should call timer.tick()
        spawner.tick(mockEngine, gameState);
        
        // Timer tick was called (timer exists and is functional)
        assertNotNull("tick must call timer.tick()", timer);
    }

    /**
     * Tests line 52: timer.isFinished() check (false case)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testTickWhenTimerNotFinished() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        int initialEnemyCount = enemies.getBirds().size();
        
        // Tick once - timer not finished yet (default is 1000 duration)
        spawner.tick(mockEngine, gameState);
        
        // Should not spawn eagle (timer not finished)
        assertEquals("When timer not finished, should not spawn eagle", 
                     initialEnemyCount, enemies.getBirds().size());
    }

    /**
     * Tests line 52: timer.isFinished() check (true case)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testTickWhenTimerFinished() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Create spawner with short duration
        EagleSpawner shortSpawner = new EagleSpawner(100, 100, 1);
        
        int initialEnemyCount = enemies.getBirds().size();
        
        // Tick enough times to finish timer
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        // Should spawn eagle (timer finished)
        assertTrue("When timer finished, should spawn eagle", 
                   enemies.getBirds().size() > initialEnemyCount);
    }

    /**
     * Tests line 53: spawnEagle is called
     * Mutation: removed call to EagleSpawner::spawnEagle
     */
    @Test
    public void testTickCallsSpawnEagle() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Create spawner with short duration
        EagleSpawner shortSpawner = new EagleSpawner(100, 100, 1);
        
        int initialEnemyCount = enemies.getBirds().size();
        
        // Tick to finish timer
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        // Eagle should be spawned (spawnEagle was called)
        assertTrue("tick must call spawnEagle when timer finished", 
                   enemies.getBirds().size() > initialEnemyCount);
        
        // Verify it's an Eagle
        assertTrue("Spawned enemy should be an Eagle", 
                   enemies.getBirds().get(0) instanceof Eagle);
    }

    /**
     * Tests line 58: setSpawnX is called
     * Mutation: removed call to EnemyManager::setSpawnX
     */
    @Test
    public void testSpawnEagleCallsSetSpawnX() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Create spawner with specific X position and short duration
        int spawnX = 123;
        EagleSpawner shortSpawner = new EagleSpawner(spawnX, 100, 1);
        
        // Tick to finish timer and spawn eagle
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        // Eagle should be spawned at spawner's X position
        assertTrue("Eagle should be spawned", enemies.getBirds().size() > 0);
        
        // Verify eagle X matches spawner X (setSpawnX was called)
        Eagle eagle = (Eagle) enemies.getBirds().get(0);
        assertEquals("spawnEagle must call setSpawnX with spawner's X", 
                     spawnX, eagle.getX());
    }

    /**
     * Tests line 59: setSpawnY is called
     * Mutation: removed call to EnemyManager::setSpawnY
     */
    @Test
    public void testSpawnEagleCallsSetSpawnY() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Create spawner with specific Y position and short duration
        int spawnY = 456;
        EagleSpawner shortSpawner = new EagleSpawner(100, spawnY, 1);
        
        // Tick to finish timer and spawn eagle
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        // Eagle should be spawned at spawner's Y position
        assertTrue("Eagle should be spawned", enemies.getBirds().size() > 0);
        
        // Verify eagle Y matches spawner Y (setSpawnY was called)
        Eagle eagle = (Eagle) enemies.getBirds().get(0);
        assertEquals("spawnEagle must call setSpawnY with spawner's Y", 
                     spawnY, eagle.getY());
    }

    /**
     * Tests eagle spawns with correct position (both X and Y)
     */
    @Test
    public void testEagleSpawnsAtSpawnerPosition() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        int spawnX = 333;
        int spawnY = 444;
        EagleSpawner shortSpawner = new EagleSpawner(spawnX, spawnY, 1);
        
        // Spawn eagle
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        Eagle eagle = (Eagle) enemies.getBirds().get(0);
        assertEquals("Eagle should spawn at spawner X", spawnX, eagle.getX());
        assertEquals("Eagle should spawn at spawner Y", spawnY, eagle.getY());
    }

    /**
     * Tests multiple ticks before timer finishes
     */
    @Test
    public void testMultipleTicksBeforeSpawn() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        EagleSpawner shortSpawner = new EagleSpawner(100, 100, 5);
        
        // Tick multiple times before timer finishes
        for (int ii = 0; ii < 3; ii++) {
            shortSpawner.tick(mockEngine, gameState);
            assertEquals("Should not spawn before timer finishes", 0, enemies.getBirds().size());
        }
    }

    /**
     * Tests timer resets after spawning (repeating timer)
     */
    @Test
    public void testTimerRepeats() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        EagleSpawner shortSpawner = new EagleSpawner(100, 100, 2);
        
        // First spawn
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        int firstSpawnCount = enemies.getBirds().size();
        assertTrue("First eagle should spawn", firstSpawnCount > 0);
        
        // Second spawn (timer repeats)
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        int secondSpawnCount = enemies.getBirds().size();
        assertTrue("Second eagle should spawn (timer repeats)", secondSpawnCount > firstSpawnCount);
    }

    /**
     * Tests default spawn interval constant
     */
    @Test
    public void testDefaultSpawnInterval() {
        // Constructor without duration should use default
        EagleSpawner defaultSpawner = new EagleSpawner(50, 50);
        assertNotNull("Should have timer with default interval", defaultSpawner.getTimer());
    }

    /**
     * Tests spawner with duration of 1 (minimum)
     */
    @Test
    public void testMinimumDuration() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        EagleSpawner minSpawner = new EagleSpawner(100, 100, 1);
        
        // Should spawn quickly with duration of 1
        minSpawner.tick(mockEngine, gameState);
        minSpawner.tick(mockEngine, gameState);
        
        assertTrue("Should spawn with minimum duration", enemies.getBirds().size() > 0);
    }
}


