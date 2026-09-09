package seng202.team1.api.trashdb;

import seng202.team1.data.bin.BinFullness;
import seng202.team1.data.report.ReportType;
import seng202.team1.data.trashlog.TrashType;

import java.time.Instant;

public record BinReport (

        Integer _id,
        Instant created,
        Integer updated,

        Integer user_id,
        Integer bin_id,
        ReportType type,
        String issue,
        BinFullness fullness
){}
