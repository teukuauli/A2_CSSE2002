package builder.world;

import builder.entities.tiles.Tile;
import engine.renderer.Dimensions;
import engine.renderer.TileGrid;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the WorldBuilder class.
 * Tests world construction from strings and files.
 */
public class WorldBuilderTest {

    private Dimensions dimensions;
    private static final int SIZE = 800;
    private static final int TILES_PER_ROW = 10;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        dimensions = new TileGrid(TILES_PER_ROW, SIZE);
    }

    /**
     * Tests that empty world can be created.
     */
    @Test
    public void testEmptyWorld() {
        BeanWorld world = WorldBuilder.empty();
        assertNotNull(world);
        assertTrue(world.allTiles().isEmpty());
    }

    /**
     * Tests that fromString throws exception for wrong number of lines.
     */
    @Test(expected = WorldLoadException.class)
    public void testFromStringWrongNumberOfLines() throws WorldLoadException {
        String text = "gggggggggg\n"; // Only 1 line, expects 10
        WorldBuilder.fromString(dimensions, text);
    }

    /**
     * Tests that fromString throws exception for wrong line length.
     */
    @Test(expected = WorldLoadException.class)
    public void testFromStringWrongLineLength() throws WorldLoadException {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            text.append("ggggg\n"); // Only 5 characters, expects 10
        }
        WorldBuilder.fromString(dimensions, text.toString());
    }

    /**
     * Tests that fromString throws exception for unknown symbol.
     */
    @Test(expected = WorldLoadException.class)
    public void testFromStringUnknownSymbol() throws WorldLoadException {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                text.append("gggggXgggg\n"); // X is unknown symbol
            } else {
                text.append("gggggggggg\n");
            }
        }
        WorldBuilder.fromString(dimensions, text.toString());
    }

    /**
     * Tests that valid string creates correct number of tiles.
     */
    @Test
    public void testFromStringValidInput() throws WorldLoadException {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            text.append("gggggggggg");
            if (i < 9) {
                text.append("\n");
            }
        }
        
        List<Tile> tiles = WorldBuilder.fromString(dimensions, text.toString());
        assertEquals(100, tiles.size()); // 10x10 grid
    }

    /**
     * Tests fromTiles creates world with tiles.
     */
    @Test
    public void testFromTiles() throws WorldLoadException {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            text.append("gggggggggg");
            if (i < 9) {
                text.append("\n");
            }
        }
        
        List<Tile> tiles = WorldBuilder.fromString(dimensions, text.toString());
        BeanWorld world = WorldBuilder.fromTiles(tiles);
        
        assertNotNull(world);
        assertEquals(100, world.allTiles().size());
    }

    /**
     * Tests fromTiles with empty list.
     */
    @Test
    public void testFromTilesEmptyList() {
        BeanWorld world = WorldBuilder.fromTiles(List.of());
        assertNotNull(world);
        assertTrue(world.allTiles().isEmpty());
    }

    /**
     * Tests that fromFile throws IOException for non-existent file.
     */
    @Test(expected = IOException.class)
    public void testFromFileNonExistent() throws IOException, WorldLoadException {
        WorldBuilder.fromFile(dimensions, "nonexistent.map");
    }

    /**
     * Tests that valid map file loads correctly.
     */
    @Test
    public void testFromFileValidMap() throws IOException, WorldLoadException {
        // Use correct dimensions for base.map file
        Dimensions correctDimensions = new TileGrid(25, 800); // base.map is 25x25
        BeanWorld world = WorldBuilder.fromFile(correctDimensions, "resources/testmaps/base.map");
        assertNotNull(world);
        assertFalse(world.allTiles().isEmpty());
    }
}
