package builder.world;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for the OverlayBuilder class.
 * Tests parsing of .details files for game state initialization.
 */
public class OverlayBuilderTest {

    private static final String TEST_DETAILS_FILE = "resources/testmaps/base.details";
    private static final String PLAYER_TEST_FILE = "resources/testmaps/playerTest.details";
    private static final String MAGPIE_TEST_FILE = "resources/testmaps/magpieTest.details";
    private static final String EAGLE_TEST_FILE = "resources/testmaps/eagleTest.details";
    private static final String PIGEON_TEST_FILE = "resources/testmaps/pigeonTest.details";

    /**
     * Tests line 23: filepath.endsWith check (false)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testLoadChecksFileExtensionFalse() throws Exception {
        // Use reflection to call private load method
        java.lang.reflect.Method loadMethod = 
            OverlayBuilder.class.getDeclaredMethod("load", String.class);
        loadMethod.setAccessible(true);
        
        try {
            // Should throw IllegalArgumentException for wrong extension
            loadMethod.invoke(null, "resources/testmaps/base.txt");
            fail("Should throw IllegalArgumentException for wrong extension");
        } catch (java.lang.reflect.InvocationTargetException ex) {
            // Expected - verify it's IllegalArgumentException inside
            assertTrue("Must throw IllegalArgumentException for wrong extension",
                      ex.getCause() instanceof IllegalArgumentException);
        }
    }

    /**
     * Tests line 23: filepath.endsWith check (true)
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testLoadChecksFileExtensionTrue() throws Exception {
        // Use reflection to call private load method
        java.lang.reflect.Method loadMethod = 
            OverlayBuilder.class.getDeclaredMethod("load", String.class);
        loadMethod.setAccessible(true);
        
        // Should succeed with .details extension
        String content = (String) loadMethod.invoke(null, TEST_DETAILS_FILE);
        assertNotNull("load must check file extension and load valid files", content);
        assertTrue("Content should contain game data", content.contains("chickenFarmer"));
    }

    /**
     * Tests line 26: load returns non-empty string
     * Mutation: replaced return value with ""
     */
    @Test
    public void testLoadReturnsNonEmptyString() throws Exception {
        java.lang.reflect.Method loadMethod = 
            OverlayBuilder.class.getDeclaredMethod("load", String.class);
        loadMethod.setAccessible(true);
        
        String content = (String) loadMethod.invoke(null, TEST_DETAILS_FILE);
        
        assertNotNull("load must return non-null string", content);
        assertFalse("load must return non-empty string", content.isEmpty());
        assertTrue("load must return file contents", content.length() > 0);
    }

    /**
     * Tests line 43: i < contents.length() comparison (false)
     * Mutation: removed conditional - replaced comparison check with false
     */
    @Test
    public void testGetSectionLoopsOverContent() throws IOException {
        String testContent = ":chickenFarmer:\n|x:5 y:10 coins:2 food:3\nend;";
        
        List<String> section = OverlayBuilder.getSection("chickenFarmer", testContent);
        
        // Must loop through content to find section
        assertFalse("getSection must loop through content", section.isEmpty());
    }

    /**
     * Tests line 43: i < contents.length() comparison (true)
     * Mutation: removed conditional - replaced comparison check with true
     */
    @Test
    public void testGetSectionFindsSection() throws IOException {
        String testContent = ":chickenFarmer:\n|x:5 y:10 coins:2 food:3\nend;";
        
        List<String> section = OverlayBuilder.getSection("chickenFarmer", testContent);
        
        assertEquals("Must process entire loop", 1, section.size());
    }

