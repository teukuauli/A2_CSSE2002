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
}
