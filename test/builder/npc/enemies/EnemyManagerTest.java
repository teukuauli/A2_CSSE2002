package builder.npc.enemies;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.Eagle;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.enemies.Magpie;
import builder.entities.npc.enemies.Pigeon;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the EnemyManager class.
 * Tests enemy management including adding, removing, and ticking enemies.
 */
public class EnemyManagerTest {

    private EnemyManager enemyManager;
    private MockEngineState mockEngine;
    private JavaBeanGameState gameState;
    private ChickenFarmer player;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        mockEngine = new MockEngineState();
        enemyManager = new EnemyManager(mockEngine.getDimensions());
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemyManager);
    }

    /**
     * Tests that enemy manager initializes with empty lists.
     */
    @Test
    public void testInitiallyEmpty() {
        assertTrue(enemyManager.getBirds().isEmpty());
        assertTrue(enemyManager.getSpawners().isEmpty());
    }

    /**
     * Tests adding a pigeon enemy.
     */
    @Test
    public void testAddPigeon() {
        Pigeon pigeon = new Pigeon(100, 100);
        enemyManager.getBirds().add(pigeon);
        
        assertEquals(1, enemyManager.getBirds().size());
    }

    /**
     * Tests adding a magpie enemy.
     */
    @Test
    public void testAddMagpie() {
        Magpie magpie = new Magpie(100, 100, player);
        enemyManager.getBirds().add(magpie);
        
        assertEquals(1, enemyManager.getBirds().size());
    }

    /**
     * Tests adding an eagle enemy.
     */
    @Test
    public void testAddEagle() {
        Eagle eagle = new Eagle(100, 100, player);
        enemyManager.getBirds().add(eagle);
        
        assertEquals(1, enemyManager.getBirds().size());
    }

    /**
     * Tests cleanup removes marked enemies.
     */
    @Test
    public void testCleanupRemovesMarkedEnemies() {
        Pigeon pigeon1 = new Pigeon(100, 100);
        Pigeon pigeon2 = new Pigeon(200, 200);
        
        enemyManager.getBirds().add(pigeon1);
        enemyManager.getBirds().add(pigeon2);
        
        pigeon1.markForRemoval();
        enemyManager.tick(mockEngine, gameState);
        
        // Cleanup happens in tick
        assertEquals(1, enemyManager.getBirds().size());
    }

    /**
     * Tests getAll returns all enemies.
     */
    @Test
    public void testGetAllEnemies() {
        Pigeon pigeon = new Pigeon(100, 100);
        Magpie magpie = new Magpie(150, 150, player);
        
        enemyManager.getBirds().add(pigeon);
        enemyManager.getBirds().add(magpie);
        
        assertEquals(2, enemyManager.getAll().size());
    }

    /**
     * Tests getMagpies filters only magpies.
     */
    @Test
    public void testGetMagpiesFiltersMagpies() {
        Pigeon pigeon = new Pigeon(100, 100);
        Magpie magpie = new Magpie(150, 150, player);
        Eagle eagle = new Eagle(200, 200, player);
        
        enemyManager.getBirds().add(pigeon);
        enemyManager.getBirds().add(magpie);
        enemyManager.getBirds().add(eagle);
        
        assertEquals(1, enemyManager.getMagpies().size());
        assertTrue(enemyManager.getMagpies().get(0) instanceof Magpie);
    }

    /**
     * Tests setSpawnX and getSpawnX.
     */
    @Test
    public void testSetAndGetSpawnX() {
        enemyManager.setSpawnX(300);
        assertEquals(300, enemyManager.getSpawnX());
    }

    /**
     * Tests setSpawnY and getSpawnY.
     */
    @Test
    public void testSetAndGetSpawnY() {
        enemyManager.setSpawnY(400);
        assertEquals(400, enemyManager.getSpawnY());
    }

    /**
     * Tests render returns renderables.
     */
    @Test
    public void testRender() {
        assertNotNull(enemyManager.render());
    }

    /**
     * Tests tick on enemies.
     */
    @Test
    public void testTickEnemies() {
        Pigeon pigeon = new Pigeon(100, 100, player);
        pigeon.setDirection(0);
        enemyManager.getBirds().add(pigeon);
        
        int initialX = pigeon.getX();
        
        pigeon.move(); // Test move directly
        
        // Pigeon should have moved
        assertTrue(pigeon.getX() != initialX);
    }

    /**
     * Tests line 118: mkM returns a non-null Magpie
     * Mutation: replaced return value with null
     */
    @Test
    public void testMkMReturnsNonNull() {
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(150);
        
        Magpie magpie = enemyManager.mkM(player);
        
        assertNotNull("mkM must return a non-null Magpie", magpie);
    }

    /**
     * Tests line 118: mkM adds magpie to birds list
     * Mutation: replaced return value with null
     */
    @Test
    public void testMkMAddsMagpieToBirds() {
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(150);
        
        Magpie magpie = enemyManager.mkM(player);
        
        assertEquals("mkM must add magpie to birds list", 1, enemyManager.getBirds().size());
        assertSame("Magpie must be added to list", magpie, enemyManager.getBirds().get(0));
    }

    /**
     * Tests line 130: mkP returns a non-null Pigeon
     * Mutation: replaced return value with null
     */
    @Test
    public void testMkPReturnsNonNull() {
        enemyManager.setSpawnX(200);
        enemyManager.setSpawnY(250);
        
        Pigeon pigeon = enemyManager.mkP(player);
        
        assertNotNull("mkP must return a non-null Pigeon", pigeon);
    }

    /**
     * Tests line 130: mkP adds pigeon to birds list
     * Mutation: replaced return value with null
     */
    @Test
    public void testMkPAddsPigeonToBirds() {
        enemyManager.setSpawnX(200);
        enemyManager.setSpawnY(250);
        
        Pigeon pigeon = enemyManager.mkP(player);
        
        assertEquals("mkP must add pigeon to birds list", 1, enemyManager.getBirds().size());
        assertSame("Pigeon must be added to list", pigeon, enemyManager.getBirds().get(0));
    }

    /**
     * Tests line 141: mkE returns a non-null Eagle
     * Mutation: replaced return value with null
     */
    @Test
    public void testMkEReturnsNonNull() {
        enemyManager.setSpawnX(300);
        enemyManager.setSpawnY(350);
        
        Eagle eagle = enemyManager.mkE(player);
        
        assertNotNull("mkE must return a non-null Eagle", eagle);
    }

    /**
     * Tests line 141: mkE creates eagle with correct spawn position
     * Mutation: replaced return value with null
     */
    @Test
    public void testMkECreatesEagleAtSpawn() {
        enemyManager.setSpawnX(300);
        enemyManager.setSpawnY(350);
        
        Eagle eagle = enemyManager.mkE(player);
        
        assertEquals("Eagle must be created at spawn X", 300, eagle.getX());
        assertEquals("Eagle must be created at spawn Y", 350, eagle.getY());
    }

    /**
     * Tests line 202: render returns list with enemies
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testRenderReturnsEnemies() {
        Pigeon pigeon = new Pigeon(100, 100);
        Magpie magpie = new Magpie(150, 150, player);
        
        enemyManager.getBirds().add(pigeon);
        enemyManager.getBirds().add(magpie);
        
        assertEquals("render must return list with enemies", 2, enemyManager.render().size());
    }

    /**
     * Tests line 202: render returns actual bird instances
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testRenderReturnsBirdInstances() {
        Pigeon pigeon = new Pigeon(100, 100);
        enemyManager.getBirds().add(pigeon);
        
        assertTrue("render must contain the pigeon", enemyManager.render().contains(pigeon));
    }

    /**
     * Tests line 151: instanceof Magpie conditional check
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testTickCallsMagpieTick() {
        Magpie magpie = new Magpie(100, 100, player);
        magpie.setDirection(0);
        magpie.setSpeed(2);
        enemyManager.getBirds().add(magpie);
        
        int initialX = magpie.getX();
        enemyManager.tick(mockEngine, gameState);
        
        // Magpie.tick() should be called, which calls move()
        assertTrue("Magpie.tick must be called", magpie.getX() != initialX);
    }

    /**
     * Tests line 154: instanceof Eagle conditional check
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testTickCallsEagleTick() {
        Eagle eagle = new Eagle(100, 100, player);
        eagle.setDirection(0);
        eagle.setSpeed(2);
        enemyManager.getBirds().add(eagle);
        
        int initialX = eagle.getX();
        enemyManager.tick(mockEngine, gameState);
        
        // Eagle.tick() should be called, which calls move()
        assertTrue("Eagle.tick must be called", eagle.getX() != initialX);
    }

    /**
     * Tests line 157: instanceof Pigeon conditional check (false)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testTickCallsPigeonTick() {
        Pigeon pigeon = new Pigeon(100, 100, player);
        
        // Set a very short lifespan (1 tick)
        engine.timing.FixedTimer shortTimer = new engine.timing.FixedTimer(1);
        pigeon.setLifespan(shortTimer);
        
        enemyManager.getBirds().add(pigeon);
        
        // Pigeon should not be marked for removal yet
        assertFalse("Pigeon should not be marked before tick", 
                    pigeon.isMarkedForRemoval());
        
        // Tick once - pigeon's lifespan will finish and it will be marked
        enemyManager.tick(mockEngine, gameState);
        
        // Pigeon should now be marked for removal (proving tick was called)
        assertTrue("Pigeon.tick must be called to mark pigeon when lifespan finishes", 
                   pigeon.isMarkedForRemoval());
    }

    /**
     * Tests line 157: instanceof Pigeon conditional check (true)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testTickOnlyCallsPigeonTickForPigeons() {
        Pigeon pigeon = new Pigeon(100, 100, player);
        Magpie magpie = new Magpie(200, 200, player);
        
        // Set very short lifespans (1 tick each)
        engine.timing.FixedTimer pigeonTimer = new engine.timing.FixedTimer(1);
        engine.timing.FixedTimer magpieTimer = new engine.timing.FixedTimer(1);
        pigeon.setLifespan(pigeonTimer);
        magpie.setLifespan(magpieTimer);
        
        enemyManager.getBirds().add(pigeon);
        enemyManager.getBirds().add(magpie);
        
        // Tick once
        enemyManager.tick(mockEngine, gameState);
        
        // Both should be marked for removal (proving both were ticked)
        assertTrue("Pigeon must tick and mark for removal", pigeon.isMarkedForRemoval());
        assertTrue("Magpie must tick and mark for removal", magpie.isMarkedForRemoval());
    }

    /**
     * Tests line 148: spawner.tick is called
     * Mutation: removed call to builder/entities/npc/spawners/Spawner::tick
     */
    @Test
    public void testTickCallsSpawnerTick() {
        // Use MagpieSpawner with interval of 2
        builder.entities.npc.spawners.MagpieSpawner spawner = 
            new builder.entities.npc.spawners.MagpieSpawner(100, 100, 2);
        
        enemyManager.add(spawner);
        enemyManager.setSpawnX(100);
        enemyManager.setSpawnY(100);
        
        // Before ticking, timer should not be finished
        assertFalse("Timer should not be finished initially", 
                    spawner.getTimer().isFinished());
        
        // Tick once
        enemyManager.tick(mockEngine, gameState);
        
        // Timer should have ticked (not finished yet since interval is 2)
        assertFalse("Timer should tick but not be finished", 
                    spawner.getTimer().isFinished());
        
        // Tick again - now timer should be finished
        enemyManager.tick(mockEngine, gameState);
        
        // After 2 ticks, magpie should be spawned (verifying tick was called)
        assertTrue("Spawner.tick must be called to spawn magpie", 
                   enemyManager.getMagpies().size() > 0);
    }

    /**
     * Tests line 152: Magpie.tick method call
     * Mutation: removed call to builder/entities/npc/enemies/Magpie::tick
     */
    @Test
    public void testTickInvokesMagpieTickMethod() {
        Magpie magpie = new Magpie(100, 100, player);
        magpie.setDirection(90); // Face down
        magpie.setSpeed(3);
        enemyManager.getBirds().add(magpie);
        
        int initialY = magpie.getY();
        enemyManager.tick(mockEngine, gameState);
        
        // Verify tick was called by checking movement
        assertTrue("Magpie.tick must be invoked", magpie.getY() != initialY);
    }

    /**
     * Tests line 155: Eagle.tick method call
     * Mutation: removed call to builder/entities/npc/enemies/Eagle::tick
     */
    @Test
    public void testTickInvokesEagleTickMethod() {
        Eagle eagle = new Eagle(100, 100, player);
        eagle.setDirection(90); // Face down
        eagle.setSpeed(3);
        enemyManager.getBirds().add(eagle);
        
        int initialY = eagle.getY();
        enemyManager.tick(mockEngine, gameState);
        
        // Verify tick was called by checking movement
        assertTrue("Eagle.tick must be invoked", eagle.getY() != initialY);
    }

    /**
     * Tests line 158: Pigeon.tick method call
     * Mutation: removed call to builder/entities/npc/enemies/Pigeon::tick
     */
    @Test
    public void testTickInvokesPigeonTickMethod() {
        Pigeon pigeon = new Pigeon(100, 100, player);
        
        // Set short lifespan to verify tick
        engine.timing.FixedTimer shortTimer = new engine.timing.FixedTimer(1);
        pigeon.setLifespan(shortTimer);
        
        enemyManager.getBirds().add(pigeon);
        
        assertFalse("Pigeon should not be marked before tick",
                    pigeon.isMarkedForRemoval());
        
        // Tick once
        enemyManager.tick(mockEngine, gameState);
        
        // Verify tick was called by checking if pigeon is marked for removal
        assertTrue("Pigeon.tick must be invoked to expire lifespan",
                   pigeon.isMarkedForRemoval());
    }
}
