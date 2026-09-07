package seng202.team1.api.photon;

//      {
//          "coordinates": [
//            13.438596,
//            52.519854
//          ],
//          "type": "Point"
//        },
//        "properties": {
//          "city": "Berlin",
//          "country": "Germany",
//          "name": "Berlin"
//        }
//      }

import java.util.List;

public record PhotonLocationResponse (
        String type,
        List<Feature> features
){}

record Feature (
        String type,
        Geometry geometry,
        Properties properties
) {}

record Geometry (
        List<Double> coordinates,
        String type
) {}

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

record Properties (
        String type,
        String housenumber,
        String street,
        String locality,
        String district,
        String city,
        String county,
        String state,
        String country,
        String postcode,
        String countrycode,
        String name
) {}



