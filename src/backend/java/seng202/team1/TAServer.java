package seng202.team1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TAServer {

    static void main(String[] args) {
        SpringApplication app = new SpringApplication(TAServer.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);
    }

}
