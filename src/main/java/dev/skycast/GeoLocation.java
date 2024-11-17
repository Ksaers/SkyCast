package dev.skycast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeoLocation {

    private String publicIP;
    private static String apiKey;


    public static String weatherApiKey = "590bce9f3bea98fd82b8da05468fe5ba";

    public GeoLocation(String publicIP, String apiKey) {
        this.publicIP = publicIP;
        this.apiKey = apiKey;
    }

    public double[] getLatitudeLongitude() {
        JSONObject locationData = getGeoLocation(publicIP, apiKey);
        if (locationData != null) {
            double latitude = locationData.getDouble("latitude");
            double longitude = locationData.getDouble("longitude");
            return new double[]{latitude, longitude};
        } else {
            System.out.println("Failed to retrieve location data.");
            return null;
        }
    }

    public String getCityAndCountry(String city) {
        String result = getCityAndCountryFromWeatherAPI(city);

        if (result != null) {
            return result;
        } else {
            String publicIP = IPFetcher.getPublicIP();
            if (publicIP != null) {
                return getCityAndCountryByIP(publicIP);
            }
        }
        return null;
    }


    private JSONObject getGeoLocation(String ip, String apiKey) {
        try {
            String apiUrl = "https://api.ipgeolocation.io/ipgeo?apiKey=" + apiKey + "&ip=" + ip;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return new JSONObject(response.toString());
            } else {
                System.out.println("Error: HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCityAndCountryByIP(String ip) {
        try {
            String apiUrl = "https://api.ipgeolocation.io/ipgeo?apiKey=" + apiKey + "&ip=" + ip;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                String cityName = jsonResponse.getString("city");
                String country = jsonResponse.getString("country_name");

                return cityName + ", " + country;
            } else {
                System.out.println("Error: Unable to retrieve city data from IP Geolocation API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getCityAndCountryFromWeatherAPI(String city) {
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + weatherApiKey;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                String cityName = jsonResponse.getString("name");
                String country = jsonResponse.getJSONObject("sys").getString("country");

                return cityName + ", " + country;
            } else {
                System.out.println("Error: Unable to retrieve city data from Weather API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static JSONObject getGeoLocationByCity(String city, String apiKey) {
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return new JSONObject(response.toString());
            } else {
                System.out.println("Error: HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] getLatitudeLongitudeByCity(String city) {
        JSONObject locationData = getGeoLocationByCity(city, weatherApiKey);
        if (locationData != null) {
            JSONObject coord = locationData.getJSONObject("coord");
            double latitude = coord.getDouble("lat");
            double longitude = coord.getDouble("lon");
            return new double[]{latitude, longitude};
        } else {
            System.out.println("Failed to retrieve location data for city: " + city);
            return null;
        }
    }



}
