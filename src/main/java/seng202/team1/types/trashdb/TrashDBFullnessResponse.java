package seng202.team1.types.trashdb;

import java.util.Map;

public record TrashDBFullnessResponse(

    Map<Integer, Integer> bins

){ }
