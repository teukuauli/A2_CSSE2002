package builder.npc.spawners;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.spawners.PigeonSpawner;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import engine.timing.TickTimer;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the PigeonSpawner class.
 * Tests spawner position management and timer functionality.
 */
public class PigeonSpawnerTest {

    private PigeonSpawner spawner;
    private static final int TEST_X = 100;
    private static final int TEST_Y = 200;
    private static final int CUSTOM_INTERVAL = 500;
    private MockEngineState mockEngine;
    private JavaBeanGameState gameState;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        spawner = new PigeonSpawner(TEST_X, TEST_Y);
        mockEngine = new MockEngineState();
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(300, 300);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
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
        PigeonSpawner customSpawner = new PigeonSpawner(50, 75, CUSTOM_INTERVAL);
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
     * Tests line 55: timer.tick is called in tick method
     * Mutation: removed call to engine/timing/TickTimer::tick
     */
    @Test
    public void testTickCallsTimerTick() {
        PigeonSpawner testSpawner = new PigeonSpawner(100, 100, 2);
        
        // Timer should not be finished initially
        assertFalse("Timer should not be finished before ticking", 
                    testSpawner.getTimer().isFinished());
        
        // Tick once
        testSpawner.tick(mockEngine, gameState);
        
        // Timer should have ticked but not finished (interval is 2)
        assertFalse("Timer should have ticked but not finished", 
                    testSpawner.getTimer().isFinished());
        
        // Tick again
        testSpawner.tick(mockEngine, gameState);
        
        // Now timer should be finished (proving tick was called)
        assertTrue("timer.tick must be called to advance timer", 
                   testSpawner.getTimer().isFinished());
    }

    /**
     * Tests line 57: timer.isFinished conditional check (false branch)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testTickConditionalCheckFalse() {
        PigeonSpawner testSpawner = new PigeonSpawner(100, 100, 10);
        
        int initialPigeonCount = gameState.getEnemies().getBirds().size();
        
        // Tick once (timer not finished)
        testSpawner.tick(mockEngine, gameState);
        
        // Should NOT spawn pigeon yet
        assertEquals("Should not spawn when timer not finished", 
                     initialPigeonCount, gameState.getEnemies().getBirds().size());
    }

    /**
     * Tests line 57: timer.isFinished conditional check (true branch)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testTickConditionalCheckTrue() {
        PigeonSpawner testSpawner = new PigeonSpawner(100, 100, 1);
        
        int initialPigeonCount = gameState.getEnemies().getBirds().size();
        
        // Tick once (timer will finish)
        testSpawner.tick(mockEngine, gameState);
        
        // Should spawn pigeon when timer finished
        assertTrue("Should spawn when timer finished", 
                   gameState.getEnemies().getBirds().size() > initialPigeonCount);
    }

    /**
     * Tests line 58: spawnPigeon is called
     * Mutation: removed call to builder/entities/npc/spawners/PigeonSpawner::spawnPigeon
     */
    @Test
    public void testTickCallsSpawnPigeon() {
        PigeonSpawner testSpawner = new PigeonSpawner(150, 250, 1);
        
        int initialPigeonCount = gameState.getEnemies().getBirds().size();
        
        // Tick to trigger spawn
        testSpawner.tick(mockEngine, gameState);
        
        // spawnPigeon must be called
        assertTrue("spawnPigeon must be called when timer finishes", 
                   gameState.getEnemies().getBirds().size() > initialPigeonCount);
    }

    /**
     * Tests line 63: setSpawnX is called in spawnPigeon
     * Mutation: removed call to builder/entities/npc/enemies/EnemyManager::setSpawnX
     */
    @Test
    public void testSpawnPigeonCallsSetSpawnX() {
        PigeonSpawner testSpawner = new PigeonSpawner(150, 250, 1);
        
        // Tick to trigger spawn
        testSpawner.tick(mockEngine, gameState);
        
        // setSpawnX must be called with spawner's X coordinate
        assertEquals("setSpawnX must be called with spawner X", 
                     150, gameState.getEnemies().getSpawnX());
    }

    /**
     * Tests line 64: setSpawnY is called in spawnPigeon
     * Mutation: removed call to builder/entities/npc/enemies/EnemyManager::setSpawnY
     */
    @Test
    public void testSpawnPigeonCallsSetSpawnY() {
        PigeonSpawner testSpawner = new PigeonSpawner(150, 250, 1);
        
        // Tick to trigger spawn
        testSpawner.tick(mockEngine, gameState);
        
        // setSpawnY must be called with spawner's Y coordinate
        assertEquals("setSpawnY must be called with spawner Y", 
                     250, gameState.getEnemies().getSpawnY());
    }

    /**
     * Tests that pigeon is spawned at correct location
     */
    @Test
    public void testPigeonSpawnedAtCorrectLocation() {
        PigeonSpawner testSpawner = new PigeonSpawner(200, 300, 1);
        
        // Tick to trigger spawn
        testSpawner.tick(mockEngine, gameState);
        
        // Pigeon should be spawned at spawner location
        assertFalse("Pigeon should be spawned", gameState.getEnemies().getBirds().isEmpty());
        assertEquals("Pigeon X should match spawner X", 
                     200, gameState.getEnemies().getBirds().get(0).getX());
        assertEquals("Pigeon Y should match spawner Y", 
                     300, gameState.getEnemies().getBirds().get(0).getY());
    }

    /**
     * Tests that spawner continues to spawn pigeons on each timer completion
     */
    @Test
    public void testSpawnerContinuesSpawning() {
        PigeonSpawner testSpawner = new PigeonSpawner(100, 100, 1);
        
        // First spawn
        testSpawner.tick(mockEngine, gameState);
        int firstCount = gameState.getEnemies().getBirds().size();
        
        // Second spawn
        testSpawner.tick(mockEngine, gameState);
        int secondCount = gameState.getEnemies().getBirds().size();
        
        // Should have spawned another pigeon
        assertTrue("Spawner should spawn multiple pigeons", secondCount > firstCount);
    }
}
