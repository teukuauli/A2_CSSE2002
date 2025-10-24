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

    /**
     * Tests line 58: row != -1 && col != -1 condition
     * Mutation: removed conditional - replaced equality check with true
     * This test ensures that when row is set but col is -1, 
     * the message does NOT include column information
     */
    @Test
    public void testGetMessageWithRowButNoColumn() {
        String message = "Error at row only";
        int row = 5;
        WorldLoadException exception = new WorldLoadException(message, row);
        
        String resultMessage = exception.getMessage();
        
        // Should have line information
        assertTrue("Message should contain line number when row is set", 
                   resultMessage.contains("line 6"));
        
        // Should NOT have character information (col is -1)
        assertFalse("Message should NOT contain character when col is -1", 
                    resultMessage.contains("character"));
        
        // Should match the second condition format
        assertEquals("Should use row-only format", "Error at row only on line 6", resultMessage);
    }

    /**
     * Tests that when both row and col are -1, only base message is returned
     */
    @Test
    public void testGetMessageWithNoRowOrColumn() {
        String message = "Generic error";
        WorldLoadException exception = new WorldLoadException(message);
        
        String resultMessage = exception.getMessage();
        
        // Should not have line or character information
        assertFalse("Message should NOT contain line when row is -1", 
                    resultMessage.contains("line"));
        assertFalse("Message should NOT contain character when col is -1", 
                    resultMessage.contains("character"));
        
        // Should be just the base message
        assertEquals("Should return only base message", "Generic error", resultMessage);
    }

    /**
     * Tests that when both row and col are set, message includes both
     */
    @Test
    public void testGetMessageWithBothRowAndColumn() {
        String message = "Error at position";
        int row = 2;
        int col = 3;
        WorldLoadException exception = new WorldLoadException(message, row, col);
        
        String resultMessage = exception.getMessage();
        
        // Should have both line and character information
        assertTrue("Message should contain line number", resultMessage.contains("line 3"));
        assertTrue("Message should contain character position", resultMessage.contains("character 4"));
        
        // Should match the first condition format
        assertEquals("Should use row and column format", 
                     "Error at position on line 3, character 4", resultMessage);
    }

    /**
     * Tests edge case with row 0 and col -1
     */
    @Test
    public void testGetMessageWithRowZeroAndNoColumn() {
        WorldLoadException exception = new WorldLoadException("Error", 0);
        
        String resultMessage = exception.getMessage();
        
        assertTrue("Should contain line 1", resultMessage.contains("line 1"));
        assertFalse("Should NOT contain character", resultMessage.contains("character"));
    }

    /**
     * Tests edge case with negative row (should still be -1)
     */
    @Test
    public void testGetMessagePreservesNegativeRow() {
        String message = "Error message";
        WorldLoadException exception = new WorldLoadException(message);
        
        // Constructor with message only leaves row as -1
        String resultMessage = exception.getMessage();
        
        assertFalse("Should not contain line info when row is -1", 
                    resultMessage.contains("line"));
    }

    /**
     * Tests line 58: Mutation detection - replaced equality check with true
     * If condition is always true, would always use "line X, character Y" format
     * even when col is -1
     */
    @Test
    public void testGetMessageMutationDetectionLineOnly() {
        String message = "Test error";
        int row = 3;
        // col is -1 (not set)
        WorldLoadException exception = new WorldLoadException(message, row);
        
        String resultMessage = exception.getMessage();
        
        // Should be "Test error on line 4" (NOT "Test error on line 4, character X")
        assertEquals("Must NOT use col when col is -1", 
                     "Test error on line 4", resultMessage);
        
        // Verify it does NOT contain "character" keyword
        assertFalse("Must check col != -1 before including character info",
                    resultMessage.contains("character"));
    }

    /**
     * Tests large row and column values
     */
    @Test
    public void testGetMessageWithLargeValues() {
        WorldLoadException exception = new WorldLoadException("Error", 999, 888);
        
        String resultMessage = exception.getMessage();
        
        assertTrue("Should contain line 1000", resultMessage.contains("line 1000"));
        assertTrue("Should contain character 889", resultMessage.contains("character 889"));
    }
}

