package builder.npc.spawners;

import builder.entities.npc.spawners.EagleSpawner;
import engine.timing.TickTimer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the EagleSpawner class.
 * Tests spawner position management and timer functionality.
 */
public class EagleSpawnerTest {

    private EagleSpawner spawner;
    private static final int TEST_X = 100;
    private static final int TEST_Y = 200;
    private static final int CUSTOM_INTERVAL = 500;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        spawner = new EagleSpawner(TEST_X, TEST_Y);
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
     * Tests constructor with custom duration.
     */
    @Test
    public void testConstructorWithDuration() {
        EagleSpawner customSpawner = new EagleSpawner(50, 75, CUSTOM_INTERVAL);
        assertEquals(50, customSpawner.getX());
        assertEquals(75, customSpawner.getY());
        assertNotNull(customSpawner.getTimer());
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
}
