package dev.skycast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherData {
    private double lon;
    private double lat;
    private String mainWeather;
    private String description;
    private double feelsLike;
    private double temp;
    private double tempMin;
    private double tempMax;
    private int humidity;
    private int pressure;
    private double windSpeed;
    private int windDirection;

    public WeatherData(double lon, double lat, String mainWeather, String description, double feelsLike, double temp,
                       double tempMin, double tempMax, int humidity, int pressure, double windSpeed, int windDirection) {
        this.lon = lon;
        this.lat = lat;
        this.mainWeather = mainWeather;
        this.description = description;
        this.feelsLike = feelsLike;
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.humidity = humidity;
        this.pressure = pressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }



    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getMainWeather() {
        return mainWeather;
    }

    public String getDescription() {
        return description;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public double getTemp() {
        return temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setMainWeather(String mainWeather) {
        this.mainWeather = mainWeather;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }



    @Override
    public String toString() {
        return "WeatherData{" +
                "lon=" + lon +
                "lat=" + lat +
                ", mainWeather='" + mainWeather + '\'' +
                ", description='" + description + '\'' +
                ", feelsLike=" + feelsLike +
                ", temp=" + temp +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                '}';
    }

    public static WeatherData fetchWeatherData(String apiUrl) throws Exception {
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

        JSONObject jsonObject = new JSONObject(content.toString());

        JSONObject coord = jsonObject.getJSONObject("coord");
        double lon = coord.getDouble("lon");
        double lat = coord.getDouble("lat");

        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weatherObj = weatherArray.getJSONObject(0);
        String mainWeather = weatherObj.getString("main");
        String description = weatherObj.getString("description");

        JSONObject main = jsonObject.getJSONObject("main");
        double feelsLike = main.getDouble("feels_like");
        double temp = main.getDouble("temp");
        double tempMin = main.getDouble("temp_min");
        double tempMax = main.getDouble("temp_max");
        int humidity = main.getInt("humidity");
        int pressure = main.getInt("pressure");

        JSONObject wind = jsonObject.getJSONObject("wind");
        double windSpeed = wind.getDouble("speed");
        int windDirection = wind.getInt("deg");

        return new WeatherData(lon, lat, mainWeather, description, feelsLike, temp, tempMin, tempMax, humidity, pressure, windSpeed, windDirection);
    }

}
