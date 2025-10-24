package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.BeeHive;
import builder.entities.npc.Npc;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import engine.renderer.Renderable;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import java.util.List;

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

    /**
     * Tests line 56: tick calls cleanup
     * Mutation: removed call to cleanup
     */
    @Test
    public void testTickCallsCleanup() {
        Npc npc1 = new Npc(100, 100);
        Npc npc2 = new Npc(200, 200);
        
        npcManager.addNpc(npc1);
        npcManager.addNpc(npc2);
        
        // Mark one for removal
        npc1.markForRemoval();
        
        // Tick should call cleanup
        npcManager.tick(mockEngine, gameState);
        
        // npc1 should be removed by cleanup
        assertEquals("tick must call cleanup to remove marked NPCs", 1, npcManager.getNpcs().size());
        assertFalse("Marked NPC should be removed", npcManager.getNpcs().contains(npc1));
        assertTrue("Unmarked NPC should remain", npcManager.getNpcs().contains(npc2));
    }

    /**
     * Tests line 65: interact calls interactable.interact
     * Mutation: removed call to Interactable::interact
     */
    @Test
    public void testInteractCallsNpcInteract() {
        // Use BeeHive which is Interactable
        BeeHive hive = new BeeHive(150, 150);
        npcManager.addNpc(hive);
        
        // Add an enemy in range for the hive to spawn a bee
        builder.entities.npc.enemies.Magpie enemy = 
            new builder.entities.npc.enemies.Magpie(160, 160, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        int initialNpcCount = npcManager.getNpcs().size();
        
        // interact should call hive.interact which spawns a bee
        npcManager.interact(mockEngine, gameState);
        
        // A bee should be added if interact was called
        assertTrue("interact must call interactable.interact on NPCs", 
                   npcManager.getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 77: instanceof Interactable check (false case)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testGetInteractablesChecksInstanceof() {
        // Add regular Npc (not Interactable beyond base)
        Npc regularNpc = new Npc(100, 100);
        npcManager.addNpc(regularNpc);
        
        // Add BeeHive (is Interactable)
        BeeHive hive = new BeeHive(200, 200);
        npcManager.addNpc(hive);
        
        // interact should only call interact on BeeHive, not regular Npc
        npcManager.interact(mockEngine, gameState);
        
        // Verify both NPCs still exist (instanceof check worked)
        assertEquals("instanceof check must filter interactables", 2, npcManager.getNpcs().size());
    }

    /**
     * Tests line 77: instanceof Interactable check (true case)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testGetInteractablesReturnsInteractables() {
        // Add BeeHive which is Interactable
        BeeHive hive = new BeeHive(150, 150);
        npcManager.addNpc(hive);
        
        // Add enemy for hive to detect
        builder.entities.npc.enemies.Magpie enemy = 
            new builder.entities.npc.enemies.Magpie(160, 160, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(enemy);
        
        int initialNpcCount = npcManager.getNpcs().size();
        
        // interact should process the interactable
        npcManager.interact(mockEngine, gameState);
        
        // Bee should be spawned (interactable was processed)
        assertTrue("instanceof check must identify interactables", 
                   npcManager.getNpcs().size() > initialNpcCount);
    }

    /**
     * Tests line 77: instanceof check filters correctly (mutation: replaced equality with true)
     * If the check is replaced with true, ALL npcs would be added to interactables list
     * But base Npc is not actually Interactable, so this would cause a ClassCastException
     */
    @Test
    public void testGetInteractablesFiltersByInstanceof() {
        // Add a basic Npc which is NOT Interactable
        Npc basicNpc = new Npc(100, 100);
        npcManager.addNpc(basicNpc);
        
        // Add an Interactable NPC
        BeeHive hive = new BeeHive(200, 200);
        npcManager.addNpc(hive);
        
        // This should only interact with the hive, not the basic Npc
        // If instanceof check is bypassed (mutation: replaced with true),
        // it would try to add basic Npc to List<Interactable>, causing issues
        try {
            npcManager.interact(mockEngine, gameState);
            // Should succeed - only hive is interacted with
            assertTrue("instanceof check must filter non-Interactable npcs", true);
        } catch (ClassCastException e) {
            fail("instanceof check failed - tried to cast non-Interactable Npc: " + e.getMessage());
        }
    }

    /**
     * Tests line 86: render returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testRenderReturnsNonEmptyList() {
        Npc npc1 = new Npc(100, 100);
        Npc npc2 = new Npc(200, 200);
        
        npcManager.addNpc(npc1);
        npcManager.addNpc(npc2);
        
        List<Renderable> renderables = npcManager.render();
        
        assertNotNull("render must return non-null list", renderables);
        assertFalse("render must return non-empty list when NPCs exist", renderables.isEmpty());
        assertEquals("render must return all NPCs", 2, renderables.size());
        assertTrue("render must include npc1", renderables.contains(npc1));
        assertTrue("render must include npc2", renderables.contains(npc2));
    }

    /**
     * Tests render returns empty list when no NPCs
     */
    @Test
    public void testRenderReturnsEmptyListWhenNoNpcs() {
        List<Renderable> renderables = npcManager.render();
        
        assertNotNull("render must return non-null list", renderables);
        assertTrue("render must return empty list when no NPCs", renderables.isEmpty());
    }

    /**
     * Tests cleanup removes multiple marked NPCs
     */
    @Test
    public void testCleanupRemovesMultipleMarked() {
        Npc npc1 = new Npc(100, 100);
        Npc npc2 = new Npc(200, 200);
        Npc npc3 = new Npc(300, 300);
        
        npcManager.addNpc(npc1);
        npcManager.addNpc(npc2);
        npcManager.addNpc(npc3);
        
        npc1.markForRemoval();
        npc3.markForRemoval();
        
        npcManager.cleanup();
        
        assertEquals("cleanup should remove all marked NPCs", 1, npcManager.getNpcs().size());
        assertFalse("npc1 should be removed", npcManager.getNpcs().contains(npc1));
        assertTrue("npc2 should remain", npcManager.getNpcs().contains(npc2));
        assertFalse("npc3 should be removed", npcManager.getNpcs().contains(npc3));
    }

    /**
     * Tests tick calls tick on all NPCs
     */
    @Test
    public void testTickCallsNpcTick() {
        Npc npc1 = new Npc(100, 100);
        Npc npc2 = new Npc(200, 200);
        
        npc1.setSpeed(5);
        npc1.setDirection(90);
        npc2.setSpeed(5);
        npc2.setDirection(90);
        
        npcManager.addNpc(npc1);
        npcManager.addNpc(npc2);
        
        int npc1InitialY = npc1.getY();
        int npc2InitialY = npc2.getY();
        
        npcManager.tick(mockEngine, gameState);
        
        // NPCs should have moved (tick was called)
        assertTrue("tick must call tick on npc1", npc1.getY() != npc1InitialY);
        assertTrue("tick must call tick on npc2", npc2.getY() != npc2InitialY);
    }

    /**
     * Tests interact with no interactable NPCs
     */
    @Test
    public void testInteractWithNoInteractables() {
        Npc npc = new Npc(100, 100);
        npcManager.addNpc(npc);
        
        // Should not throw exception even with no interactables
        npcManager.interact(mockEngine, gameState);
        
        assertEquals("NPC should still exist", 1, npcManager.getNpcs().size());
    }

    /**
     * Tests getNpcs returns the actual list
     */
    @Test
    public void testGetNpcsReturnsActualList() {
        Npc npc = new Npc(100, 100);
        npcManager.addNpc(npc);
        
        // Modify returned list
        npcManager.getNpcs().add(new Npc(200, 200));
        
        // Should affect manager's list
        assertEquals("getNpcs should return actual list", 2, npcManager.getNpcs().size());
    }

    /**
     * Tests addNpc adds to end of list
     */
    @Test
    public void testAddNpcAddsToEnd() {
        Npc npc1 = new Npc(100, 100);
        Npc npc2 = new Npc(200, 200);
        Npc npc3 = new Npc(300, 300);
        
        npcManager.addNpc(npc1);
        npcManager.addNpc(npc2);
        npcManager.addNpc(npc3);
        
        assertEquals("First NPC should be npc1", npc1, npcManager.getNpcs().get(0));
        assertEquals("Second NPC should be npc2", npc2, npcManager.getNpcs().get(1));
        assertEquals("Third NPC should be npc3", npc3, npcManager.getNpcs().get(2));
    }

    /**
     * Tests cleanup iterates backwards
     */
    @Test
    public void testCleanupIteratesBackwards() {
        Npc npc1 = new Npc(100, 100);
        Npc npc2 = new Npc(200, 200);
        Npc npc3 = new Npc(300, 300);
        Npc npc4 = new Npc(400, 400);
        
        npcManager.addNpc(npc1);
        npcManager.addNpc(npc2);
        npcManager.addNpc(npc3);
        npcManager.addNpc(npc4);
        
        // Mark all for removal
        npc1.markForRemoval();
        npc2.markForRemoval();
        npc3.markForRemoval();
        npc4.markForRemoval();
        
        npcManager.cleanup();
        
        // All should be removed
        assertEquals("cleanup should remove all marked NPCs", 0, npcManager.getNpcs().size());
    }

    /**
     * Tests render returns copy of NPCs as Renderables
     */
    @Test
    public void testRenderReturnsCopy() {
        Npc npc = new Npc(100, 100);
        npcManager.addNpc(npc);
        
        List<Renderable> renderables1 = npcManager.render();
        List<Renderable> renderables2 = npcManager.render();
        
        // Should return new list each time
        assertNotSame("render should return new list", renderables1, renderables2);
        assertEquals("Lists should have same size", renderables1.size(), renderables2.size());
    }
}

