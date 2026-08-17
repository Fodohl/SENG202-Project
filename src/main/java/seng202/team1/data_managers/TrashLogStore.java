package seng202.team1.data_managers;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Stores trash logs in memory and persists them to a JSON file.
 *
 * <p>This class coordinates reading, writing, and querying records.
 * It does not contain JavaFX, CSV, or PDF code.</p>
 */
public class TrashLogStore {

    private final Path filePath;
    private final TrashLogLoader loader;
    private final TrashLogWriter writer;
    private final List<TrashLog> trashLogs = new ArrayList<>();

    /**
     * Creates a store that saves logs to the specified JSON file.
     *
     * @param filePath location of the user's trash-log JSON file
     */
    public TrashLogStore(Path filePath) {
        this.filePath = Objects.requireNonNull(filePath);
        this.loader = new TrashLogLoader(filePath);
        this.writer = new TrashLogWriter();
    }

    /**
     * Returns the default location for the user's saved trash history.
     *
     * @return default JSON file location
     */
    public static Path defaultDataPath() {
        return Path.of(
                System.getProperty("user.home"),
                ".trashapp",
                "trashlogs.json"
        );
    }

    /**
     * Loads saved logs into memory.
     *
     * <p>A missing JSON file results in an empty history. Invalid JSON
     * causes TrashLogLoader to throw an IllegalStateException.</p>
     */
    public void load() {
        loader.readJson();

        trashLogs.clear();
        trashLogs.addAll(loader.getTrashLogs());
    }

    /**
     * Returns all logs currently held by this store.
     *
     * @return immutable copy of all trash logs
     */
    public List<TrashLog> getAll() {
        return List.copyOf(trashLogs);
    }

    /**
     * Saves a new log without removing existing records.
     *
     * @param newLog the trash log to save
     * @throws IOException if the JSON file cannot be written
     * @throws IllegalArgumentException if the log ID already exists
     */
    public void add(TrashLog newLog) throws IOException {
        Objects.requireNonNull(newLog);

        boolean idAlreadyExists = trashLogs.stream()
                .anyMatch(log ->
                        log.trashLogId() == newLog.trashLogId()
                );

        if (idAlreadyExists) {
            throw new IllegalArgumentException(
                    "A trash log already exists with ID "
                            + newLog.trashLogId()
            );
        }

        /*
         * Create a copy first. If JSON writing fails, the existing
         * in-memory history remains unchanged.
         */
        List<TrashLog> updatedLogs = new ArrayList<>(trashLogs);
        updatedLogs.add(newLog);

        writer.writeJson(filePath, updatedLogs);

        /*
         * Only update memory after the file was written successfully.
         */
        trashLogs.clear();
        trashLogs.addAll(updatedLogs);
    }

    /**
     * Calculates an ID for the next new trash log.
     *
     * @return one more than the largest existing ID, or 1 if empty
     */
    public int nextId() {
        return trashLogs.stream()
                .mapToInt(TrashLog::trashLogId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Finds all records for one bin, ordered by disposal time.
     *
     * @param binId ID of the bin to search for
     * @return matching records in chronological order
     */
    public List<TrashLog> findByBinId(int binId) {
        return trashLogs.stream()
                .filter(log -> log.binLogId() == binId)
                .sorted(Comparator.comparing(TrashLog::disposedAt))
                .toList();
    }

    /**
     * Finds records disposed within a date-time range.
     *
     * <p>The start is included; the end is excluded.</p>
     *
     * @param startInclusive first allowed disposal time
     * @param endExclusive first excluded disposal time
     * @return matching records in chronological order
     */
    public List<TrashLog> findByDateRange(
            LocalDateTime startInclusive,
            LocalDateTime endExclusive
    ) {
        validateDateRange(startInclusive, endExclusive);

        return trashLogs.stream()
                .filter(log ->
                        !log.disposedAt().isBefore(startInclusive)
                )
                .filter(log ->
                        log.disposedAt().isBefore(endExclusive)
                )
                .sorted(Comparator.comparing(TrashLog::disposedAt))
                .toList();
    }

    /**
     * Finds records for one bin within a date-time range.
     *
     * @param binId ID of the bin to search for
     * @param startInclusive first allowed disposal time
     * @param endExclusive first excluded disposal time
     * @return matching records in chronological order
     */
    public List<TrashLog> findByBinAndDateRange(
            int binId,
            LocalDateTime startInclusive,
            LocalDateTime endExclusive
    ) {
        validateDateRange(startInclusive, endExclusive);

        return trashLogs.stream()
                .filter(log -> log.binLogId() == binId)
                .filter(log ->
                        !log.disposedAt().isBefore(startInclusive)
                )
                .filter(log ->
                        log.disposedAt().isBefore(endExclusive)
                )
                .sorted(Comparator.comparing(TrashLog::disposedAt))
                .toList();
    }

    /**
     * Validates that a query date range has a valid order.
     */
    private void validateDateRange(
            LocalDateTime startInclusive,
            LocalDateTime endExclusive
    ) {
        Objects.requireNonNull(startInclusive);
        Objects.requireNonNull(endExclusive);

        if (!startInclusive.isBefore(endExclusive)) {
            throw new IllegalArgumentException(
                    "Start date must be before end date"
            );
        }
    }
}