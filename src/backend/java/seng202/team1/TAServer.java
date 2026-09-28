package seng202.team1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import seng202.team1.database.DAO.BinDAO;
import seng202.team1.database.DAO.UserDAO;
import seng202.team1.database.DatabaseManager;
import seng202.team1.data.bin.BinDataset;

import java.util.Arrays;

@SpringBootApplication
public class TAServer {


    //@TODO: c queue upload trash
    //@TODO: c queue upload status
    //@TODO: s worked calculate fullness
    //@TODO: s worker calculate leaderboard
    //@TODO: change password / details
    //@TODO: delete account
    //@TODO: download my stuff from db

    public static DatabaseManager db = new DatabaseManager(null);
    public static UserDAO userTable = new UserDAO(db);
    public static BinDAO binTable = new BinDAO(db);

    private static void init(){

        // Create sql
        db.resetDB();

        // Propagate bins
        BinDataset bins = new BinDataset();
        bins.readGeoJSON();
        binTable.addBatch(bins.getBinDataset());

    }

    public static void main(String[] args) {

        if(Arrays.stream(args).toList().contains("--init"))
            init();

        SpringApplication app = new SpringApplication(TAServer.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);

    }
}
