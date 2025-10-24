package builder.npc.spawners;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.Scarecrow;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.npc.spawners.ScarecrowSpawner;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import engine.timing.TickTimer;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the ScarecrowSpawner class.
 * Tests spawner position management and timer functionality.
 */
public class ScarecrowSpawnerTest {

    private ScarecrowSpawner spawner;
    private static final int TEST_X = 100;
    private static final int TEST_Y = 200;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        spawner = new ScarecrowSpawner(TEST_X, TEST_Y);
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
     * Tests line 37: timer.tick() is called
     * Mutation: removed call to RepeatingTimer::tick
     */
    @Test
    public void testTickCallsTimerTick() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 0, 0); // No coins - won't spawn
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Timer should not be finished initially (duration is 300)
        assertFalse("Timer should not be finished before ticking", 
                    spawner.getTimer().isFinished());
        
        // Tick once - timer should have ticked but not finished
        spawner.tick(mockEngine, gameState);
        assertFalse("Timer should have ticked but not finished after 1 tick", 
                    spawner.getTimer().isFinished());
        
        // Tick 298 more times (299 total) - should still not be finished
        for (int i = 0; i < 298; i++) {
            spawner.tick(mockEngine, gameState);
        }
        assertFalse("Timer should not be finished after 299 ticks total",
                    spawner.getTimer().isFinished());
        
        // Tick one more time (300th tick) - should be finished
        spawner.tick(mockEngine, gameState);
        assertTrue("timer.tick must be called to advance timer to finished state", 
                   spawner.getTimer().isFinished());
    }

    /**
     * Tests line 39: game.getInventory().getCoins() >= 2 comparison (false case)
     * Mutation: removed conditional - replaced comparison check with false
     */
    @Test
    public void testTickWhenNotEnoughCoins() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 1, 10); // Only 1 coin, need 2
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'c' key
        mockEngine = mockEngine.press('c');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        int initialCoins = inventory.getCoins();
        
        spawner.tick(mockEngine, gameState);
        
        // Should not spawn (not enough coins)
        assertEquals("When coins < 2, should not spawn scarecrow", initialNpcCount, 
                     gameState.getNpcs().getNpcs().size());
        assertEquals("When coins < 2, coins should not be deducted", initialCoins, 
                     inventory.getCoins());
    }

    /**
     * Tests line 39: game.getInventory().getCoins() >= 2 comparison (true case)
     * Mutation: removed conditional - replaced comparison check with true
     */
    @Test
    public void testTickWhenEnoughCoins() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10); // 10 coins, need 2
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'c' key
        mockEngine = mockEngine.press('c');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should spawn when coins >= 2
        assertTrue("When coins >= 2 and 'c' pressed, should spawn scarecrow", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 39: state.getKeys().isDown('c') check (false case)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testTickWhenKeyNotPressed() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Do NOT press 'c' key
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should not spawn when 'c' is not pressed
        assertEquals("When 'c' key not pressed, should not spawn scarecrow", 
                     initialNpcCount, gameState.getNpcs().getNpcs().size());
    }

    /**
     * Tests line 39: state.getKeys().isDown('c') check (true case)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testTickWhenKeyPressed() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'c' key
        mockEngine = mockEngine.press('c');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should spawn when 'c' is pressed
        assertTrue("When 'c' key pressed and enough coins, should spawn scarecrow", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 40: addCoins is called
     * Mutation: removed call to Inventory::addCoins
     */
    @Test
    public void testTickCallsAddCoins() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'c' key
        mockEngine = mockEngine.press('c');
        
        int initialCoins = inventory.getCoins();
        
        spawner.tick(mockEngine, gameState);
        
        // Coins should be deducted by 2
        assertEquals("tick must call addCoins(-2) to deduct coins", 
                     initialCoins - 2, inventory.getCoins());
    }

    /**
     * Tests line 41: addNpc is called
     * Mutation: removed call to NpcManager::addNpc
     */
    @Test
    public void testTickCallsAddNpc() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'c' key
        mockEngine = mockEngine.press('c');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Scarecrow should be added
        assertEquals("tick must call addNpc to add scarecrow", 
                     initialNpcCount + 1, gameState.getNpcs().getNpcs().size());
        
        // Verify it's a Scarecrow
        assertTrue("Added NPC should be a Scarecrow", 
                   gameState.getNpcs().getNpcs().get(0) instanceof Scarecrow);
    }

    /**
     * Tests scarecrow spawns at player position
     */
    @Test
    public void testScarecrowSpawnsAtPlayerPosition() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        mockEngine = mockEngine.press('c');
        
        spawner.tick(mockEngine, gameState);
        
        // Verify scarecrow was added at player position
        assertTrue("Scarecrow should be spawned", gameState.getNpcs().getNpcs().size() > 0);
        Scarecrow scarecrow = (Scarecrow) gameState.getNpcs().getNpcs().get(0);
        assertEquals("Scarecrow should spawn at player X", player.getX(), scarecrow.getX());
        assertEquals("Scarecrow should spawn at player Y", player.getY(), scarecrow.getY());
    }

    /**
     * Tests exact coin amount required (2 coins)
     */
    @Test
    public void testExactCoinAmount() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 2, 10); // Exactly 2 coins
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        mockEngine = mockEngine.press('c');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should spawn with exactly 2 coins
        assertTrue("Should spawn with exactly 2 coins", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
        assertEquals("Coins should be 0 after spawning", 0, inventory.getCoins());
    }

    /**
     * Tests multiple spawns deduct coins correctly
     */
    @Test
    public void testMultipleSpawnsDeductCoins() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        mockEngine = mockEngine.press('c');
        
        // First spawn
        spawner.tick(mockEngine, gameState);
        assertEquals("After first spawn, coins should be 8", 8, inventory.getCoins());
        
        // Second spawn
        spawner.tick(mockEngine, gameState);
        assertEquals("After second spawn, coins should be 6", 6, inventory.getCoins());
    }

    /**
     * Tests that without key press, coins are not deducted
     */
    @Test
    public void testNoKeyPressNoDeduction() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Don't press key
        int initialCoins = inventory.getCoins();
        
        spawner.tick(mockEngine, gameState);
        
        // Coins should not be deducted
        assertEquals("Coins should not be deducted without key press", 
                     initialCoins, inventory.getCoins());
    }

    /**
     * Tests that without enough coins, NPC is not added
     */
    @Test
    public void testNotEnoughCoinsNoSpawn() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 1, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        mockEngine = mockEngine.press('c');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // No NPC should be added
        assertEquals("NPC should not be added without enough coins", 
                     initialNpcCount, gameState.getNpcs().getNpcs().size());
    }
}

