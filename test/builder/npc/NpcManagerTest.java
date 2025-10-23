package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.Npc;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the NpcManager class.
 * Tests NPC management including adding, removing, and ticking NPCs.
 */
public class NpcManagerTest {

    private NpcManager npcManager;
    private MockEngineState mockEngine;
    private JavaBeanGameState gameState;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        mockEngine = new MockEngineState();
        npcManager = new NpcManager();
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        
        gameState = new JavaBeanGameState(world, player, inventory, npcManager, enemies);
    }

    /**
     * Tests that NPC manager initializes with empty list.
     */
    @Test
    public void testInitiallyEmpty() {
        assertTrue(npcManager.getNpcs().isEmpty());
    }

    /**
     * Tests adding an NPC to the manager.
     */
    @Test
    public void testAddNpc() {
        Npc npc = new Npc(100, 100);
        npcManager.addNpc(npc);
        
        assertEquals(1, npcManager.getNpcs().size());
        assertTrue(npcManager.getNpcs().contains(npc));
    }

    /**
     * Tests adding multiple NPCs.
     */
    @Test
    public void testAddMultipleNpcs() {
        Npc npc1 = new Npc(100, 100);
        Npc npc2 = new Npc(200, 200);
        
        npcManager.addNpc(npc1);
        npcManager.addNpc(npc2);
        
        assertEquals(2, npcManager.getNpcs().size());
    }

    /**
     * Tests cleanup removes marked NPCs.
     */
    @Test
    public void testCleanupRemovesMarkedNpcs() {
        Npc npc1 = new Npc(100, 100);
        Npc npc2 = new Npc(200, 200);
        
        npcManager.addNpc(npc1);
        npcManager.addNpc(npc2);
        
        npc1.markForRemoval();
        npcManager.cleanup();
        
        assertEquals(1, npcManager.getNpcs().size());
        assertFalse(npcManager.getNpcs().contains(npc1));
        assertTrue(npcManager.getNpcs().contains(npc2));
    }

    /**
     * Tests cleanup doesn't remove unmarked NPCs.
     */
    @Test
    public void testCleanupKeepsUnmarkedNpcs() {
        Npc npc = new Npc(100, 100);
        npcManager.addNpc(npc);
        
        npcManager.cleanup();
        
        assertEquals(1, npcManager.getNpcs().size());
    }

    /**
     * Tests tick method on all NPCs.
     */
    @Test
    public void testTickAllNpcs() {
        Npc npc1 = new Npc(100, 100);
        Npc npc2 = new Npc(200, 200);
        
        npc1.setDirection(0);
        npc2.setDirection(0);
        
        npcManager.addNpc(npc1);
        npcManager.addNpc(npc2);
        
        int npc1InitialX = npc1.getX();
        int npc2InitialX = npc2.getX();
        
        npcManager.tick(mockEngine, gameState);
        
        assertTrue(npc1.getX() != npc1InitialX);
        assertTrue(npc2.getX() != npc2InitialX);
    }

    /**
     * Tests render returns renderables.
     */
    @Test
    public void testRender() {
        Npc npc = new Npc(100, 100);
        npcManager.addNpc(npc);
        
        assertNotNull(npcManager.render());
    }

    /**
     * Tests interact method doesn't throw exception.
     */
    @Test
    public void testInteract() {
        npcManager.interact(mockEngine, gameState);
        // Should not throw exception
    }
}
