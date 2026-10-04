package seng202.team1.data.export;

import seng202.team1.data.User;
import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.status.ReportType;
import seng202.team1.data.status.StatusReport;
import seng202.team1.data.trashlog.TrashLog;
import seng202.team1.data.trashlog.TrashType;

import java.time.Instant;
import java.time.ZoneId;


public record ExportEntry(
        Instant time,
        String source,
        int binId,
        Integer userId,
        // Status report only
        BinFullness fullness,
        ReportType issue,
        String description,
        // Trash log only
        TrashType trashType,
        Integer trashWeight
) {
    public static ExportEntry from(StatusReport r) {
        User user = r.getSubmittedBy();
        return new ExportEntry(
                r.getSubmittedAt().atZone(ZoneId.systemDefault()).toInstant(),
                "Status Report",
                r.getBinID(),
                user == null ? null : user.getUserID(),
                r.getFullness(), r.getIssue(), r.getDescription(),
                null, null);
    }

    public static ExportEntry from(TrashLog t) {
        return new ExportEntry(
                t.disposedAt(),
                "Trash Log",
                t.binID(),
                t.userID(),
                null, null, null,
                t.trashType(), t.trashWeight());
    }
}