    /**
     * Tests line 44: collectingLines && lines[i].equals("end;") (first condition false)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testGetSectionChecksCollectingLinesBeforeEnd() throws IOException {
        String testContent = ":test:\nline1\nline2\nend;";
        
        List<String> section = OverlayBuilder.getSection("test", testContent);
        
        // Should collect lines before hitting end
        assertEquals("Must check collectingLines before end", 2, section.size());
    }

    /**
     * Tests line 44: end; check (second condition false)
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testGetSectionChecksEndMarker() throws IOException {
        String testContent = ":test:\nline1\nline2\nend;";
        
        List<String> section = OverlayBuilder.getSection("test", testContent);
        
        // Should stop at end marker
        assertEquals("Must check for end marker", 2, section.size());
    }

    /**
     * Tests line 44: both conditions true
     * Mutation: removed conditional - replaced equality check with true
     */
    @Test
    public void testGetSectionReturnsWhenEndFound() throws IOException {
        String testContent = ":test:\nline1\nend;\nline3";
        
        List<String> section = OverlayBuilder.getSection("test", testContent);
        
        // Should return when end is found
        assertEquals("Must return when end found", 1, section.size());
    }

    /**
     * Tests line 45: getSection returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testGetSectionReturnsNonEmptyList() throws IOException {
        String testContent = ":test:\nline1\nline2\nend;";
        
        List<String> section = OverlayBuilder.getSection("test", testContent);
        
        assertNotNull("getSection must return non-null list", section);
        assertFalse("getSection must return non-empty list", section.isEmpty());
        assertEquals("Must return all lines in section", 2, section.size());
    }

    /**
     * Tests line 47: collectingLines check
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testGetSectionAddsLinesToSection() throws IOException {
        String testContent = ":test:\nline1\nline2\nend;";
        
        List<String> section = OverlayBuilder.getSection("test", testContent);
        
        // Must add lines when collecting
        assertTrue("Must collect lines", section.contains("line1"));
        assertTrue("Must collect lines", section.contains("line2"));
    }

    /**
     * Tests line 50: label match check
     * Mutation: removed conditional - replaced equality check with false
     */
    @Test
    public void testGetSectionChecksLabelMatch() throws IOException {
        String testContent = ":test:\nline1\nend;";
        
        List<String> section = OverlayBuilder.getSection("test", testContent);
        
        // Must match label to start collecting
        assertFalse("Must check label match", section.isEmpty());
    }

    /**
     * Tests line 72: extractSpawnDetailsFromLine returns non-null
     * Mutation: replaced return value with null
     */
    @Test
    public void testExtractSpawnDetailsFromLineReturnsNonNull() {
        String line = "|x:100 y:200 duration:360";
        
        SpawnerDetails details = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        assertNotNull("extractSpawnDetailsFromLine must return non-null", details);
        assertEquals(100, details.getX());
        assertEquals(200, details.getY());
        assertEquals(360, details.getDuration());
    }

    /**
     * Tests line 75: SpawnerDetails anonymous class getX()
     * Mutation: replaced int return with 0
     */
    @Test
    public void testSpawnerDetailsGetXReturnsCorrectValue() {
        String line = "|x:555 y:200 duration:360";
        
        SpawnerDetails details = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        // Must return the actual X value, not 0
        assertEquals("getX must return parsed X coordinate", 
                     555, details.getX());
        assertNotEquals("getX must not return 0 for non-zero value", 
                        0, details.getX());
    }

    /**
     * Tests line 80: SpawnerDetails anonymous class getY()
     * Mutation: replaced int return with 0
     */
    @Test
    public void testSpawnerDetailsGetYReturnsCorrectValue() {
        String line = "|x:100 y:666 duration:360";
        
        SpawnerDetails details = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        // Must return the actual Y value, not 0
        assertEquals("getY must return parsed Y coordinate", 
                     666, details.getY());
        assertNotEquals("getY must not return 0 for non-zero value", 
                        0, details.getY());
    }

    /**
     * Tests line 91: SpawnerDetails anonymous class getDuration()
     * Mutation: replaced int return with 0
     */
    @Test
    public void testSpawnerDetailsGetDurationReturnsCorrectValue() {
        String line = "|x:100 y:200 duration:999";
        
        SpawnerDetails details = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        // Must return the actual duration value, not 0
        assertEquals("getDuration must return parsed duration value", 
                     999, details.getDuration());
        assertNotEquals("getDuration must not return 0 for non-zero value", 
                        0, details.getDuration());
    }

