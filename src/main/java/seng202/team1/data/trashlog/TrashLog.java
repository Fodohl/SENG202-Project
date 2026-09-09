package seng202.team1.data.trashlog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A record representing a user's trash log keeping record of the disposal time,
 * the bin id where it was disposed, the log timestamp, the trash itself.
 *
 * @author Marcus Demafeliz
 */
public record TrashLog(int trashLogId, LocalDateTime createdAt, LocalDateTime disposedAt, int userID, int binID, TrashType trashType, int trashWeight)
{

    public TrashLog {
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("TrashLog{%d, %s, %s, %d, %d, %s, %d}", trashLogId,  disposedAt.format(formatter), createdAt.format(formatter), userID, binID, trashType, trashWeight);
    }
}
