package seng202.team1.api.trashdb;

import seng202.team1.data.bin.BinCategory;
import seng202.team1.data.trashlog.TrashType;

import java.time.Instant;

public record TrashLog(

        Integer _id,
        Instant created,
        Integer updated,

        Integer user_id,
        Integer bin_id,
        TrashType type,
        Integer weight
) { }