    /**
     * Tests line 96: SpawnerDetails anonymous class toString()
     * Mutation: replaced return value with ""
     */
    @Test
    public void testSpawnerDetailsToStringReturnsNonEmpty() {
        String line = "|x:123 y:456 duration:789";
        
        SpawnerDetails details = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        String result = details.toString();
        
        // Must return non-empty string
        assertNotNull("toString must return non-null", result);
        assertFalse("toString must return non-empty string", result.isEmpty());
        assertTrue("toString must contain coordinate info", result.contains("123"));
        assertTrue("toString must contain coordinate info", result.contains("456"));
        assertTrue("toString must contain duration info", result.contains("789"));
    }

    /**
     * Tests SpawnerDetails with zero values (edge case)
     */
    @Test
    public void testSpawnerDetailsWithZeroValues() {
        String line = "|x:0 y:0 duration:0";
        
        SpawnerDetails details = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        assertEquals("getX should work with zero", 0, details.getX());
        assertEquals("getY should work with zero", 0, details.getY());
        assertEquals("getDuration should work with zero", 0, details.getDuration());
    }

    /**
     * Tests SpawnerDetails with large values
     */
    @Test
    public void testSpawnerDetailsWithLargeValues() {
        String line = "|x:9999 y:8888 duration:7777";
        
        SpawnerDetails details = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        assertEquals("getX should handle large values", 9999, details.getX());
        assertEquals("getY should handle large values", 8888, details.getY());
        assertEquals("getDuration should handle large values", 7777, details.getDuration());
    }

    /**
     * Tests SpawnerDetails toString format
     */
    @Test
    public void testSpawnerDetailsToStringFormat() {
        String line = "|x:10 y:20 duration:30";
        
        SpawnerDetails details = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        String result = details.toString();
        
        // Verify format includes OverlayBuilder prefix and all fields
        assertTrue("toString should contain OverlayBuilder", result.contains("OverlayBuilder"));
        assertTrue("toString should contain x value", result.contains("x:10") || result.contains("x:"));
        assertTrue("toString should contain y value", result.contains("y:20") || result.contains("y:"));
        assertTrue("toString should contain duration", result.contains("duration:30") || result.contains("duration:"));
    }

    /**
     * Tests SpawnerDetails all methods return correct values
     */
    @Test
    public void testSpawnerDetailsAllMethodsReturnCorrectValues() {
        String line = "|x:11 y:22 duration:33";
        
        SpawnerDetails details = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        // Verify all getter methods work correctly
        assertEquals("X must be 11", 11, details.getX());
        assertEquals("Y must be 22", 22, details.getY());
        assertEquals("Duration must be 33", 33, details.getDuration());
        
        // Verify toString includes all values
        String str = details.toString();
        assertTrue("toString must include all values", 
                   str.contains("11") && str.contains("22") && str.contains("33"));
    }

