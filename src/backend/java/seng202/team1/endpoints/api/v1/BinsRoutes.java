package seng202.team1.endpoints.api.v1;

import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import seng202.team1.api.BinApi;
import seng202.team1.taserver.model.GetBinsFullness200Response;
import seng202.team1.taserver.model.GetBinsFullness200ResponseBinsInner;
import seng202.team1.types.bin.Bin;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

import static seng202.team1.TAServer.binTable;

@RestController
public class BinsRoutes implements BinApi{


    @Override
    public ResponseEntity<Object> getBins(String limit, @Nullable String page) {
        return null;
    }

    @Override
    public ResponseEntity<GetBinsFullness200Response> getBinsFullness(@Nullable OffsetDateTime lastUpdated) {

        List<Bin> bins = binTable.getBinsAfter(lastUpdated == null ? null : lastUpdated.toInstant());

        GetBinsFullness200Response response = new GetBinsFullness200Response();
        bins.forEach(bin -> {
            GetBinsFullness200ResponseBinsInner binFullness = new GetBinsFullness200ResponseBinsInner();
            binFullness.id(Long.valueOf(bin.getID()));
            binFullness.fullness(BigDecimal.valueOf(bin.getFullness().ordinal()));

            response.addBinsItem(binFullness);
        });


        return ResponseEntity.ok(response);
    }
}
