package home.sweethome.fussdb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import home.sweethome.fussdb.entity.Location;
import home.sweethome.fussdb.entity.Weather;
import home.sweethome.fussdb.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static home.sweethome.fussdb.util.ConnectionUtil.getConnection;
import static home.sweethome.fussdb.util.ConnectionUtil.getRawDataFromConnection;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private static final String GET_FORECAST_BY_GEOCODE = "https://www.7timer.info/bin/api.pl?product=civillight&output=json&lon=%s&lat=%s";
    private final static String USER_AGENT = "Mozilla/5.0";

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public List<Weather> getForecast(Location location) throws IOException {
        String forecastUrl = String.format(GET_FORECAST_BY_GEOCODE, location.getLatitude(), location.getLongitude());
        List<Weather> list;

        URL url = new URL(forecastUrl);
        HttpURLConnection connection = getConnection(url, USER_AGENT);
        String rawJson = getRawDataFromConnection(connection);

        list = convertRawJsonToListWeather(rawJson);

        connection.disconnect();
        return list;
    }

    private static List<Weather> convertRawJsonToListWeather(String rawJson) throws JsonProcessingException {
        List<Weather> weatherList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode arrNode = new ObjectMapper()
                .readTree(rawJson)
                .get("dataseries");
        if (arrNode.isArray()) {
            for (JsonNode objNode : arrNode) {
                Weather weather = mapper.convertValue(objNode, Weather.class);
                weather.setMaxTemp(objNode.findPath("temp2m").get("max").asInt());
                weather.setMinTemp(objNode.findPath("temp2m").get("min").asInt());
                weatherList.add(weather);
            }
        }
        return weatherList;
    }

}