    /**
     * Tests line 123: getEagleSpawnDetailsFromString returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testGetEagleSpawnDetailsFromStringReturnsNonEmpty() throws Exception {
        java.lang.reflect.Method loadMethod = 
            OverlayBuilder.class.getDeclaredMethod("load", String.class);
        loadMethod.setAccessible(true);
        String content = (String) loadMethod.invoke(null, EAGLE_TEST_FILE);
        
        List<SpawnerDetails> eagles = OverlayBuilder.getEagleSpawnDetailsFromString(content);
        
        assertNotNull("getEagleSpawnDetailsFromString must return non-null", eagles);
        assertFalse("getEagleSpawnDetailsFromString must return non-empty list", 
                    eagles.isEmpty());
        assertTrue("Must parse eagle spawners", eagles.size() > 0);
    }

    /**
     * Tests line 140: getPigeonSpawnDetailsFromString returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testGetPigeonSpawnDetailsFromStringReturnsNonEmpty() throws Exception {
        java.lang.reflect.Method loadMethod = 
            OverlayBuilder.class.getDeclaredMethod("load", String.class);
        loadMethod.setAccessible(true);
        String content = (String) loadMethod.invoke(null, PIGEON_TEST_FILE);
        
        List<SpawnerDetails> pigeons = OverlayBuilder.getPigeonSpawnDetailsFromString(content);
        
        assertNotNull("getPigeonSpawnDetailsFromString must return non-null", pigeons);
        assertFalse("getPigeonSpawnDetailsFromString must return non-empty list", 
                    pigeons.isEmpty());
        assertTrue("Must parse pigeon spawners", pigeons.size() > 0);
    }

    /**
     * Tests line 157: getMagpieSpawnDetailsFromString returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testGetMagpieSpawnDetailsFromStringReturnsNonEmpty() throws Exception {
        java.lang.reflect.Method loadMethod = 
            OverlayBuilder.class.getDeclaredMethod("load", String.class);
        loadMethod.setAccessible(true);
        String content = (String) loadMethod.invoke(null, MAGPIE_TEST_FILE);
        
        List<SpawnerDetails> magpies = OverlayBuilder.getMagpieSpawnDetailsFromString(content);
        
        assertNotNull("getMagpieSpawnDetailsFromString must return non-null", magpies);
        assertFalse("getMagpieSpawnDetailsFromString must return non-empty list", 
                    magpies.isEmpty());
        assertTrue("Must parse magpie spawners", magpies.size() > 0);
    }

    /**
     * Tests line 177: extractPlayerDetailsFromLine returns non-null
     * Mutation: replaced return value with null
     */
    @Test
    public void testExtractPlayerDetailsFromLineReturnsNonNull() {
        String line = "|x:100 y:200 coins:5 food:10";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        assertNotNull("extractPlayerDetailsFromLine must return non-null", details);
        assertEquals(100, details.getX());
        assertEquals(200, details.getY());
        assertEquals(5, details.getStartingCoins());
        assertEquals(10, details.getStartingFood());
    }

    /**
     * Tests line 227: getPlayerDetailsFromFile returns non-null
     * Mutation: replaced return value with null
     */
    @Test
    public void testGetPlayerDetailsFromFileReturnsNonNull() throws Exception {
        java.lang.reflect.Method loadMethod = 
            OverlayBuilder.class.getDeclaredMethod("load", String.class);
        loadMethod.setAccessible(true);
        String content = (String) loadMethod.invoke(null, PLAYER_TEST_FILE);
        
        PlayerDetails player = OverlayBuilder.getPlayerDetailsFromFile(content);
        
        assertNotNull("getPlayerDetailsFromFile must return non-null", player);
        assertTrue("Player must have valid position", player.getX() >= 0);
        assertTrue("Player must have valid position", player.getY() >= 0);
    }

    /**
     * Tests line 244: getCabbageSpawnDetailsFromString returns non-empty list
     * Mutation: replaced return value with Collections.emptyList
     */
    @Test
    public void testGetCabbageSpawnDetailsFromStringReturnsNonEmpty() throws IOException {
        String testContent = ":cabbages:\n|x:100 y:200\n|x:150 y:250\nend;";
        
        List<CabbageDetails> cabbages = 
            OverlayBuilder.getCabbageSpawnDetailsFromString(testContent);
        
        assertNotNull("getCabbageSpawnDetailsFromString must return non-null", cabbages);
        assertFalse("getCabbageSpawnDetailsFromString must return non-empty list", 
                    cabbages.isEmpty());
        assertEquals("Must parse all cabbages", 2, cabbages.size());
    }

