package dev.skycast;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherDataTest {

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

    @Test
    void testFetchWeatherData() {
        WeatherData weatherData = new WeatherData( lon,  lat,  mainWeather,  description,  feelsLike,  temp,
         tempMin,  tempMax,  humidity,  pressure,  windSpeed,  windDirection );

        double lon = Math.random() * 180 - 90;
        double lat = Math.random() * 360 - 180;
        String mainWeather = "Clear";
        String description = "clear sky";
        double feelsLike = Math.random() * 50 + 250;
        double temp = Math.random() * 50 + 250;
        double tempMin = Math.random() * 50 + 250;
        double tempMax = Math.random() * 50 + 250;
        int pressure = (int) (Math.random() * 1000 + 900);
        int humidity = (int) (Math.random() * 100);
        double windSpeed = Math.random() * 20;
        int windDirection = (int) (Math.random() * 360);

        weatherData.setLon(lon);
        weatherData.setLat(lat);
        weatherData.setMainWeather(mainWeather);
        weatherData.setDescription(description);
        weatherData.setFeelsLike(feelsLike);
        weatherData.setTemp(temp);
        weatherData.setTempMin(tempMin);
        weatherData.setTempMax(tempMax);
        weatherData.setPressure(pressure);
        weatherData.setHumidity(humidity);
        weatherData.setWindSpeed(windSpeed);
        weatherData.setWindDirection(windDirection);

        assertNotNull(weatherData);
        assertEquals(lon, weatherData.getLon());
        assertEquals(lat, weatherData.getLat());
        assertEquals(mainWeather, weatherData.getMainWeather());
        assertEquals(description, weatherData.getDescription());
        assertEquals(feelsLike, weatherData.getFeelsLike());
        assertEquals(temp, weatherData.getTemp());
        assertEquals(tempMin, weatherData.getTempMin());
        assertEquals(tempMax, weatherData.getTempMax());
        assertEquals(pressure, weatherData.getPressure());
        assertEquals(humidity, weatherData.getHumidity());
        assertEquals(windSpeed, weatherData.getWindSpeed());
        assertEquals(windDirection, weatherData.getWindDirection());

        System.out.println("Longitude: " + weatherData.getLon());
        System.out.println("Latitude: " + weatherData.getLat());
        System.out.println("Main Weather: " + weatherData.getMainWeather());
        System.out.println("Description: " + weatherData.getDescription());
        System.out.println("Feels Like: " + weatherData.getFeelsLike());
        System.out.println("Temperature: " + weatherData.getTemp());
        System.out.println("Minimum Temperature: " + weatherData.getTempMin());
        System.out.println("Maximum Temperature: " + weatherData.getTempMax());
        System.out.println("Humidity: " + weatherData.getHumidity());
        System.out.println("Pressure: " + weatherData.getPressure());
        System.out.println("Wind Speed: " + weatherData.getWindSpeed());
        System.out.println("Wind Direction: " + weatherData.getWindDirection());
    }
}
