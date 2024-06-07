package dev.skycast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ForecastData {
    private LocalDateTime dateTime;
    private double temperature;
    private double feelsLike;
    private String weather;
    private int humidity;
    private double windSpeed;
    private double windGusts;

    public ForecastData(long dt, double temperature, double feelsLike, String weather, int humidity, double windSpeed, double windGusts) {
        this.dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.systemDefault());
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.weather = weather;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windGusts = windGusts;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public String getWeather() {
        return weather;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getwindSpeed() {
        return windSpeed;
    }

    public double getWindGusts() {
        return windGusts;
    }

    public static List<ForecastData> getForecast(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        connection.disconnect();

        JSONObject json = new JSONObject(content.toString());
        JSONArray list = json.getJSONArray("list");

        List<ForecastData> forecasts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            JSONObject entry = list.getJSONObject(i);
            long dt = entry.getLong("dt");
            JSONObject main = entry.getJSONObject("main");
            double temperature = main.getDouble("temp");
            double feelsLike = main.getDouble("feels_like");
            int humidity = main.getInt("humidity");
            String weather = entry.getJSONArray("weather").getJSONObject(0).getString("description");
            JSONObject wind = entry.getJSONObject("wind");
            double windSpeed = wind.getDouble("speed");
            double windGusts = wind.optDouble("gust", 0);

            forecasts.add(new ForecastData(dt, temperature, feelsLike, weather, humidity, windSpeed, windGusts));
        }

        return forecasts;
    }
}