    /**
     * Tests line 253: extractCabbageDetailsFromLine returns non-null
     * Mutation: replaced return value with null
     */
    @Test
    public void testExtractCabbageDetailsFromLineReturnsNonNull() throws Exception {
        // Use reflection to call private method
        java.lang.reflect.Method extractMethod = 
            OverlayBuilder.class.getDeclaredMethod("extractCabbageDetailsFromLine", String.class);
        extractMethod.setAccessible(true);
        
        String line = "|x:100 y:200";
        CabbageDetails details = (CabbageDetails) extractMethod.invoke(null, line);
        
        assertNotNull("extractCabbageDetailsFromLine must return non-null", details);
        assertEquals(100, details.getX());
        assertEquals(200, details.getY());
    }

    /**
     * Tests line 256: CabbageDetails anonymous class getX()
     * Mutation: replaced int return with 0
     */
    @Test
    public void testCabbageDetailsGetXReturnsCorrectValue() throws Exception {
        java.lang.reflect.Method extractMethod = 
            OverlayBuilder.class.getDeclaredMethod("extractCabbageDetailsFromLine", String.class);
        extractMethod.setAccessible(true);
        
        String line = "|x:555 y:200";
        CabbageDetails details = (CabbageDetails) extractMethod.invoke(null, line);
        
        // Must return the actual X value, not 0
        assertEquals("getX must return parsed X coordinate", 
                     555, details.getX());
        assertNotEquals("getX must not return 0 for non-zero value", 
                        0, details.getX());
    }

    /**
     * Tests line 261: CabbageDetails anonymous class getY()
     * Mutation: replaced int return with 0
     */
    @Test
    public void testCabbageDetailsGetYReturnsCorrectValue() throws Exception {
        java.lang.reflect.Method extractMethod = 
            OverlayBuilder.class.getDeclaredMethod("extractCabbageDetailsFromLine", String.class);
        extractMethod.setAccessible(true);
        
        String line = "|x:100 y:666";
        CabbageDetails details = (CabbageDetails) extractMethod.invoke(null, line);
        
        // Must return the actual Y value, not 0
        assertEquals("getY must return parsed Y coordinate", 
                     666, details.getY());
        assertNotEquals("getY must not return 0 for non-zero value", 
                        0, details.getY());
    }

    /**
     * Tests line 266: CabbageDetails anonymous class toString()
     * Mutation: replaced return value with ""
     */
    @Test
    public void testCabbageDetailsToStringReturnsNonEmpty() throws Exception {
        java.lang.reflect.Method extractMethod = 
            OverlayBuilder.class.getDeclaredMethod("extractCabbageDetailsFromLine", String.class);
        extractMethod.setAccessible(true);
        
        String line = "|x:123 y:456";
        CabbageDetails details = (CabbageDetails) extractMethod.invoke(null, line);
        
        String result = details.toString();
        
        // Must return non-empty string
        assertNotNull("toString must return non-null", result);
        assertFalse("toString must return non-empty string", result.isEmpty());
        assertTrue("toString must contain coordinate info", result.contains("123"));
        assertTrue("toString must contain coordinate info", result.contains("456"));
    }

    /**
     * Tests CabbageDetails with zero values (edge case)
     */
    @Test
    public void testCabbageDetailsWithZeroValues() throws Exception {
        java.lang.reflect.Method extractMethod = 
            OverlayBuilder.class.getDeclaredMethod("extractCabbageDetailsFromLine", String.class);
        extractMethod.setAccessible(true);
        
        String line = "|x:0 y:0";
        CabbageDetails details = (CabbageDetails) extractMethod.invoke(null, line);
        
        assertEquals("getX should work with zero", 0, details.getX());
        assertEquals("getY should work with zero", 0, details.getY());
    }

