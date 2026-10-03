package seng202.team1.types.photon;

/**
 * Photon Type: The properties of a geometric location
 *
 * @param type The type of properties
 * @param housenumber The street number
 * @param street The street address
 * @param locality The local area name
 * @param district N/A in NZ
 * @param city The city the place is in
 * @param county The region the city is in
 * @param state The state the city is in
 * @param country The country the place is in
 * @param postcode The postcode of the local area
 * @param countrycode The ISO country ID
 * @param name The name of the area if not street addressable
 */
public record Properties(
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
) {
}
