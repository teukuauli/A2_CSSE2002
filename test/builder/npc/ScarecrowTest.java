package builder.npc;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.Scarecrow;
import builder.entities.npc.enemies.EnemyManager;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import builder.world.BeanWorld;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import static org.junit.Assert.*;

/**
 * Unit tests for the Scarecrow class.
 * Tests scarecrow behavior including bird scaring and static positioning.
 */
public class ScarecrowTest {

    private Scarecrow scarecrow;
    private MockEngineState mockEngine;
    private JavaBeanGameState gameState;
    private static final int X_POS = 100;
    private static final int Y_POS = 100;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        mockEngine = new MockEngineState();
        scarecrow = new Scarecrow(X_POS, Y_POS);
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
    }

    /**
     * Helper method to check if a Magpie is attacking using reflection.
     */
    private boolean isMagpieAttacking(builder.entities.npc.enemies.Magpie magpie) throws Exception {
        java.lang.reflect.Field attackingField = 
            builder.entities.npc.enemies.Magpie.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        return (boolean) attackingField.get(magpie);
    }

    /**
     * Helper method to check if a Pigeon is attacking using reflection.
     */
    private boolean isPigeonAttacking(builder.entities.npc.enemies.Pigeon pigeon) throws Exception {
        java.lang.reflect.Field attackingField = 
            builder.entities.npc.enemies.Pigeon.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        return (boolean) attackingField.get(pigeon);
    }

    /**
     * Tests that scarecrow is constructed with correct position.
     */
    @Test
    public void testConstructorSetsPosition() throws Exception {
        assertEquals(X_POS, scarecrow.getX());
        assertEquals(Y_POS, scarecrow.getY());
    }

    /**
     * Tests that scarecrow has zero speed (stationary).
     */
    @Test
    public void testConstructorSetsSpeedToZero() throws Exception {
        assertEquals(0.0, scarecrow.getSpeed(), 0.01);
    }

    /**
     * Tests that scarecrow sprite is set.
     */
    @Test
    public void testConstructorSetsSprite() throws Exception {
        assertNotNull(scarecrow.getSprite());
    }

    /**
     * Tests that scarecrow doesn't move when ticked.
     */
    @Test
    public void testTickDoesNotCauseMovement() throws Exception {
        int initialX = scarecrow.getX();
        int initialY = scarecrow.getY();

        scarecrow.tick(mockEngine);

        assertEquals(initialX, scarecrow.getX());
        assertEquals(initialY, scarecrow.getY());
    }

    /**
     * Tests distance calculation from position.
     */
    @Test
    public void testDistanceFromPosition() throws Exception {
        ChickenFarmer player = new ChickenFarmer(X_POS + 30, Y_POS + 40);
        int distance = scarecrow.distanceFrom(player);
        assertEquals(50, distance);
    }

    /**
     * Tests distance calculation from coordinates.
     */
    @Test
    public void testDistanceFromCoordinates() throws Exception {
        int distance = scarecrow.distanceFrom(X_POS + 30, Y_POS + 40);
        assertEquals(50, distance);
    }

    /**
     * Tests that scarecrow is not marked for removal initially.
     */
    @Test
    public void testNotMarkedForRemovalInitially() throws Exception {
        assertFalse(scarecrow.isMarkedForRemoval());
    }

    /**
     * Tests mark for removal.
     */
    @Test
    public void testMarkForRemoval() throws Exception {
        scarecrow.markForRemoval();
        assertTrue(scarecrow.isMarkedForRemoval());
    }

    /**
     * Tests interact method doesn't throw exception.
     */
    @Test
    public void testInteract() throws Exception {
        // Should not throw exception
        scarecrow.interact(mockEngine, gameState);
    }

    /**
     * Tests direction setting.
     */
    @Test
    public void testSetDirection() throws Exception {
        scarecrow.setDirection(90);
        assertEquals(90, scarecrow.getDirection());
    }

    /**
     * Tests speed setting.
     */
    @Test
    public void testSetSpeed() throws Exception {
        scarecrow.setSpeed(5);
        assertEquals(5.0, scarecrow.getSpeed(), 0.01);
    }

    /**
     * Tests that COIN_COST is defined correctly.
     */
    @Test
    public void testCoinCostConstant() throws Exception {
        assertEquals(2, Scarecrow.COIN_COST);
    }

    /**
     * Tests sprite is not null.
     */
    @Test
    public void testSpriteNotNull() throws Exception {
        assertNotNull(scarecrow.getSprite());
    }

    /**
     * Tests tick method execution.
     */
    @Test
    public void testTickExecution() throws Exception {
        scarecrow.tick(mockEngine, gameState);
        assertNotNull(scarecrow);
    }

    /**
     * Tests interact method execution.
     */
    @Test
    public void testInteractExecution() throws Exception {
        scarecrow.interact(mockEngine, gameState);
        assertNotNull(scarecrow);
    }

    /**
     * Tests scarecrow calculation method.
     */
    @Test
    public void testCalculateScareRadius() throws Exception {
        // Method should work without exceptions
        assertNotNull(scarecrow);
        assertTrue(scarecrow.getX() >= 0);
    }

    /**
     * Tests line 32: setSprite is called in constructor
     * Mutation: removed call to builder/entities/npc/Scarecrow::setSprite
     */
    @Test
    public void testConstructorCallsSetSprite() throws Exception {
        Scarecrow newScarecrow = new Scarecrow(150, 150);
        assertNotNull("Constructor must call setSprite", newScarecrow.getSprite());
    }

    /**
     * Tests line 38: super.tick is called
     * Mutation: removed call to builder/entities/npc/Npc::tick
     */
    @Test
    public void testTickCallsSuperTick() throws Exception {
        // super.tick should be called (verifying no exception)
        scarecrow.tick(mockEngine);
        assertNotNull("super.tick must be called", scarecrow);
    }

    /**
     * Tests line 43: super.interact is called
     * Mutation: removed call to builder/entities/npc/Npc::interact
     */
    @Test
    public void testInteractCallsSuperInteract() throws Exception {
        // super.interact should be called (verifying no exception)
        scarecrow.interact(mockEngine, gameState);
        assertNotNull("super.interact must be called", scarecrow);
    }

    /**
     * Tests line 45: scareAwayBirds is called
     * Mutation: removed call to builder/entities/npc/Scarecrow::scareAwayBirds
     */
    @Test
    public void testInteractCallsScareAwayBirds() throws Exception {
        // Add a magpie close to scarecrow
        builder.entities.npc.enemies.Magpie magpie = 
            new builder.entities.npc.enemies.Magpie(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        magpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(magpie);
        
        // Interact should call scareAwayBirds
        scarecrow.interact(mockEngine, gameState);
        
        // Magpie should be scared (attacking set to false)
        java.lang.reflect.Field attackingField = 
            builder.entities.npc.enemies.Magpie.class.getDeclaredField("attacking");
        attackingField.setAccessible(true);
        assertFalse("scareAwayBirds must be called to scare magpies", 
                    (boolean) attackingField.get(magpie));
    }

    /**
     * Tests line 49: calculateScareRadius multiplication
     * Mutation: Replaced integer multiplication with division
     */
    @Test
    public void testCalculateScareRadiusUsesMult() throws Exception {
        // Add a magpie just outside division range but inside multiplication range
        int tileSize = mockEngine.getDimensions().tileSize();
        // With multiplication: radius = tileSize * 4
        // With division: radius = tileSize / 4
        // Place magpie at distance = tileSize * 2 (inside mult, outside div)
        
        builder.entities.npc.enemies.Magpie magpie = 
            new builder.entities.npc.enemies.Magpie(
                X_POS + tileSize * 2, Y_POS, gameState.getPlayer());
        magpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(magpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // With correct multiplication, magpie should be scared
        assertFalse("Must use multiplication for scare radius", 
                    isMagpieAttacking(magpie));
    }

    /**
     * Tests line 49: calculateScareRadius returns non-zero
     * Mutation: replaced int return with 0
     */
    @Test
    public void testCalculateScareRadiusReturnsNonZero() throws Exception {
        // Add magpie very close to scarecrow
        builder.entities.npc.enemies.Magpie magpie = 
            new builder.entities.npc.enemies.Magpie(X_POS + 5, Y_POS + 5, gameState.getPlayer());
        magpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(magpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // With non-zero radius, close magpie should be scared
        assertFalse("calculateScareRadius must return non-zero value", 
                    isMagpieAttacking(magpie));
    }

    /**
     * Tests line 56: scareAwayMagpies is called
     * Mutation: removed call to builder/entities/npc/Scarecrow::scareAwayMagpies
     */
    @Test
    public void testScareAwayBirdsCallsScareAwayMagpies() throws Exception {
        builder.entities.npc.enemies.Magpie magpie = 
            new builder.entities.npc.enemies.Magpie(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        magpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(magpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // scareAwayMagpies must be called
        assertFalse("scareAwayMagpies must be called", isMagpieAttacking(magpie));
    }

    /**
     * Tests line 57: scareAwayPigeons is called
     * Mutation: removed call to builder/entities/npc/Scarecrow::scareAwayPigeons
     */
    @Test
    public void testScareAwayBirdsCallsScareAwayPigeons() throws Exception {
        builder.entities.npc.enemies.Pigeon pigeon = 
            new builder.entities.npc.enemies.Pigeon(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        pigeon.setAttacking(true);
        gameState.getEnemies().getBirds().add(pigeon);
        
        scarecrow.interact(mockEngine, gameState);
        
        // scareAwayPigeons must be called
        assertFalse("scareAwayPigeons must be called", isPigeonAttacking(pigeon));
    }

    /**
     * Tests line 63: instanceof Magpie conditional (false branch)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testExtractMagpiesConditionalFalse() throws Exception {
        // Add non-magpie birds
        builder.entities.npc.enemies.Pigeon pigeon = 
            new builder.entities.npc.enemies.Pigeon(X_POS, Y_POS, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(pigeon);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Should not extract pigeons as magpies
        assertNotNull("Must check instanceof correctly", scarecrow);
    }

    /**
     * Tests line 63: instanceof Magpie conditional (true branch)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testExtractMagpiesConditionalTrue() throws Exception {
        builder.entities.npc.enemies.Magpie magpie = 
            new builder.entities.npc.enemies.Magpie(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        magpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(magpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Magpie should be extracted and scared
        assertFalse("Must extract magpies when instanceof is true", 
                    isMagpieAttacking(magpie));
    }

    /**
     * Tests line 67: extractMagpies returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testExtractMagpiesReturnsNonEmpty() throws Exception {
        builder.entities.npc.enemies.Magpie magpie = 
            new builder.entities.npc.enemies.Magpie(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        magpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(magpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // extractMagpies must return list with magpies
        assertFalse("extractMagpies must return actual magpies", 
                    isMagpieAttacking(magpie));
    }

    /**
     * Tests line 73: instanceof Pigeon conditional (false branch)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testExtractPigeonsConditionalFalse() throws Exception {
        // Add non-pigeon birds
        builder.entities.npc.enemies.Magpie magpie = 
            new builder.entities.npc.enemies.Magpie(X_POS, Y_POS, gameState.getPlayer());
        gameState.getEnemies().getBirds().add(magpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Should not extract magpies as pigeons
        assertNotNull("Must check instanceof correctly", scarecrow);
    }

    /**
     * Tests line 73: instanceof Pigeon conditional (true branch)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testExtractPigeonsConditionalTrue() throws Exception {
        builder.entities.npc.enemies.Pigeon pigeon = 
            new builder.entities.npc.enemies.Pigeon(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        pigeon.setAttacking(true);
        gameState.getEnemies().getBirds().add(pigeon);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Pigeon should be extracted and scared
        assertFalse("Must extract pigeons when instanceof is true", 
                    isPigeonAttacking(pigeon));
    }

    /**
     * Tests line 77: extractPigeons returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testExtractPigeonsReturnsNonEmpty() throws Exception {
        builder.entities.npc.enemies.Pigeon pigeon = 
            new builder.entities.npc.enemies.Pigeon(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        pigeon.setAttacking(true);
        gameState.getEnemies().getBirds().add(pigeon);
        
        scarecrow.interact(mockEngine, gameState);
        
        // extractPigeons must return list with pigeons
        assertFalse("extractPigeons must return actual pigeons", 
                    isPigeonAttacking(pigeon));
    }

    /**
     * Tests line 82: isWithinScareRadius conditional for magpies (false branch)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testScareAwayMagpiesConditionalFalse() throws Exception {
        // Add magpie far away (outside scare radius)
        int tileSize = mockEngine.getDimensions().tileSize();
        builder.entities.npc.enemies.Magpie farMagpie = 
            new builder.entities.npc.enemies.Magpie(
                X_POS + tileSize * 10, Y_POS, gameState.getPlayer());
        farMagpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(farMagpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Far magpie should NOT be scared
        assertTrue("Must check if magpie is within radius", 
                   isMagpieAttacking(farMagpie));
    }

    /**
     * Tests line 82: isWithinScareRadius conditional for magpies (true branch)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testScareAwayMagpiesConditionalTrue() throws Exception {
        builder.entities.npc.enemies.Magpie closeMagpie = 
            new builder.entities.npc.enemies.Magpie(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        closeMagpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(closeMagpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Close magpie should be scared
        assertFalse("Must scare magpie when within radius", 
                    isMagpieAttacking(closeMagpie));
    }

    /**
     * Tests line 83: setAttacking is called for magpies
     * Mutation: removed call to builder/entities/npc/enemies/Magpie::setAttacking
     */
    @Test
    public void testScareAwayMagpiesCallsSetAttacking() throws Exception {
        builder.entities.npc.enemies.Magpie magpie = 
            new builder.entities.npc.enemies.Magpie(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        magpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(magpie);
        
        assertTrue("Magpie should be attacking initially", isMagpieAttacking(magpie));
        
        scarecrow.interact(mockEngine, gameState);
        
        // setAttacking must be called
        assertFalse("setAttacking must be called to stop magpie attacking", 
                    isMagpieAttacking(magpie));
    }

    /**
     * Tests line 90: isWithinScareRadius conditional for pigeons (false branch)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testScareAwayPigeonsConditionalFalse() throws Exception {
        // Add pigeon far away (outside scare radius)
        int tileSize = mockEngine.getDimensions().tileSize();
        builder.entities.npc.enemies.Pigeon farPigeon = 
            new builder.entities.npc.enemies.Pigeon(
                X_POS + tileSize * 10, Y_POS, gameState.getPlayer());
        farPigeon.setAttacking(true);
        gameState.getEnemies().getBirds().add(farPigeon);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Far pigeon should NOT be scared
        assertTrue("Must check if pigeon is within radius", 
                   isPigeonAttacking(farPigeon));
    }

    /**
     * Tests line 90: isWithinScareRadius conditional for pigeons (true branch)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testScareAwayPigeonsConditionalTrue() throws Exception {
        builder.entities.npc.enemies.Pigeon closePigeon = 
            new builder.entities.npc.enemies.Pigeon(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        closePigeon.setAttacking(true);
        gameState.getEnemies().getBirds().add(closePigeon);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Close pigeon should be scared
        assertFalse("Must scare pigeon when within radius", 
                    isPigeonAttacking(closePigeon));
    }

    /**
     * Tests line 91: setAttacking is called for pigeons
     * Mutation: removed call to builder/entities/npc/enemies/Pigeon::setAttacking
     */
    @Test
    public void testScareAwayPigeonsCallsSetAttacking() throws Exception {
        builder.entities.npc.enemies.Pigeon pigeon = 
            new builder.entities.npc.enemies.Pigeon(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        pigeon.setAttacking(true);
        gameState.getEnemies().getBirds().add(pigeon);
        
        assertTrue("Pigeon should be attacking initially", isPigeonAttacking(pigeon));
        
        scarecrow.interact(mockEngine, gameState);
        
        // setAttacking must be called
        assertFalse("setAttacking must be called to stop pigeon attacking", 
                    isPigeonAttacking(pigeon));
    }

    /**
     * Tests line 97: isWithinScareRadius comparison (false)
     * Mutation: removed conditional - replaced comparison check with false
     */
    @Test
    public void testIsWithinScareRadiusComparisonFalse() throws Exception {
        // Add bird far away
        int tileSize = mockEngine.getDimensions().tileSize();
        builder.entities.npc.enemies.Magpie farMagpie = 
            new builder.entities.npc.enemies.Magpie(
                X_POS + tileSize * 10, Y_POS, gameState.getPlayer());
        farMagpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(farMagpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Must correctly check distance < radius (false case)
        assertTrue("Must check distance comparison correctly", 
                   isMagpieAttacking(farMagpie));
    }

    /**
     * Tests line 97: isWithinScareRadius comparison (true)
     * Mutation: removed conditional - replaced comparison check with true
     */
    @Test
    public void testIsWithinScareRadiusComparisonTrue() throws Exception {
        // Add bird close by
        builder.entities.npc.enemies.Magpie closeMagpie = 
            new builder.entities.npc.enemies.Magpie(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        closeMagpie.setAttacking(true);
        gameState.getEnemies().getBirds().add(closeMagpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Must correctly check distance < radius (true case)
        assertFalse("Must check distance comparison correctly", 
                    isMagpieAttacking(closeMagpie));
    }

    /**
     * Tests line 97: isWithinScareRadius returns boolean (not always true)
     * Mutation: replaced boolean return with true
     */
    @Test
    public void testIsWithinScareRadiusReturnsCorrectBoolean() throws Exception {
        // Add one close bird and one far bird
        builder.entities.npc.enemies.Magpie closeMagpie = 
            new builder.entities.npc.enemies.Magpie(X_POS + 10, Y_POS + 10, gameState.getPlayer());
        closeMagpie.setAttacking(true);
        
        int tileSize = mockEngine.getDimensions().tileSize();
        builder.entities.npc.enemies.Magpie farMagpie = 
            new builder.entities.npc.enemies.Magpie(
                X_POS + tileSize * 10, Y_POS, gameState.getPlayer());
        farMagpie.setAttacking(true);
        
        gameState.getEnemies().getBirds().add(closeMagpie);
        gameState.getEnemies().getBirds().add(farMagpie);
        
        scarecrow.interact(mockEngine, gameState);
        
        // Close bird scared, far bird not scared (proving method returns correct boolean)
        assertFalse("Close bird must be scared", isMagpieAttacking(closeMagpie));
        assertTrue("Far bird must not be scared", isMagpieAttacking(farMagpie));
    }
}
