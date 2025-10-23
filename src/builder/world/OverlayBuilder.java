package builder.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts a list of information that changes the default beginning game state regarding starting
 * resources, placement of the player, cabbages, spawner locations and times etc
 */
public class OverlayBuilder {

    /**
     * Loads the contents of a file as a string.
     *
     * @param filepath Location of the text file we wish to load.
     * @return String representation of the contents of file found at the filepath.
     * @throws IOException If the file cannot be read.
     */
    private static String load(String filepath) throws IOException {
        if (!filepath.endsWith(".details")) {
            throw new IllegalArgumentException("incorrect file name must have .details at end!");
        }
        return Files.readString(Path.of(filepath));
    }

    /**
     * Search the given string for a line equivalent to the given label surrounded by
     * a pair of colons, then collect all lines of text between that label and the next
     * line that reads as "end;".
     *
     * @param label Label we are searching for.
     * @param contents File contents we are searching through.
     * @return A list of lines within the searched for section.
     * @throws IOException If the section is not found.
     */
    public static List<String> getSection(String label, String contents) throws IOException {
        final String[] lines = contents.split("\n");
        boolean collectingLines = false;
        final List<String> section = new ArrayList<>();
        for (int i = 0; i < contents.length(); i++) {
            if (collectingLines && lines[i].toLowerCase().trim().equals("end;")) {
                return section;
            }
            if (collectingLines) {
                section.add(lines[i].toLowerCase().trim());
            }
            if (lines[i].toLowerCase().trim().equals(":" + label.toLowerCase().trim() + ":")) {
                collectingLines = true;
            }
        }
        throw new IOException("Section not Found!");
    }

    /**
     * Extracts spawner details from a line of text.
     *
     * @param line Line to process.
     * @return A new SpawnerDetails holding the information extracted from the line.
     */
    public static SpawnerDetails extractSpawnDetailsFromLine(String line) {
        String[] chunks = line.split(" ");
        assert chunks.length == 3; // should always be 3 chunks in a correctly shaped line.
        String[] xCoord = chunks[0].split(":");
        String[] yCoord = chunks[1].split(":");
        String[] durationChunk = chunks[2].split(":");
        final int xValue = Integer.parseInt(xCoord[1]);
        final int yValue = Integer.parseInt(yCoord[1]);
        final int duration = Integer.parseInt(durationChunk[1]);
        return new SpawnerDetails() {
            @Override
            public int getX() {
                return xValue;
            }

            @Override
            public int getY() {
                return yValue;
            }

            @Override
            public void setX(int xPos) {}

            @Override
            public void setY(int yPos) {}

            @Override
            public int getDuration() {
                return duration;
            }

            @Override
            public String toString() {
                return "OverlayBuilder["
                        + "x:"
                        + xValue
                        + ","
                        + "y:"
                        + yValue
                        + ",duration:"
                        + duration
                        + "]";
            }
        };
    }

    /**
     * Gets eagle spawn details from the provided content string.
     *
     * @param detailsContent The content string to parse.
     * @return List of eagle spawner details.
     * @throws IOException If the section cannot be found.
     */
    public static List<SpawnerDetails> getEagleSpawnDetailsFromString(String detailsContent)
            throws IOException {
        List<String> section = OverlayBuilder.getSection("eaglespawner", detailsContent);
        final List<SpawnerDetails> list = new ArrayList<>();
        for (String entry : section) {
            list.add(extractSpawnDetailsFromLine(entry));
        }
        return list;
    }

    /**
     * Gets pigeon spawn details from the provided content string.
     *
     * @param detailsContent The content string to parse.
     * @return List of pigeon spawner details.
     * @throws IOException If the section cannot be found.
     */
    public static List<SpawnerDetails> getPigeonSpawnDetailsFromString(String detailsContent)
            throws IOException {
        List<String> section = OverlayBuilder.getSection("pigeonspawner", detailsContent);
        final List<SpawnerDetails> list = new ArrayList<>();
        for (String entry : section) {
            list.add(extractSpawnDetailsFromLine(entry));
        }
        return list;
    }

