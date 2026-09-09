package seng202.team1.data.trashlog;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;

public class TrashLogWriter {

    public void writeJson(Path destination, List<TrashLog> logs)
            throws IOException {

        Path absoluteDestination = destination.toAbsolutePath();
        Path parent = absoluteDestination.getParent();
        Files.createDirectories(parent);

        Path temporaryFile = Files.createTempFile(
                parent,
                "trashlogs-",
                ".tmp"
        );

        try {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    temporaryFile,
                    StandardCharsets.UTF_8
            )) {
                writer.write(createDocument(logs).toJSONString());
            }

            try {
                Files.move(
                        temporaryFile,
                        absoluteDestination,
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(
                        temporaryFile,
                        absoluteDestination,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    private JSONObject createDocument(List<TrashLog> logs) {
        JSONArray logArray = new JSONArray();

        for (TrashLog log : logs) {
            logArray.add(trashLogToJson(log));
        }

        JSONObject root = new JSONObject();
        root.put("schemaVersion", 1);
        root.put("lastSavedAt", Instant.now().toString());
        root.put("trashLogs", logArray);

        return root;
    }

    private JSONObject trashLogToJson(TrashLog log) {
        JSONObject json = new JSONObject();

        json.put("trashLogId", log.trashLogId());
        json.put("trashLogged", trashToJson(log.trashLogged()));
        json.put("disposedAt", log.disposedAt().toString());
        json.put("binLogId", log.binLogId());
        json.put("createdAt", log.createdAt().toString());

        return json;
    }

    private JSONObject trashToJson(Trash trash) {
        JSONObject json = new JSONObject();

        json.put("binCategory", trash.binCategory().name());
        json.put("trashType", trash.trashType().name());
        json.put("trashWeight", trash.trashWeight());

        return json;
    }


}