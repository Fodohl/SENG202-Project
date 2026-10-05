package seng202.team1.data.export;

import seng202.team1.types.bin.BinFullness;
import seng202.team1.types.status.ReportType;
import seng202.team1.types.status.StatusReport;
import seng202.team1.types.trashlog.TrashLog;
import seng202.team1.types.trashlog.TrashType;

import java.time.Instant;
import java.time.ZoneId;

import static com.sun.scenario.effect.impl.state.AccessHelper.getState;

/**|
 * Record class representing user timeline export entries combining both status report and trash logs
 * @param time time of entry
 * @param source Status report or trash logs
 * @param binId where the report or trash log was made
 * @param fullness fullness for status report
 * @param issue issues reported status report
 * @param description description for status report
 * @param trashType the type of trash for trash logs
 * @param trashWeight weight of trash for trash logs
 */
public record ExportEntry(
        Instant time,
        String source,
        int binId,
        // Status report only
        BinFullness fullness,
        ReportType issue,
        String description,
        // Trash log only
        TrashType trashType,
        Double trashWeight
) {
    public static ExportEntry from(StatusReport r) {
        return new ExportEntry(
                r.getSubmittedAt().atZone(ZoneId.systemDefault()).toInstant(),
                "Status Report",
                r.getBinId(),
                r.getFullness(), r.getIssue(), r.getDescription(),
                null, null);
    }

    public static ExportEntry from(TrashLog t, BinFullness f) {
        return new ExportEntry(
                t.disposedAt(),
                "Trash Log",
                t.binID(),
                f, null, null,
                t.trashType(), t.trashWeight());
    }
}