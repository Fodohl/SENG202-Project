package seng202.team1.data_managers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a user's trash log
 * @author Marcus Demafeliz
 */
public class TrashLog {

    private final int trashLogId;
    private final int binLogId;
    private final Trash trashLogged;
    private final LocalDateTime disposedAt;
    private final LocalDateTime createdAt;

    /**
     * Constructor for TrashLog class
     * @param trashLogId - unique identifier for log
     * @param trashLogged - Trash object the user has logged
     * @param binLogId - the unique identifier of bin where waste is disposed
     */
    public TrashLog(int trashLogId, Trash trashLogged, LocalDateTime disposedAt, int binLogId) {
        this.trashLogId = trashLogId;
        this.trashLogged = trashLogged;
        this.disposedAt = disposedAt;
        this.binLogId = binLogId;
        this.createdAt = LocalDateTime.now();
    }

    public int getTrashLogId() {
        return trashLogId;
    }

    public Trash getTrashLogged() {
        return trashLogged;
    }

    public int getBinLogId() {
        return binLogId;
    }

    public LocalDateTime getDisposedAt() {
        return disposedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("TrashLog{%d, %s, %d, %s, %s}", trashLogId, trashLogged.toString(), binLogId, disposedAt.format(formatter), createdAt.format(formatter));
    }
}
