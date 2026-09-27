package seng202.team1.endpoints.api.v1;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import seng202.team1.api.BinApi;
import seng202.team1.taserver.model.GetBinsFullness200Response;
import seng202.team1.taserver.model.GetBinsFullness200ResponseBinsInner;

import java.math.BigDecimal;

@RestController
public class Bins implements BinApi{


    @Override
    public ResponseEntity<Object> getBins(String limit, @Nullable String page) {
        return null;
    }

    @Override
    public ResponseEntity<GetBinsFullness200Response> getBinsFullness() {

        GetBinsFullness200Response response = new GetBinsFullness200Response();

        GetBinsFullness200ResponseBinsInner bin = new GetBinsFullness200ResponseBinsInner();
        bin.fullness(BigDecimal.valueOf(0.1));
        bin.id(0L);

        response.addBinsItem(bin);

        return ResponseEntity.ok(response);
    }
}
