package seng202.team1.api.photon;
import java.util.List;


public record PhotonLocationResponse (
        String type,
        List<Feature> features
){}


// "type": "house",
//        "housenumber": "29",
//        "street": "Mt Pleasant Road",
//        "locality": "Saint Andrews Hill",
//        "district": "Mount Pleasant",
//        "city": "Christchurch",
//        "county": "Christchurch City",
//        "state": "Canterbury",
//        "country": "New Zealand / Aotearoa",
//        "postcode": "8241",
//        "countrycode": "NZ"
//@todo need extent?