    /**
     * Tests CabbageDetails with large values
     */
    @Test
    public void testCabbageDetailsWithLargeValues() throws Exception {
        java.lang.reflect.Method extractMethod = 
            OverlayBuilder.class.getDeclaredMethod("extractCabbageDetailsFromLine", String.class);
        extractMethod.setAccessible(true);
        
        String line = "|x:9999 y:8888";
        CabbageDetails details = (CabbageDetails) extractMethod.invoke(null, line);
        
        assertEquals("getX should handle large values", 9999, details.getX());
        assertEquals("getY should handle large values", 8888, details.getY());
    }

    /**
     * Tests CabbageDetails toString format
     */
    @Test
    public void testCabbageDetailsToStringFormat() throws Exception {
        java.lang.reflect.Method extractMethod = 
            OverlayBuilder.class.getDeclaredMethod("extractCabbageDetailsFromLine", String.class);
        extractMethod.setAccessible(true);
        
        String line = "|x:10 y:20";
        CabbageDetails details = (CabbageDetails) extractMethod.invoke(null, line);
        
        String result = details.toString();
        
        // Verify format includes OverlayBuilder prefix and all fields
        assertTrue("toString should contain OverlayBuilder", result.contains("OverlayBuilder"));
        assertTrue("toString should contain x value", result.contains("x:10") || result.contains("x:"));
        assertTrue("toString should contain y value", result.contains("y:20") || result.contains("y:"));
    }

    /**
     * Tests CabbageDetails all methods return correct values
     */
    @Test
    public void testCabbageDetailsAllMethodsReturnCorrectValues() throws Exception {
        java.lang.reflect.Method extractMethod = 
            OverlayBuilder.class.getDeclaredMethod("extractCabbageDetailsFromLine", String.class);
        extractMethod.setAccessible(true);
        
        String line = "|x:11 y:22";
        CabbageDetails details = (CabbageDetails) extractMethod.invoke(null, line);
        
        // Verify all getter methods work correctly
        assertEquals("X must be 11", 11, details.getX());
        assertEquals("Y must be 22", 22, details.getY());
        
        // Verify toString includes all values
        String str = details.toString();
        assertTrue("toString must include all values", 
                   str.contains("11") && str.contains("22"));
    }

    /**
     * Integration test: Parse complete details file
     */
    @Test
    public void testParseCompleteDetailsFile() throws Exception {
        java.lang.reflect.Method loadMethod = 
            OverlayBuilder.class.getDeclaredMethod("load", String.class);
        loadMethod.setAccessible(true);
        String content = (String) loadMethod.invoke(null, TEST_DETAILS_FILE);
        
        // Test all sections can be parsed
        PlayerDetails player = OverlayBuilder.getPlayerDetailsFromFile(content);
        List<SpawnerDetails> magpies = OverlayBuilder.getMagpieSpawnDetailsFromString(content);
        List<SpawnerDetails> eagles = OverlayBuilder.getEagleSpawnDetailsFromString(content);
        List<SpawnerDetails> pigeons = OverlayBuilder.getPigeonSpawnDetailsFromString(content);
        
        assertNotNull("Player details must be parsed", player);
        assertNotNull("Magpie spawners must be parsed", magpies);
        assertNotNull("Eagle spawners must be parsed", eagles);
        assertNotNull("Pigeon spawners must be parsed", pigeons);
        
        assertFalse("Magpie spawners should exist", magpies.isEmpty());
        assertFalse("Eagle spawners should exist", eagles.isEmpty());
        assertFalse("Pigeon spawners should exist", pigeons.isEmpty());
    }

    /**
     * Test SpawnerDetails values are correctly parsed
     */
    @Test
    public void testSpawnerDetailsCorrectValues() {
        String line = "|x:123 y:456 duration:789";
        
        SpawnerDetails details = OverlayBuilder.extractSpawnDetailsFromLine(line);
        
        assertEquals("X coordinate must be parsed correctly", 123, details.getX());
        assertEquals("Y coordinate must be parsed correctly", 456, details.getY());
        assertEquals("Duration must be parsed correctly", 789, details.getDuration());
    }

