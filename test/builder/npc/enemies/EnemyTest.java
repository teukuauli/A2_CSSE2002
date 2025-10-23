package builder.npc.enemies;

import builder.GameState;
import builder.entities.npc.enemies.Enemy;
import builder.JavaBeanGameState;
import builder.world.BeanWorld;
import builder.player.ChickenFarmer;
import builder.inventory.TinyInventory;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import scenarios.mocks.MockEngineState;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the Enemy class.
 * Tests basic enemy behavior and tick functionality.
 */
public class EnemyTest {

    private Enemy enemy;
    private MockEngineState mockEngine;
    private GameState gameState;

    @Before
    public void setUp() {
        enemy = new Enemy(100, 100);
        mockEngine = new MockEngineState();
        
        BeanWorld world = builder.world.WorldBuilder.empty();
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(mockEngine.getDimensions());
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
    }

    /**
     * Tests line 24: Enemy.tick() calls super.tick()
     * Mutation: removed call to builder/entities/npc/Npc::tick
     */
    @Test
    public void testTickCallsSuperTick() {
        // Set direction and speed so enemy will move
        enemy.setDirection(0); // Move right (0 degrees)
        enemy.setSpeed(5);
        
        int initialX = enemy.getX();
        int initialY = enemy.getY();
        
        // Call tick - should call super.tick() which calls move()
        enemy.tick(mockEngine, gameState);
        
        // Enemy position should change because super.tick() calls move()
        int newX = enemy.getX();
        int newY = enemy.getY();
        
        assertTrue("tick must call super.tick() which moves the enemy",
                   newX != initialX || newY != initialY);
    }

    /**
     * Tests that tick moves enemy in correct direction (east/right)
     */
    @Test
    public void testTickMovesEnemyEast() {
        enemy.setDirection(0); // 0 degrees = east/right
        enemy.setSpeed(3);
        
        int initialX = enemy.getX();
        
        enemy.tick(mockEngine, gameState);
        
        // Should move right (positive X direction)
        assertTrue("Enemy should move right when direction is 0",
                   enemy.getX() > initialX);
    }

    /**
     * Tests that tick moves enemy in correct direction (south/down)
     */
    @Test
    public void testTickMovesEnemySouth() {
        enemy.setDirection(90); // 90 degrees = south/down
        enemy.setSpeed(3);
        
        int initialY = enemy.getY();
        
        enemy.tick(mockEngine, gameState);
        
        // Should move down (positive Y direction)
        assertTrue("Enemy should move down when direction is 90",
                   enemy.getY() > initialY);
    }

    /**
     * Tests that tick moves enemy in correct direction (west/left)
     */
    @Test
    public void testTickMovesEnemyWest() {
        enemy.setDirection(180); // 180 degrees = west/left
        enemy.setSpeed(3);
        
        int initialX = enemy.getX();
        
        enemy.tick(mockEngine, gameState);
        
        // Should move left (negative X direction)
        assertTrue("Enemy should move left when direction is 180",
                   enemy.getX() < initialX);
    }

    /**
     * Tests that tick moves enemy in correct direction (north/up)
     */
    @Test
    public void testTickMovesEnemyNorth() {
        enemy.setDirection(270); // 270 degrees = north/up
        enemy.setSpeed(3);
        
        int initialY = enemy.getY();
        
        enemy.tick(mockEngine, gameState);
        
        // Should move up (negative Y direction)
        assertTrue("Enemy should move up when direction is 270",
                   enemy.getY() < initialY);
    }

    /**
     * Tests that tick with speed 0 doesn't move enemy
     */
    @Test
    public void testTickWithZeroSpeed() {
        enemy.setDirection(0);
        enemy.setSpeed(0);
        
        int initialX = enemy.getX();
        int initialY = enemy.getY();
        
        enemy.tick(mockEngine, gameState);
        
        assertEquals("Enemy with speed 0 should not move in X", initialX, enemy.getX());
        assertEquals("Enemy with speed 0 should not move in Y", initialY, enemy.getY());
    }

    /**
     * Tests multiple ticks accumulate movement
     */
    @Test
    public void testMultipleTicks() {
        enemy.setDirection(0); // Move right
        enemy.setSpeed(2);
        
        int initialX = enemy.getX();
        
        // Tick multiple times
        enemy.tick(mockEngine, gameState);
        enemy.tick(mockEngine, gameState);
        enemy.tick(mockEngine, gameState);
        
        // Should have moved multiple times
        int totalMovement = enemy.getX() - initialX;
        assertTrue("Multiple ticks should accumulate movement",
                   totalMovement > 2);
    }

    /**
     * Tests enemy constructor sets position
     */
    @Test
    public void testConstructorSetsPosition() {
        Enemy testEnemy = new Enemy(50, 75);
        
        assertEquals("Constructor should set X position", 50, testEnemy.getX());
        assertEquals("Constructor should set Y position", 75, testEnemy.getY());
    }

    /**
     * Tests enemy interact method exists and doesn't crash
     */
    @Test
    public void testInteractDoesNotCrash() {
        // interact() is empty but should not crash
        enemy.interact(mockEngine, gameState);
        
        // If we get here, interact didn't crash
        assertTrue("interact should not crash", true);
    }

    /**
     * Tests that super.tick() is called (detects mutation)
     */
    @Test
    public void testSuperTickIsCalled() {
        // Create enemy with non-zero speed and specific direction
        Enemy testEnemy = new Enemy(500, 500);
        testEnemy.setDirection(45); // Northeast
        testEnemy.setSpeed(10);
        
        int initialX = testEnemy.getX();
        int initialY = testEnemy.getY();
        
        // Tick should call super.tick() which calls move()
        testEnemy.tick(mockEngine, gameState);
        
        // If super.tick() is not called, enemy won't move
        assertNotEquals("tick must call super.tick() to move enemy (X changed)",
                        initialX, testEnemy.getX());
        assertNotEquals("tick must call super.tick() to move enemy (Y changed)",
                        initialY, testEnemy.getY());
    }

    /**
     * Tests movement distance matches speed
     */
    @Test
    public void testMovementDistanceMatchesSpeed() {
        enemy.setDirection(0); // Move right
        int speed = 5;
        enemy.setSpeed(speed);
        
        int initialX = enemy.getX();
        
        enemy.tick(mockEngine, gameState);
        
        int actualMovement = enemy.getX() - initialX;
        
        // Movement should be approximately equal to speed
        assertEquals("Movement distance should match speed",
                     speed, actualMovement, 1); // Allow 1 pixel tolerance for rounding
    }

    /**
     * Tests large speed values
     */
    @Test
    public void testLargeSpeed() {
        enemy.setDirection(0);
        enemy.setSpeed(100);
        
        int initialX = enemy.getX();
        
        enemy.tick(mockEngine, gameState);
        
        // Should move a large distance
        int movement = enemy.getX() - initialX;
        assertTrue("Large speed should result in large movement",
                   movement > 50);
    }
}
