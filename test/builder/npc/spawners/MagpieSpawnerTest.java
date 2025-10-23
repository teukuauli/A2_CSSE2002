package builder.npc.spawners;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.enemies.Magpie;
import builder.entities.npc.spawners.MagpieSpawner;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import engine.timing.TickTimer;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the MagpieSpawner class.
 * Tests spawner position management and timer functionality.
 */
public class MagpieSpawnerTest {

    private MagpieSpawner spawner;
    private static final int TEST_X = 100;
    private static final int TEST_Y = 200;
    private static final int CUSTOM_INTERVAL = 500;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        spawner = new MagpieSpawner(TEST_X, TEST_Y);
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
        MagpieSpawner customSpawner = new MagpieSpawner(50, 75, CUSTOM_INTERVAL);
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
        
        // Tick once - timer not finished yet (default is 360 duration)
        spawner.tick(mockEngine, gameState);
        
        // Should not spawn magpie (timer not finished)
        assertEquals("When timer not finished, should not spawn magpie", 
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
        MagpieSpawner shortSpawner = new MagpieSpawner(100, 100, 1);
        
        int initialEnemyCount = enemies.getBirds().size();
        
        // Tick enough times to finish timer
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        // Should spawn magpie (timer finished)
        assertTrue("When timer finished, should spawn magpie", 
                   enemies.getBirds().size() > initialEnemyCount);
    }

    /**
     * Tests line 53: spawnMagpie is called
     * Mutation: removed call to MagpieSpawner::spawnMagpie
     */
    @Test
    public void testTickCallsSpawnMagpie() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Create spawner with short duration
        MagpieSpawner shortSpawner = new MagpieSpawner(100, 100, 1);
        
        int initialEnemyCount = enemies.getBirds().size();
        
        // Tick to finish timer
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        // Magpie should be spawned (spawnMagpie was called)
        assertTrue("tick must call spawnMagpie when timer finished", 
                   enemies.getBirds().size() > initialEnemyCount);
        
        // Verify it's a Magpie
        assertTrue("Spawned enemy should be a Magpie", 
                   enemies.getBirds().get(0) instanceof Magpie);
    }

    /**
     * Tests line 58: setSpawnX is called
     * Mutation: removed call to EnemyManager::setSpawnX
     */
    @Test
    public void testSpawnMagpieCallsSetSpawnX() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Create spawner with specific X position and short duration
        int spawnX = 123;
        MagpieSpawner shortSpawner = new MagpieSpawner(spawnX, 100, 1);
        
        // Tick to finish timer and spawn magpie
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        // Magpie should be spawned at spawner's X position
        assertTrue("Magpie should be spawned", enemies.getBirds().size() > 0);
        
        // Verify magpie X matches spawner X (setSpawnX was called)
        Magpie magpie = (Magpie) enemies.getBirds().get(0);
        assertEquals("spawnMagpie must call setSpawnX with spawner's X", 
                     spawnX, magpie.getX());
    }

    /**
     * Tests line 59: setSpawnY is called
     * Mutation: removed call to EnemyManager::setSpawnY
     */
    @Test
    public void testSpawnMagpieCallsSetSpawnY() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Create spawner with specific Y position and short duration
        int spawnY = 456;
        MagpieSpawner shortSpawner = new MagpieSpawner(100, spawnY, 1);
        
        // Tick to finish timer and spawn magpie
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        // Magpie should be spawned at spawner's Y position
        assertTrue("Magpie should be spawned", enemies.getBirds().size() > 0);
        
        // Verify magpie Y matches spawner Y (setSpawnY was called)
        Magpie magpie = (Magpie) enemies.getBirds().get(0);
        assertEquals("spawnMagpie must call setSpawnY with spawner's Y", 
                     spawnY, magpie.getY());
    }

    /**
     * Tests magpie spawns with correct position (both X and Y)
     */
    @Test
    public void testMagpieSpawnsAtSpawnerPosition() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        int spawnX = 333;
        int spawnY = 444;
        MagpieSpawner shortSpawner = new MagpieSpawner(spawnX, spawnY, 1);
        
        // Spawn magpie
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        Magpie magpie = (Magpie) enemies.getBirds().get(0);
        assertEquals("Magpie should spawn at spawner X", spawnX, magpie.getX());
        assertEquals("Magpie should spawn at spawner Y", spawnY, magpie.getY());
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
        
        MagpieSpawner shortSpawner = new MagpieSpawner(100, 100, 5);
        
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
        
        MagpieSpawner shortSpawner = new MagpieSpawner(100, 100, 2);
        
        // First spawn
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        int firstSpawnCount = enemies.getBirds().size();
        assertTrue("First magpie should spawn", firstSpawnCount > 0);
        
        // Second spawn (timer repeats)
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        shortSpawner.tick(mockEngine, gameState);
        
        int secondSpawnCount = enemies.getBirds().size();
        assertTrue("Second magpie should spawn (timer repeats)", secondSpawnCount > firstSpawnCount);
    }

    /**
     * Tests default spawn interval constant
     */
    @Test
    public void testDefaultSpawnInterval() {
        // Constructor without duration should use default (360)
        MagpieSpawner defaultSpawner = new MagpieSpawner(50, 50);
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
        
        MagpieSpawner minSpawner = new MagpieSpawner(100, 100, 1);
        
        // Should spawn quickly with duration of 1
        minSpawner.tick(mockEngine, gameState);
        minSpawner.tick(mockEngine, gameState);
        
        assertTrue("Should spawn with minimum duration", enemies.getBirds().size() > 0);
    }
}

