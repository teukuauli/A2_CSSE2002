package builder.world;

import builder.JavaBeanGameState;
import builder.entities.npc.NpcManager;
import builder.entities.npc.enemies.EnemyManager;
import builder.entities.tiles.Tile;
import builder.inventory.TinyInventory;
import builder.player.ChickenFarmer;
import engine.renderer.Dimensions;
import engine.renderer.Renderable;
import engine.renderer.TileGrid;
import org.junit.Before;
import org.junit.Test;
import scenarios.mocks.MockEngineState;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.*;

/**
 * Unit tests for the BeanWorld class.
 * Tests world tile management, rendering, and selection.
 */
public class BeanWorldTest {

    private BeanWorld world;
    private MockEngineState mockEngine;
    private JavaBeanGameState gameState;
    private Dimensions dimensions;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        world = new BeanWorld();
        mockEngine = new MockEngineState();
        dimensions = new TileGrid(25, 2000);
        
        ChickenFarmer player = new ChickenFarmer(200, 200);
        TinyInventory inventory = new TinyInventory(5, 10, 10);
        NpcManager npcs = new NpcManager();
        EnemyManager enemies = new EnemyManager(dimensions);
        
        gameState = new JavaBeanGameState(world, player, inventory, npcs, enemies);
    }

    /**
     * Tests that new world has no tiles.
     */
    @Test
    public void testNewWorldIsEmpty() {
        assertTrue("New world should have no tiles", world.allTiles().isEmpty());
    }

    /**
     * Tests placing a tile in the world.
     */
    @Test
    public void testPlaceTile() {
        Tile tile = new builder.entities.tiles.Grass(100, 100);
        world.place(tile);
        
        assertEquals("World should have 1 tile", 1, world.allTiles().size());
        assertTrue("World should contain placed tile", world.allTiles().contains(tile));
    }

    /**
     * Tests placing multiple tiles.
     */
    @Test
    public void testPlaceMultipleTiles() {
        Tile tile1 = new builder.entities.tiles.Grass(100, 100);
        Tile tile2 = new builder.entities.tiles.Grass(200, 200);
        Tile tile3 = new builder.entities.tiles.Grass(300, 300);
        
        world.place(tile1);
        world.place(tile2);
        world.place(tile3);
        
        assertEquals("World should have 3 tiles", 3, world.allTiles().size());
    }

    /**
     * Tests allTiles returns copy of tiles list.
     */
    @Test
    public void testAllTilesReturnsCopy() {
        Tile tile = new builder.entities.tiles.Grass(100, 100);
        world.place(tile);
        
        List<Tile> tiles1 = world.allTiles();
        List<Tile> tiles2 = world.allTiles();
        
        assertNotSame("allTiles should return new list", tiles1, tiles2);
        assertEquals("Lists should have same content", tiles1.size(), tiles2.size());
    }

    /**
     * Tests line 54: tilesAtPosition conditionals (gridX == tileX && gridY == tileY)
     * Mutation: removed conditional - replaced equality check with false/true
     */
    @Test
    public void testTilesAtPositionChecksCoordinates() {
        Dimensions dims = new TileGrid(25, 2000);
        
        // Place tile at specific position
        Tile tile1 = new builder.entities.tiles.Grass(100, 100);
        Tile tile2 = new builder.entities.tiles.Grass(200, 200);
        
        world.place(tile1);
        world.place(tile2);
        
        // Get tiles at tile1's position
        List<Tile> tilesAt100 = world.tilesAtPosition(100, 100, dims);
        
        // Should find tile1 but not tile2
        assertTrue("Should find tile at matching position", tilesAt100.contains(tile1));
        assertFalse("Should not find tile at different position", tilesAt100.contains(tile2));
    }

    /**
     * Tests line 54: gridX == tileX check (false case)
     */
    @Test
    public void testTilesAtPositionXCoordinateMismatch() {
        Dimensions dims = new TileGrid(25, 2000);
        
        Tile tile = new builder.entities.tiles.Grass(100, 100);
        world.place(tile);
        
        // Query position with different X
        List<Tile> tiles = world.tilesAtPosition(200, 100, dims);
        
        assertFalse("Should not find tile with different X coordinate", tiles.contains(tile));
    }

    /**
     * Tests line 54: gridY == tileY check (false case)
     */
    @Test
    public void testTilesAtPositionYCoordinateMismatch() {
        Dimensions dims = new TileGrid(25, 2000);
        
        Tile tile = new builder.entities.tiles.Grass(100, 100);
        world.place(tile);
        
        // Query position with different Y
        List<Tile> tiles = world.tilesAtPosition(100, 200, dims);
        
        assertFalse("Should not find tile with different Y coordinate", tiles.contains(tile));
    }

    /**
     * Tests line 54: both coordinates match (true case)
     */
    @Test
    public void testTilesAtPositionBothCoordinatesMatch() {
        Dimensions dims = new TileGrid(25, 2000);
        
        Tile tile = new builder.entities.tiles.Grass(100, 100);
        world.place(tile);
        
        // Query exact position
        List<Tile> tiles = world.tilesAtPosition(100, 100, dims);
        
        assertTrue("Should find tile with matching coordinates", tiles.contains(tile));
        assertEquals("Should find exactly one tile", 1, tiles.size());
    }

    /**
     * Tests line 58: tilesAtPosition returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testTilesAtPositionReturnsNonEmptyList() {
        Dimensions dims = new TileGrid(25, 2000);
        
        Tile tile1 = new builder.entities.tiles.Grass(100, 100);
        Tile tile2 = new builder.entities.tiles.Grass(100, 100); // Same position
        
        world.place(tile1);
        world.place(tile2);
        
        List<Tile> tiles = world.tilesAtPosition(100, 100, dims);
        
        assertNotNull("tilesAtPosition must return non-null list", tiles);
        assertFalse("tilesAtPosition must return non-empty list when tiles exist", tiles.isEmpty());
        assertEquals("Should find both tiles at same position", 2, tiles.size());
    }

    /**
     * Tests line 79: tileSelector filter.test check (false case)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testTileSelectorFilterReturnsFalse() {
        Tile tile1 = new builder.entities.tiles.Grass(100, 100);
        Tile tile2 = new builder.entities.tiles.Grass(200, 200);
        
        world.place(tile1);
        world.place(tile2);
        
        // Filter that returns false for tile2
        Predicate<Tile> filter = tile -> tile.getX() == 100;
        
        List<Tile> filtered = world.tileSelector(filter);
        
        assertTrue("Should include tile1", filtered.contains(tile1));
        assertFalse("Should not include tile2 (filter returned false)", filtered.contains(tile2));
    }

    /**
     * Tests line 79: tileSelector filter.test check (true case)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testTileSelectorFilterReturnsTrue() {
        Tile tile1 = new builder.entities.tiles.Grass(100, 100);
        Tile tile2 = new builder.entities.tiles.Grass(200, 200);
        
        world.place(tile1);
        world.place(tile2);
        
        // Filter that returns true for both
        Predicate<Tile> filter = tile -> tile.getX() >= 100;
        
        List<Tile> filtered = world.tileSelector(filter);
        
        assertTrue("Should include tile1", filtered.contains(tile1));
        assertTrue("Should include tile2", filtered.contains(tile2));
        assertEquals("Should include both tiles", 2, filtered.size());
    }

    /**
     * Tests line 83: tileSelector returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testTileSelectorReturnsNonEmptyList() {
        Tile tile1 = new builder.entities.tiles.Grass(100, 100);
        Tile tile2 = new builder.entities.tiles.Grass(200, 200);
        
        world.place(tile1);
        world.place(tile2);
        
        // Filter that accepts all tiles
        Predicate<Tile> filter = tile -> true;
        
        List<Tile> filtered = world.tileSelector(filter);
        
        assertNotNull("tileSelector must return non-null list", filtered);
        assertFalse("tileSelector must return non-empty list when matches exist", filtered.isEmpty());
        assertEquals("Should return all matching tiles", 2, filtered.size());
    }

    /**
     * Tests line 98: tick calls tile.tick
     * Mutation: removed call to Tile::tick
     */
    @Test
    public void testTickCallsTileTick() {
        // Create a custom tile that tracks if tick was called
        final boolean[] tickCalled = {false};
        
        Tile tile = new builder.entities.tiles.Grass(100, 100) {
            @Override
            public void tick(engine.EngineState state) {
                tickCalled[0] = true;
                super.tick(state);
            }
        };
        
        world.place(tile);
        
        world.tick(mockEngine, gameState);
        
        assertTrue("tick must call tile.tick() on all tiles", tickCalled[0]);
    }

    /**
     * Tests line 98: tick calls tick on all tiles
     */
    @Test
    public void testTickCallsTickOnAllTiles() {
        final int[] tickCount = {0};
        
        Tile tile1 = new builder.entities.tiles.Grass(100, 100) {
            @Override
            public void tick(engine.EngineState state) {
                tickCount[0]++;
                super.tick(state);
            }
        };
        
        Tile tile2 = new builder.entities.tiles.Grass(200, 200) {
            @Override
            public void tick(engine.EngineState state) {
                tickCount[0]++;
                super.tick(state);
            }
        };
        
        world.place(tile1);
        world.place(tile2);
        
        world.tick(mockEngine, gameState);
        
        assertEquals("tick must call tick on all tiles", 2, tickCount[0]);
    }

    /**
     * Tests line 119: render returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testRenderReturnsNonEmptyList() {
        Tile tile1 = new builder.entities.tiles.Grass(100, 100);
        Tile tile2 = new builder.entities.tiles.Grass(200, 200);
        
        world.place(tile1);
        world.place(tile2);
        
        List<Renderable> renderables = world.render();
        
        assertNotNull("render must return non-null list", renderables);
        assertFalse("render must return non-empty list when tiles exist", renderables.isEmpty());
        
        // Each tile's render() returns at least the tile itself
        assertTrue("render must include renderables from all tiles", renderables.size() >= 2);
    }

    /**
     * Tests render returns empty list when no tiles.
     */
    @Test
    public void testRenderReturnsEmptyListWhenNoTiles() {
        List<Renderable> renderables = world.render();
        
        assertNotNull("render must return non-null list", renderables);
        assertTrue("render must return empty list when no tiles", renderables.isEmpty());
    }

    /**
     * Tests tileSelector with filter that matches no tiles.
     */
    @Test
    public void testTileSelectorNoMatches() {
        Tile tile = new builder.entities.tiles.Grass(100, 100);
        world.place(tile);
        
        // Filter that matches nothing
        Predicate<Tile> filter = tile1 -> tile1.getX() > 500;
        
        List<Tile> filtered = world.tileSelector(filter);
        
        assertTrue("Should return empty list when no matches", filtered.isEmpty());
    }

    /**
     * Tests tileSelector with complex filter.
     */
    @Test
    public void testTileSelectorComplexFilter() {
        Tile tile1 = new builder.entities.tiles.Grass(100, 100);
        Tile tile2 = new builder.entities.tiles.Grass(200, 200);
        Tile tile3 = new builder.entities.tiles.Grass(300, 300);
        
        world.place(tile1);
        world.place(tile2);
        world.place(tile3);
        
        // Filter for tiles with X between 150 and 250
        Predicate<Tile> filter = tile -> tile.getX() >= 150 && tile.getX() <= 250;
        
        List<Tile> filtered = world.tileSelector(filter);
        
        assertFalse("Should not include tile1", filtered.contains(tile1));
        assertTrue("Should include tile2", filtered.contains(tile2));
        assertFalse("Should not include tile3", filtered.contains(tile3));
        assertEquals("Should have exactly 1 match", 1, filtered.size());
    }

    /**
     * Tests tilesAtPosition with multiple tiles at same position.
     */
    @Test
    public void testTilesAtPositionMultipleTilesSameLocation() {
        Dimensions dims = new TileGrid(25, 2000);
        
        Tile tile1 = new builder.entities.tiles.Grass(100, 100);
        Tile tile2 = new builder.entities.tiles.Grass(100, 100);
        Tile tile3 = new builder.entities.tiles.Grass(100, 100);
        
        world.place(tile1);
        world.place(tile2);
        world.place(tile3);
        
        List<Tile> tiles = world.tilesAtPosition(100, 100, dims);
        
        assertEquals("Should find all 3 tiles at same position", 3, tiles.size());
    }

    /**
     * Tests tilesAtPosition returns empty list when no tiles at position.
     */
    @Test
    public void testTilesAtPositionNoTilesFound() {
        Dimensions dims = new TileGrid(25, 2000);
        
        Tile tile = new builder.entities.tiles.Grass(100, 100);
        world.place(tile);
        
        List<Tile> tiles = world.tilesAtPosition(500, 500, dims);
        
        assertTrue("Should return empty list when no tiles at position", tiles.isEmpty());
    }

    /**
     * Tests render includes tile renderable.
     */
    @Test
    public void testRenderIncludesTile() {
        Tile tile = new builder.entities.tiles.Grass(100, 100);
        world.place(tile);
        
        List<Renderable> renderables = world.render();
        
        assertTrue("render should include the tile itself", renderables.contains(tile));
    }

    /**
     * Tests tick with empty world doesn't throw exception.
     */
    @Test
    public void testTickEmptyWorld() {
        // Should not throw exception
        world.tick(mockEngine, gameState);
        assertTrue("Empty world tick should complete", true);
    }

    /**
     * Tests render order consistency.
     */
    @Test
    public void testRenderOrderConsistency() {
        Tile tile1 = new builder.entities.tiles.Grass(100, 100);
        Tile tile2 = new builder.entities.tiles.Grass(200, 200);
        
        world.place(tile1);
        world.place(tile2);
        
        List<Renderable> renderables1 = world.render();
        List<Renderable> renderables2 = world.render();
        
        // Order should be consistent
        assertEquals("Render order should be consistent", 
                     renderables1.size(), renderables2.size());
    }
}
