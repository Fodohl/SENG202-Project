package seng202.team1.data.trashlog;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * A record representing a user's trash log keeping record of the disposal time,
 * the bin id where it was disposed, the log timestamp, the trash itself.
 *
 * @author Marcus Demafeliz
 */
public record TrashLog(int trashLogId, Instant createdAt, Instant disposedAt, int userID, int binID, TrashType trashType, double trashWeight)
{

    public TrashLog {
    }

    public TrashLog(Instant createdAt, Instant disposedAt, int userID, int binID, TrashType trashType, double trashWeight) {
        this(-1, createdAt,disposedAt,userID,binID,trashType,trashWeight);
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return String.format("TrashLog{%d, %s, %s, %d, %d, %s, %.2f}", trashLogId,  formatter.format(createdAt), formatter.format(disposedAt), userID, binID, trashType, trashWeight);
    }
}