    /**
     * Test PlayerDetails values are correctly parsed
     */
    @Test
    public void testPlayerDetailsCorrectValues() {
        String line = "|x:111 y:222 coins:333 food:444";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        assertEquals("X coordinate must be parsed correctly", 111, details.getX());
        assertEquals("Y coordinate must be parsed correctly", 222, details.getY());
        assertEquals("Coins must be parsed correctly", 333, details.getStartingCoins());
        assertEquals("Food must be parsed correctly", 444, details.getStartingFood());
    }

    /**
     * Test CabbageDetails values are correctly parsed
     */
    @Test
    public void testCabbageDetailsCorrectValues() throws Exception {
        java.lang.reflect.Method extractMethod = 
            OverlayBuilder.class.getDeclaredMethod("extractCabbageDetailsFromLine", String.class);
        extractMethod.setAccessible(true);
        
        String line = "|x:777 y:888";
        CabbageDetails details = (CabbageDetails) extractMethod.invoke(null, line);
        
        assertEquals("X coordinate must be parsed correctly", 777, details.getX());
        assertEquals("Y coordinate must be parsed correctly", 888, details.getY());
    }

    /**
     * Test getSection with empty content
     */
    @Test
    public void testGetSectionWithEmptyContent() {
        String testContent = "";
        
        try {
            // Should throw IOException for empty content
            OverlayBuilder.getSection("missing", testContent);
            fail("Should throw IOException for missing section");
        } catch (IOException ex) {
            // Expected
            assertTrue("Must throw IOException", true);
        }
    }

    /**
     * Test getSection with exact case match
     */
    @Test
    public void testGetSectionExactCaseMatch() throws IOException {
        String testContent = ":test:\nline1\nend;";
        
        List<String> section = OverlayBuilder.getSection("test", testContent);
        
        assertFalse("Section should be found", section.isEmpty());
        assertEquals("Should have one line", 1, section.size());
    }

    /**
     * Test multiple spawners parsed correctly
     */
    @Test
    public void testMultipleSpawnersParsed() throws Exception {
        java.lang.reflect.Method loadMethod = 
            OverlayBuilder.class.getDeclaredMethod("load", String.class);
        loadMethod.setAccessible(true);
        String content = (String) loadMethod.invoke(null, TEST_DETAILS_FILE);
        
        List<SpawnerDetails> magpies = OverlayBuilder.getMagpieSpawnDetailsFromString(content);
        
        // base.details has 2 magpie spawners
        assertTrue("Multiple magpie spawners should be parsed", magpies.size() >= 2);
    }

    /**
     * Tests line 185: PlayerDetails anonymous class getStartingCoins()
     * Mutation: replaced int return with 0
     */
    @Test
    public void testPlayerDetailsGetStartingCoinsReturnsCorrectValue() {
        String line = "|x:100 y:200 coins:42 food:10";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        // Must return the actual coins value, not 0
        assertEquals("getStartingCoins must return parsed coins value", 
                     42, details.getStartingCoins());
        assertNotEquals("getStartingCoins must not return 0 for non-zero value", 
                        0, details.getStartingCoins());
    }

    /**
     * Tests line 180: PlayerDetails anonymous class getStartingFood()
     * Mutation: replaced int return with 0
     */
    @Test
    public void testPlayerDetailsGetStartingFoodReturnsCorrectValue() {
        String line = "|x:100 y:200 coins:5 food:77";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        // Must return the actual food value, not 0
        assertEquals("getStartingFood must return parsed food value", 
                     77, details.getStartingFood());
        assertNotEquals("getStartingFood must not return 0 for non-zero value", 
                        0, details.getStartingFood());
    }

    /**
     * Tests line 190: PlayerDetails anonymous class getX()
     * Mutation: replaced int return with 0
     */
    @Test
    public void testPlayerDetailsGetXReturnsCorrectValue() {
        String line = "|x:555 y:200 coins:5 food:10";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        // Must return the actual X value, not 0
        assertEquals("getX must return parsed X coordinate", 
                     555, details.getX());
        assertNotEquals("getX must not return 0 for non-zero value", 
                        0, details.getX());
    }

