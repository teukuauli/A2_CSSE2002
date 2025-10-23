package builder.world;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the WorldLoadException class.
 * Tests exception construction and message formatting.
 */
public class WorldLoadExceptionTest {

    /**
     * Tests constructor with message only.
     */
    @Test
    public void testConstructorWithMessageOnly() {
        String message = "Test error message";
        WorldLoadException exception = new WorldLoadException(message);
        
        assertEquals(message, exception.getMessage());
    }

    /**
     * Tests constructor with message and row.
     */
    @Test
    public void testConstructorWithMessageAndRow() {
        String message = "Row error";
        int row = 5;
        WorldLoadException exception = new WorldLoadException(message, row);
        
        String expectedMessage = "Row error on line 6"; // Row is 0-indexed, displayed as 1-indexed
        assertEquals(expectedMessage, exception.getMessage());
    }

    /**
     * Tests constructor with message, row, and column.
     */
    @Test
    public void testConstructorWithMessageRowAndColumn() {
        String message = "Column error";
        int row = 3;
        int col = 7;
        WorldLoadException exception = new WorldLoadException(message, row, col);
        
        String expectedMessage = "Column error on line 4, character 8";
        assertEquals(expectedMessage, exception.getMessage());
    }

    /**
     * Tests that exception message contains the original message.
     */
    @Test
    public void testMessageContainsOriginalText() {
        String message = "Unknown symbol error";
        WorldLoadException exception = new WorldLoadException(message);
        
        assertTrue(exception.getMessage().contains("Unknown symbol error"));
    }

    /**
     * Tests row indexing starts from 0.
     */
    @Test
    public void testRowIndexingStartsFromZero() {
        WorldLoadException exception = new WorldLoadException("Error", 0);
        assertTrue(exception.getMessage().contains("line 1"));
    }

    /**
     * Tests column indexing starts from 0.
     */
    @Test
    public void testColumnIndexingStartsFromZero() {
        WorldLoadException exception = new WorldLoadException("Error", 0, 0);
        assertTrue(exception.getMessage().contains("line 1"));
        assertTrue(exception.getMessage().contains("character 1"));
    }

    /**
     * Tests that exception extends Exception.
     */
    @Test
    public void testExceptionExtendsException() {
        WorldLoadException exception = new WorldLoadException("Test");
        assertTrue(exception instanceof Exception);
    }
}
