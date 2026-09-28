package seng202.team1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import seng202.team1.database.DAO.UserDAO;
import seng202.team1.database.DatabaseManager;

@SpringBootApplication
public class TAServer {

    public static DatabaseManager db = new DatabaseManager(null);
    public static UserDAO userTable = new UserDAO(db);

    static void main(String[] args) {


        SpringApplication app = new SpringApplication(TAServer.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);

    }
}
