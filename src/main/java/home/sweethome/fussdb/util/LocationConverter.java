package home.sweethome.fussdb.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import home.sweethome.fussdb.entity.Location;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static home.sweethome.fussdb.util.ConnectionUtil.getConnection;
import static home.sweethome.fussdb.util.ConnectionUtil.getRawDataFromConnection;

public class LocationConverter {
    @Value("${fussdb.yandexapikey}")
    private static String yandexApiKey;
    private static final String GET_GEOCODE_BY_ADDRESS = "https://geocode-maps.yandex.ru/1.x/?apikey=%s&results=1&format=json&geocode=%s";
    private final static String USER_AGENT = "Mozilla/5.0";

    public static Location StringAddressToGeocode(String address) throws IOException {
        String locationUrl = String.format(GET_GEOCODE_BY_ADDRESS, yandexApiKey, address);

        URL url = new URL(locationUrl);
        HttpURLConnection connection = getConnection(url, USER_AGENT);
        String rawJson = getRawDataFromConnection(connection);

        connection.disconnect();

        return convertRawJsonToLocation(rawJson);
    }

    private static Location convertRawJsonToLocation(String rawJson) throws JsonProcessingException {
        Location location = new Location();
        String[] latLon = new String[2];
        String name = "";
        String description = "";

        JsonNode arrNode = new ObjectMapper()
                .readTree(rawJson)
                .path("response")
                .path("GeoObjectCollection")
                .get("featureMember");

        if (arrNode.isArray()) {
            JsonNode jsonNode = arrNode.findPath("GeoObject");

            latLon = jsonNode.path("Point")
                    .get("pos")
                    .asText().split(" ");
            name = jsonNode.get("name").asText();
            description = jsonNode.get("description").asText();
        }
        location.setLatitude(Float.parseFloat(latLon[0]));
        location.setLongitude(Float.parseFloat(latLon[1]));
        location.setName(name + " " + description);

        return location;
    }

}