    /**
     * Tests line 195: PlayerDetails anonymous class getY()
     * Mutation: replaced int return with 0
     */
    @Test
    public void testPlayerDetailsGetYReturnsCorrectValue() {
        String line = "|x:100 y:666 coins:5 food:10";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        // Must return the actual Y value, not 0
        assertEquals("getY must return parsed Y coordinate", 
                     666, details.getY());
        assertNotEquals("getY must not return 0 for non-zero value", 
                        0, details.getY());
    }

    /**
     * Tests line 200: PlayerDetails anonymous class toString()
     * Mutation: replaced return value with ""
     */
    @Test
    public void testPlayerDetailsToStringReturnsNonEmpty() {
        String line = "|x:123 y:456 coins:789 food:321";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        String result = details.toString();
        
        // Must return non-empty string
        assertNotNull("toString must return non-null", result);
        assertFalse("toString must return non-empty string", result.isEmpty());
        assertTrue("toString must contain coordinate info", result.contains("123"));
        assertTrue("toString must contain coordinate info", result.contains("456"));
        assertTrue("toString must contain coins info", result.contains("789"));
        assertTrue("toString must contain food info", result.contains("321"));
    }

    /**
     * Tests PlayerDetails with zero values (edge case)
     */
    @Test
    public void testPlayerDetailsWithZeroValues() {
        String line = "|x:0 y:0 coins:0 food:0";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        assertEquals("getX should work with zero", 0, details.getX());
        assertEquals("getY should work with zero", 0, details.getY());
        assertEquals("getStartingCoins should work with zero", 0, details.getStartingCoins());
        assertEquals("getStartingFood should work with zero", 0, details.getStartingFood());
    }

    /**
     * Tests PlayerDetails with large values
     */
    @Test
    public void testPlayerDetailsWithLargeValues() {
        String line = "|x:9999 y:8888 coins:7777 food:6666";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        assertEquals("getX should handle large values", 9999, details.getX());
        assertEquals("getY should handle large values", 8888, details.getY());
        assertEquals("getStartingCoins should handle large values", 
                     7777, details.getStartingCoins());
        assertEquals("getStartingFood should handle large values", 
                     6666, details.getStartingFood());
    }

    /**
     * Tests PlayerDetails toString format
     */
    @Test
    public void testPlayerDetailsToStringFormat() {
        String line = "|x:10 y:20 coins:30 food:40";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        String result = details.toString();
        
        // Verify format includes OverlayBuilder prefix and all fields
        assertTrue("toString should contain OverlayBuilder", result.contains("OverlayBuilder"));
        assertTrue("toString should contain x value", result.contains("x:10") || result.contains("x:"));
        assertTrue("toString should contain y value", result.contains("y:20") || result.contains("y:"));
        assertTrue("toString should contain coins", result.contains("coins:30") || result.contains("coins:"));
        assertTrue("toString should contain food", result.contains("food:40") || result.contains("food:"));
    }

    /**
     * Tests PlayerDetails all methods return correct values
     */
    @Test
    public void testPlayerDetailsAllMethodsReturnCorrectValues() {
        String line = "|x:11 y:22 coins:33 food:44";
        
        PlayerDetails details = OverlayBuilder.extractPlayerDetailsFromLine(line);
        
        // Verify all getter methods work correctly
        assertEquals("X must be 11", 11, details.getX());
        assertEquals("Y must be 22", 22, details.getY());
        assertEquals("Coins must be 33", 33, details.getStartingCoins());
        assertEquals("Food must be 44", 44, details.getStartingFood());
        
        // Verify toString includes all values
        String str = details.toString();
        assertTrue("toString must include all values", 
                   str.contains("11") && str.contains("22") && 
                   str.contains("33") && str.contains("44"));
    }
}
