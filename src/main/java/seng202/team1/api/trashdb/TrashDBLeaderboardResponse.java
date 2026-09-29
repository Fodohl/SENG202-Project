package seng202.team1.api.trashdb;

import seng202.team1.api.photon.Feature;

import java.util.List;

public record TrashDBLeaderboardResponse (
    List<LeaderBoardEntry> entries,
    Integer nextPage
){}
