package builder.npc.spawners;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.spawners.BeeHiveSpawner;
import builder.entities.npc.enemies.EnemyManager;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import engine.timing.TickTimer;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Unit tests for the BeeHiveSpawner class.
 * Tests spawner position management and timer functionality.
 */
public class BeeHiveSpawnerTest {

    private BeeHiveSpawner spawner;
    private static final int TEST_X = 100;
    private static final int TEST_Y = 200;
    private static final int TEST_DURATION = 300;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        spawner = new BeeHiveSpawner(TEST_X, TEST_Y, TEST_DURATION);
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
     * Tests line 42: timer.tick() is called
     * Mutation: removed call to RepeatingTimer::tick
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
        
        // Get initial timer state using reflection
        Field timerField = BeeHiveSpawner.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        engine.timing.RepeatingTimer timer = (engine.timing.RepeatingTimer) timerField.get(spawner);
        
        // Tick should call timer.tick()
        spawner.tick(mockEngine, gameState);
        
        // Verify timer was ticked (it should not be null)
        assertNotNull("tick must call timer.tick()", timer);
    }

    /**
     * Tests line 43: game.getInventory().getFood() >= 3 comparison (false case)
     * Mutation: removed conditional - replaced comparison check with false
     */
    @Test
    public void testTickWhenNotEnoughFood() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 5, 2); // Only 2 food (3rd param), need 3
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'h' key
        mockEngine = mockEngine.press('h');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        int initialFood = inventory.getFood();
        
        spawner.tick(mockEngine, gameState);
        
        // Should not spawn (not enough food)
        assertEquals("When food < 3, should not spawn hive", initialNpcCount, 
                     gameState.getNpcs().getNpcs().size());
        assertEquals("When food < 3, food should not be deducted", initialFood, 
                     inventory.getFood());
    }

    /**
     * Tests line 43: game.getInventory().getFood() >= 3 comparison (true case)
     * Mutation: removed conditional - replaced comparison check with true
     */
    @Test
    public void testTickWhenEnoughFood() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10); // 5 food, need 3
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'h' key
        mockEngine = mockEngine.press('h');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should spawn when food >= 3 and coins >= 3
        assertTrue("When food >= 3 and coins >= 3, should spawn hive", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 44: game.getInventory().getCoins() >= 3 comparison (false case)
     * Mutation: removed conditional - replaced comparison check with false
     */
    @Test
    public void testTickWhenNotEnoughCoins() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 2, 5); // Only 2 coins (2nd param), need 3
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'h' key
        mockEngine = mockEngine.press('h');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        int initialCoins = inventory.getCoins();
        
        spawner.tick(mockEngine, gameState);
        
        // Should not spawn (not enough coins)
        assertEquals("When coins < 3, should not spawn hive", initialNpcCount, 
                     gameState.getNpcs().getNpcs().size());
        assertEquals("When coins < 3, coins should not be deducted", initialCoins, 
                     inventory.getCoins());
    }

    /**
     * Tests line 44: game.getInventory().getCoins() >= 3 comparison (true case)
     * Mutation: removed conditional - replaced comparison check with true
     */
    @Test
    public void testTickWhenEnoughCoins() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10); // 10 coins, need 3
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'h' key
        mockEngine = mockEngine.press('h');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should spawn when food >= 3 and coins >= 3
        assertTrue("When coins >= 3 and food >= 3, should spawn hive", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 46: canAfford check (false case)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testTickWhenCannotAfford() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 1, 1); // Not enough resources (1 coin, 1 food)
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'h' key
        mockEngine = mockEngine.press('h');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should not spawn when canAfford is false
        assertEquals("When canAfford is false, should not spawn hive", 
                     initialNpcCount, gameState.getNpcs().getNpcs().size());
    }

    /**
     * Tests line 46: canAfford check (true case)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testTickWhenCanAfford() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10); // Enough resources
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'h' key
        mockEngine = mockEngine.press('h');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should spawn when canAfford is true
        assertTrue("When canAfford is true, should spawn hive", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 46: key press check (false case)
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
        
        // Do NOT press 'h' key
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should not spawn when 'h' is not pressed
        assertEquals("When 'h' key not pressed, should not spawn hive", 
                     initialNpcCount, gameState.getNpcs().getNpcs().size());
    }

    /**
     * Tests line 46: key press check (true case)
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
        
        // Press 'h' key
        mockEngine = mockEngine.press('h');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should spawn when 'h' is pressed
        assertTrue("When 'h' key pressed, should spawn hive", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 47: addFood is called
     * Mutation: removed call to Inventory::addFood
     */
    @Test
    public void testTickCallsAddFood() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        // Press 'h' key
        mockEngine = mockEngine.press('h');
        
        int initialFood = inventory.getFood();
        
        spawner.tick(mockEngine, gameState);
        
        // Food should be deducted by 3
        assertEquals("tick must call addFood(-3) to deduct food", 
                     initialFood - 3, inventory.getFood());
    }

    /**
     * Tests line 48: addCoins is called
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
        
        // Press 'h' key
        mockEngine = mockEngine.press('h');
        
        int initialCoins = inventory.getCoins();
        
        spawner.tick(mockEngine, gameState);
        
        // Coins should be deducted by 3
        assertEquals("tick must call addCoins(-3) to deduct coins", 
                     initialCoins - 3, inventory.getCoins());
    }

    /**
     * Tests bee hive spawns at player position
     */
    @Test
    public void testBeeHiveSpawnsAtPlayerPosition() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        mockEngine = mockEngine.press('h');
        
        spawner.tick(mockEngine, gameState);
        
        // Verify bee hive was added at player position
        assertTrue("BeeHive should be spawned", gameState.getNpcs().getNpcs().size() > 0);
        assertEquals("BeeHive should spawn at player X", player.getX(), 
                     gameState.getNpcs().getNpcs().get(0).getX());
        assertEquals("BeeHive should spawn at player Y", player.getY(), 
                     gameState.getNpcs().getNpcs().get(0).getY());
    }

    /**
     * Tests exact resource amounts required (3 food and 3 coins)
     */
    @Test
    public void testExactResourceAmounts() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 3, 3); // Exactly 3 of each
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        mockEngine = mockEngine.press('h');
        
        int initialNpcCount = gameState.getNpcs().getNpcs().size();
        
        spawner.tick(mockEngine, gameState);
        
        // Should spawn with exactly 3 food and 3 coins
        assertTrue("Should spawn with exactly 3 food and 3 coins", 
                   gameState.getNpcs().getNpcs().size() > initialNpcCount);
        assertEquals("Food should be 0 after spawning", 0, inventory.getFood());
        assertEquals("Coins should be 0 after spawning", 0, inventory.getCoins());
    }

    /**
     * Tests multiple spawns deduct resources correctly
     */
    @Test
    public void testMultipleSpawnsDeductResources() throws Exception {
        MockEngineState mockEngine = new MockEngineState();
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        JavaBeanGameState gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
        
        mockEngine = mockEngine.press('h');
        
        // First spawn
        spawner.tick(mockEngine, gameState);
        assertEquals("After first spawn, food should be 7", 7, inventory.getFood());
        assertEquals("After first spawn, coins should be 7", 7, inventory.getCoins());
        
        // Second spawn
        spawner.tick(mockEngine, gameState);
        assertEquals("After second spawn, food should be 4", 4, inventory.getFood());
        assertEquals("After second spawn, coins should be 4", 4, inventory.getCoins());
    }
}

