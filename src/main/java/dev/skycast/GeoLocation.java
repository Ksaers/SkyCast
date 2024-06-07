package dev.skycast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeoLocation {

    private String publicIP;
    private String apiKey;

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

    public String getCityAndCountry() {
        JSONObject locationData = getGeoLocation(publicIP, apiKey);
        if (locationData != null) {
            String city = locationData.getString("city");
            String country = locationData.getString("country_name");
            return city + ", " + country;
        } else {
            System.out.println("Failed to retrieve location data.");
            return null;
        }
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
}
