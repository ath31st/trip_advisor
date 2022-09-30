package home.sweethome.fussdb.service;

import home.sweethome.fussdb.entity.Weather;
import home.sweethome.fussdb.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;

    @Value("${fussdb.yandexapikey}")
    private String yandexApiKey;
    private static final String GET_FORECAST_BY_GEOCODE = "https://www.7timer.info/bin/api.pl?product=civillight&output=json&lon=%s&lat=%s";
    private final static String USER_AGENT = "Mozilla/5.0";

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public List<Weather> getForecast(float lon, float lat) throws IOException {
        String forecastUrl = String.format(GET_FORECAST_BY_GEOCODE, lon, lat);
        List<Weather> list;

        URL url = new URL(forecastUrl);
        HttpURLConnection connection = getConnection(url);
        String rawJson = getRawDataFromConnection(connection);

        list = null;

        connection.disconnect();
        return list;
    }

    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == 404 | responseCode == 500) {
            throw new IllegalArgumentException();
        }
        return connection;
    }
    private String getRawDataFromConnection(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();
        return response.toString();
    }
}
