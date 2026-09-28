package seng202.team1.endpoints.api.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import seng202.team1.api.DefaultApi;
import seng202.team1.taserver.model.Heartbeat200Response;

@RestController
public class DefaultRoutes implements DefaultApi {

    @Override
    public ResponseEntity<Heartbeat200Response> heartbeat() {

        Heartbeat200Response response = new Heartbeat200Response();
        response.health("UP");
        response.env("prod");
        response.motd("\"You dont have a backend server\" - Morgan");

        return ResponseEntity.ok(response);
    }
}
