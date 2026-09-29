package seng202.team1.data.cache;

import java.time.Instant;

public class CacheEntry {

    private String key;
    private Instant expires;
    private String dataPath;

    public CacheEntry(){
        // constructor needed by GSON backend
    }

    public CacheEntry(String key, Instant expires, String dataPath){
        this.key = key;
        this.expires = expires;
        this.dataPath = dataPath;
    }


}
