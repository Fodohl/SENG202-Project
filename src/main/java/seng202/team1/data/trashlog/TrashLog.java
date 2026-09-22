package seng202.team1.data.trashlog;

<<<<<<< HEAD
import java.time.LocalDateTime;
=======
import java.time.Instant;
import java.time.ZoneId;
>>>>>>> main
import java.time.format.DateTimeFormatter;

/**
 * A record representing a user's trash log keeping record of the disposal time,
 * the bin id where it was disposed, the log timestamp, the trash itself.
 *
 * @author Marcus Demafeliz
 */
<<<<<<< HEAD
public record TrashLog(int trashLogId, Trash trashLogged, LocalDateTime disposedAt, int binLogId,
                       LocalDateTime createdAt) {

    /**
     * Constructor for TrashLog class
     *
     * @param trashLogId  unique identifier for log
     * @param trashLogged Trash object the user has logged
     * @param binLogId the unique identifier of bin where waste is disposed
     */
    public TrashLog {
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("TrashLog{%d, %s, %d, %s, %s}", trashLogId, trashLogged.toString(), binLogId, disposedAt.format(formatter), createdAt.format(formatter));
    }

    public Trash getTrashLogged(){
        return trashLogged;
=======
public record TrashLog(int trashLogId, Instant createdAt, Instant disposedAt, int userID, int binID, TrashType trashType, int trashWeight)
{

    public TrashLog {
    }

    public TrashLog(Instant createdAt, Instant disposedAt, int userID, int binID, TrashType trashType, int trashWeight) {
        this(-1, createdAt,disposedAt,userID,binID,trashType,trashWeight);
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return String.format("TrashLog{%d, %s, %s, %d, %d, %s, %d}", trashLogId,  formatter.format(createdAt), formatter.format(disposedAt), userID, binID, trashType, trashWeight);
>>>>>>> main
    }
}
