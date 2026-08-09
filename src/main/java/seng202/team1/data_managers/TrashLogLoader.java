package seng202.team1.data_managers;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Loads trash logs data from a JSON file into {@link TrashLog} instances
 * @author Marcus Demafeliz
 */
public class TrashLogLoader {
    private static final Logger log = LogManager.getLogger(DataLoader.class);
    private final String filePath = "json/trashlogs.json";
    private final List<TrashLog> trashLogs = new ArrayList<>();


    public List<TrashLog> getTrashLogs() {
        return Collections.unmodifiableList(trashLogs);
    }

    /**
     * Find trash log by its ID
     * @param id ID of the trash log
     * @return TrashLog matching ID or null if not found
     */
    public TrashLog getTrashLogById(int id) {
        return trashLogs.stream()
                .filter(bin -> bin.trashLogId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Reads JSON file and puts in trashLogs list
     * Raises an error if file cannot be read
     */
    public void readJson() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = parseJson(parser);
            JSONArray logs = (JSONArray) jsonObject.get("trashLogs");
            for (Object o : logs) {
                trashLogs.add(parseLog((JSONObject) o));
            }
        } catch (Exception e) {
            log.error("Failed to parse JSON trash logs from file: " + e.getMessage());
        }
    }

    /**
     * Parses the JSON file into a JSON Object
     * @param parser JSONParser instance used to parse file contents
     * @return {@link JSONObject} with JSON data
     * @throws Exception if an error occurs while reading or parsing JSON file
     */
    private JSONObject parseJson(JSONParser parser) throws Exception {
        InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(filePath);
        if (is != null) {
            return (JSONObject) parser.parse(new InputStreamReader(is));
        }
        return (JSONObject) parser.parse(new FileReader(filePath));
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
            case "E-WASTE" -> Trash.TrashCategory.E_WASTE;
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