    /**
     * Gets magpie spawn details from the provided content string.
     *
     * @param detailsContent The content string to parse.
     * @return List of magpie spawner details.
     * @throws IOException If the section cannot be found.
     */
    public static List<SpawnerDetails> getMagpieSpawnDetailsFromString(String detailsContent)
            throws IOException {
        List<String> section = OverlayBuilder.getSection("magpiespawner", detailsContent);
        final List<SpawnerDetails> list = new ArrayList<>();
        for (String entry : section) {
            list.add(extractSpawnDetailsFromLine(entry));
        }
        return list;
    }

    /**
     * Extracts player details from a line of text.
     *
     * @param line Line to process.
     * @return A new PlayerDetails holding the information extracted from the line.
     */
    public static PlayerDetails extractPlayerDetailsFromLine(String line) {
        String[] chunks = line.split(" ");
        assert chunks.length == 4; // should always be 4 chunks in a correctly shaped line.
        String[] xCoord = chunks[0].split(":");
        String[] yCoord = chunks[1].split(":");
        String[] coinChunk = chunks[2].split(":");
        String[] foodChunk = chunks[3].split(":");
        final int xValue = Integer.parseInt(xCoord[1]);
        final int yValue = Integer.parseInt(yCoord[1]);
        final int coins = Integer.parseInt(coinChunk[1]);
        final int food = Integer.parseInt(foodChunk[1]);
        return new PlayerDetails() {
            @Override
            public int getStartingFood() {
                return food;
            }

            @Override
            public int getStartingCoins() {
                return coins;
            }

            @Override
            public int getX() {
                return xValue;
            }

            @Override
            public int getY() {
                return yValue;
            }

            @Override
            public String toString() {
                return "OverlayBuilder["
                        + "x:"
                        + xValue
                        + ","
                        + "y:"
                        + yValue
                        + ",coins:"
                        + coins
                        + ",food:"
                        + food
                        + "]";
            }
        };
    }

    /**
     * Gets player details from the provided content string.
     *
     * @param detailsContent The content string to parse.
     * @return PlayerDetails with starting position and resources.
     * @throws IOException If the section cannot be found.
     */
    public static PlayerDetails getPlayerDetailsFromFile(String detailsContent) throws IOException {
        List<String> section = OverlayBuilder.getSection("chickenFarmer", detailsContent);
        assert section.size()
                == 1; // right now we only expect there to ever be one chicken farmer entry
        String entry = section.getFirst();
        return OverlayBuilder.extractPlayerDetailsFromLine(entry);
    }

    /**
     * Gets cabbage spawn details from the provided content string.
     *
     * @param detailsContent The content string to parse.
     * @return List of cabbage details.
     * @throws IOException If the section cannot be found.
     */
    public static List<CabbageDetails> getCabbageSpawnDetailsFromString(String detailsContent)
            throws IOException {
        final List<String> section = OverlayBuilder.getSection("cabbages", detailsContent);
        final List<CabbageDetails> list = new ArrayList<>();
        for (String entry : section) {
            list.add(extractCabbageDetailsFromLine(entry));
        }
        return list;
    }

    private static CabbageDetails extractCabbageDetailsFromLine(String line) {
        final String[] chunks = line.split(" ");
        String[] xCoord = chunks[0].split(":");
        String[] yCoord = chunks[1].split(":");
        final int xValue = Integer.parseInt(xCoord[1]);
        final int yValue = Integer.parseInt(yCoord[1]);
        return new CabbageDetails() {
            @Override
            public int getX() {
                return xValue;
            }

            @Override
            public int getY() {
                return yValue;
            }

            @Override
            public String toString() {
                return "OverlayBuilder[" + "x:" + xValue + "," + "y:" + yValue + "]";
            }
        };
    }
}
