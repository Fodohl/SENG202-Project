package seng202.team1.types.trashdb;

import java.sql.Timestamp;
import java.time.Instant;

public record CommunityCleanliness (

        int id,
        int year,
        int month,
        int log_count,
        Instant updatedAt

)
{
    public CommunityCleanliness withNewCount(int count) {
        return new CommunityCleanliness(id, year, month, count, updatedAt);
    }
}
