package seng202.team1.types.trashdb;

import java.util.Map;

/**
 * TrashAppBackend Type: Map of bins and their fullness
 *
 * @param bins Map of bin id to bin fullness enum ordinal
 */
public record TrashDBFullnessResponse(

    Map<Integer, Integer> bins

){ }
