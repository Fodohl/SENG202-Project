package seng202.team1.types.trashdb;

import java.util.List;

public record TrashDBCleanlinessResponse (

        List<CommunityCleanliness> counts

) { }
