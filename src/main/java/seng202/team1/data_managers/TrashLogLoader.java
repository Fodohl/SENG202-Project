package seng202.team1.data_managers;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Loads trash logs data from a JSON file into {@link TrashLog} instances
 * @author Marcus Demafeliz
 */
public class TrashLogLoader {
    // CHANGED: identifies this class correctly in the log output.
    private static final Logger log =
            LogManager.getLogger(TrashLogLoader.class);

    // NEW: retained only for the existing bundled sample JSON file.
    private static final String SAMPLE_RESOURCE_PATH =
            "json/trashlogs.json";

    // CHANGED: a Path allows the loader to read a user-created JSON file.
    private final Path filePath;

    private final List<TrashLog> trashLogs = new ArrayList<>();


    public List<TrashLog> getTrashLogs() {
        return Collections.unmodifiableList(trashLogs);
    }

    /**
     * Uses the bundled sample JSON file.
     * Existing tests can keep using new TrashLogLoader().
     */
    public TrashLogLoader() {
        this.filePath = null;
    }

    /**
     * Uses a specific JSON file, such as the user's saved history.
     *
     * @param filePath location of the JSON file to load
     */
    public TrashLogLoader(Path filePath) {
        this.filePath = filePath;
    }
    /**
     * Find trash log by its ID
     * @param id ID of the trash log
     * @return TrashLog matching ID or null if not found
     */
    public TrashLog getTrashLogById(int id) {
        return trashLogs.stream()
                .filter(log -> log.trashLogId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Reads JSON file and puts in trashLogs list
     * Raises an error if file cannot be read
     */
    public void readJson() {
        // NEW: prevents duplicate records if readJson() is called twice.
        trashLogs.clear();

        // NEW: no saved file yet means the user simply has no history.
        if (filePath != null && Files.notExists(filePath)) {
            return;
        }

        JSONParser parser = new JSONParser();

        try (Reader reader = openReader()) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray logs = (JSONArray) jsonObject.get("trashLogs");

            // Allows a valid document with no trashLogs field yet.
            if (logs == null) {
                return;
            }

            for (Object logObject : logs) {
                trashLogs.add(parseLog((JSONObject) logObject));
            }
        } catch (IOException | ParseException exception) {
            String location = filePath == null
                    ? SAMPLE_RESOURCE_PATH
                    : filePath.toString();

            log.error(
                    "Failed to read trash logs from " + location,
                    exception
            );

            // CHANGED: do not silently continue with broken saved data.
            throw new IllegalStateException(
                    "Could not read trash-log data",
                    exception
            );
        }
    }
    /**
     * Opens either an external saved JSON file or the bundled sample file.
     *
     * @return reader for the selected JSON source
     * @throws IOException if the source cannot be opened
     */
    private Reader openReader() throws IOException {
        // NEW: use the supplied external file path for real user data.
        if (filePath != null) {
            return Files.newBufferedReader(
                    filePath,
                    StandardCharsets.UTF_8
            );
        }

        // Existing behaviour: use the bundled sample JSON resource.
        InputStream stream = TrashLogLoader.class
                .getClassLoader()
                .getResourceAsStream(SAMPLE_RESOURCE_PATH);

        if (stream == null) {
            throw new FileNotFoundException(
                    "Could not find " + SAMPLE_RESOURCE_PATH
            );
        }

        return new InputStreamReader(stream, StandardCharsets.UTF_8);
    }

    /**
     * Parses a JSON object into a {@link TrashLog} object.
     * The method extracts the trash log ID, bin log ID, creation and disposal
     * timestamps, and the logged trash details from the JSON object. The trash
     * category and type are converted from their JSON string representations
     * into the corresponding {@link Trash.TrashCategory} and
     * {@link Trash.TrashType} enum values.
     *
     * @param jsonObject represents trash log
     * @return {@link TrashLog}e instance
     */
    private TrashLog parseLog(JSONObject jsonObject) {
        int trashLogId = ((Number) jsonObject.get("trashLogId")).intValue();
        JSONObject trashLogged = (JSONObject) jsonObject.get("trashLogged");

        Object trashLoggedCategoryObj = trashLogged.get("trashCategory");
        String trashLoggedCategory = trashLoggedCategoryObj == null ? "" : trashLoggedCategoryObj.toString();
        Trash.TrashCategory trashCategory = switch (trashLoggedCategory.toUpperCase(Locale.ROOT)) {
            case "RECYCLABLE" -> Trash.TrashCategory.RECYCLABLE;
            case "ORGANIC" -> Trash.TrashCategory.ORGANIC;
            case "E-WASTE", "E_WASTE" -> Trash.TrashCategory.E_WASTE;
            default -> Trash.TrashCategory.GENERAL_WASTE;
        };

        Object trashLoggedTypeObj = trashLogged.get("trashType");
        String trashLoggedType = trashLoggedTypeObj == null ? "" : trashLoggedTypeObj.toString();
        Trash.TrashType trashType = switch (trashLoggedType.toUpperCase(Locale.ROOT)) {
            case "PLASTIC_BOTTLE" -> Trash.TrashType.PLASTIC_BOTTLE;
            case "PAPER" -> Trash.TrashType.PAPER;
            case "CARDBOARD_BOX" -> Trash.TrashType.CARDBOARD_BOX;
            case "COFFEE_CUP" -> Trash.TrashType.COFFEE_CUP;
            default -> Trash.TrashType.OTHER;
        };

        int trashWeight = ((Number) trashLogged.get("trashWeight")).intValue();
        LocalDateTime disposedAt = LocalDateTime.parse(jsonObject.get("disposedAt").toString());
        int binLogId = ((Number) jsonObject.get("binLogId")).intValue();
        LocalDateTime createdAt = LocalDateTime.parse(jsonObject.get("createdAt").toString());

        Trash trash = new Trash(trashCategory, trashType, trashWeight);
        return new TrashLog(trashLogId, trash,  disposedAt, binLogId, createdAt);
    }
}
