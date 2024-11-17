package dev.skycast;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.awt.*;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScyCastController {

    // Ключ API OpenWeather
    private String weatherApiKey = "590bce9f3bea98fd82b8da05468fe5ba";
    // Ключ API GeoLocation
    private String geoApiKey = "05827ff89d914aecb05f1c75c1951c0c";

    @FXML
    private AnchorPane bgSkyCast;

    @FXML
    Label txtDate;

    @FXML
    Label txtDirection;

    @FXML
    Label txtFeels;

    @FXML
    private Label txtForecastDate1;

    @FXML
    private Label txtForecastDate2;

    @FXML
    private Label txtForecastDate3;

    @FXML
    private Label txtForecastDesc1;

    @FXML
    private Label txtForecastDesc2;

    @FXML
    private Label txtForecastDesc3;

    @FXML
    Label txtHumidity;

    @FXML
    private Label txtLocation;

    @FXML
    Label txtMinHigh;

    @FXML
    Label txtPressure;

    @FXML
    Label txtTemp;

    @FXML
    Label txtTime;

    @FXML
    Label txtWeather;

    @FXML
    Label txtWind;

    @FXML
    private TextField txtCity;


    @FXML
    private ListView<String> citySuggestionsList;

    private List<String> cities;

    @FXML
    public void handleCityWeatherUpdate() {
        String cityName = txtCity.getText();

        if (cityName != null && !cityName.isEmpty()) {
            updateWeatherData(cityName);

            setForecastData(cityName);
        }
    }

    @FXML
    public void handleCityWeatherUpdate(String cityName) {
        if (cityName != null && !cityName.isEmpty()) {
            updateWeatherData(cityName);
            setForecastData(cityName);
        }
    }


    @FXML
    void openURL(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Ksaers"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");

    public List<String> loadCitiesFromFile(String fileName) {
        List<String> cityList = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cityList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    public void initialize() {
        setupVideoBackground();
        updateTime();
        startLiveTimeUpdate();

        String citiesFileName = "src/main/resources/dev/skycast/cities.txt";
        cities = loadCitiesFromFile(citiesFileName);

        txtCity.textProperty().addListener((observable, oldValue, newValue) -> updateCitySuggestions(newValue));

        citySuggestionsList.setOnMouseClicked(this::handleCitySelection);

        String cityName = txtCity.getText();

        updateWeatherData(cityName);

        setForecastData(cityName);
    }


    private void updateCitySuggestions(String query) {
        List<String> filteredCities = new ArrayList<>();
        for (String city : cities) {
            if (city.toLowerCase().startsWith(query.toLowerCase()) && !query.isEmpty()) {
                filteredCities.add(city);
            }
        }

        citySuggestionsList.getItems().setAll(filteredCities);
        citySuggestionsList.setVisible(!filteredCities.isEmpty());
    }

    private void handleCitySelection(MouseEvent event) {
        String selectedCity = citySuggestionsList.getSelectionModel().getSelectedItem();
        if (selectedCity != null) {
            txtCity.setText(selectedCity);
            citySuggestionsList.setVisible(false);
            handleCityWeatherUpdate(selectedCity);
        }
    }

    private void setupVideoBackground() {
        try {
            String videoPath = "/dev/skycast/bgSkyCastRain.mp4";
            Media media = new Media(getClass().getResource(videoPath).toExternalForm());

            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.fitWidthProperty().bind(bgSkyCast.widthProperty());
            mediaView.fitHeightProperty().bind(bgSkyCast.heightProperty());

            bgSkyCast.getChildren().add(mediaView);

            mediaView.toBack();
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Ошибка: Видео файл не найден. Проверьте путь к файлу.");
        }
    }

    void startLiveTimeUpdate() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> updateTime()),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    void updateTime() {
        txtTime.setText(LocalDateTime.now().format(timeFormatter));
        txtDate.setText(LocalDateTime.now().format(dateFormatter));
    }

    void updateWeatherData(String city) {
        double[] latLon;

        if (city == null || city.isEmpty()) {
            String publicIP = IPFetcher.getPublicIP();
            if (publicIP != null) {
                latLon = getLatitudeLongitude(publicIP);
            } else {
                System.out.println("Не удалось получить публичный IP-адрес.");
                return;
            }
        } else {
            latLon = GeoLocation.getLatitudeLongitudeByCity(city);
            if (latLon == null) {
                System.out.println("Не удалось получить данные для указанного города.");
                return;
            }
        }

        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + latLon[0] + "&lon=" + latLon[1] + "&units=metric&appid=" + weatherApiKey;

        try {
            WeatherData weatherData = WeatherData.fetchWeatherData(apiUrl);
            setWeatherData(weatherData, city);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    double[] getLatitudeLongitude(String publicIP) {
        GeoLocation geoLocation = new GeoLocation(publicIP, geoApiKey);
        return geoLocation.getLatitudeLongitude();
    }

    void setWeatherData(WeatherData weatherData, String publicIP) {

        txtHumidity.setText(weatherData.getHumidity() + "%");
        txtPressure.setText(weatherData.getPressure() + " гПа");
        txtWind.setText(weatherData.getWindSpeed() + " м/с");
        txtDirection.setText("Направление - " + weatherData.getWindDirection() + "° (" + getDirectionName(weatherData.getWindDirection()) + ")");

        String[] words = weatherData.getDescription().split("\\s+");
        StringBuilder weatherConditionBuilder = new StringBuilder();
        for (String word : words) {
            String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            weatherConditionBuilder.append(capitalizedWord).append(" ");
        }
        String weatherCondition = weatherConditionBuilder.toString().trim().replace(" ", "\n");


        txtLocation.setText(getCityAndCountry(publicIP));
        txtWeather.setText(weatherCondition);
        txtFeels.setText("Ощущается: " + weatherData.getFeelsLike() + "°C");
        txtTemp.setText(weatherData.getTemp() + "°C");
        txtMinHigh.setText("Мин - " + weatherData.getTempMin() + "°C / Макс - " + weatherData.getTempMax() + "°C");
        txtHumidity.setText(weatherData.getHumidity() + "%");
        txtPressure.setText(weatherData.getPressure() + " гПа");
        txtWind.setText(weatherData.getWindSpeed() + " м/с");
        txtDirection.setText("Направление - " + weatherData.getWindDirection() + "° (" + getDirectionName(weatherData.getWindDirection()) + ")");
    }


    String getCityAndCountry(String publicIP) {
        GeoLocation geoLocation = new GeoLocation(publicIP, geoApiKey);
        return geoLocation.getCityAndCountry(publicIP);
    }

    String getDirectionName(int windDirection) {
        String[] directions = {"Север", "Северо-восток", "Восток", "Юго-восток", "Юг", "Юго-запад", "Запад", "Северо-запад"};
        int index = (int) Math.round((windDirection % 360) / 45.0);
        return directions[index % 8];
    }

    void setForecastData(String city) {

        String apiUrl;

        if (city != null && !city.isEmpty()) {
            double[] coordinates = GeoLocation.getLatitudeLongitudeByCity(city);
            if (coordinates == null) {
                System.out.println("Не удалось получить координаты для города.");
                return;
            }

            double lat = coordinates[0];
            double lon = coordinates[1];

            apiUrl = String.format("https://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&units=metric&appid=%s", lat, lon, weatherApiKey);
        } else {
            String publicIP = IPFetcher.getPublicIP();
            if (publicIP == null) {
                System.out.println("Не удалось получить IP-адрес.");
                return;
            }

            GeoLocation geoLocation = new GeoLocation(publicIP, geoApiKey);
            double[] coordinates = geoLocation.getLatitudeLongitude();

            if (coordinates == null) {
                System.out.println("Не удалось получить координаты по геолокации.");
                return;
            }

            double lat = coordinates[0];
            double lon = coordinates[1];

            apiUrl = String.format("https://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&units=metric&appid=%s", lat, lon, weatherApiKey);
        }

        try {
            List<ForecastData> forecasts = ForecastData.getForecast(apiUrl);

            if (forecasts.size() >= 3) {
                ForecastData forecast1 = forecasts.get(0);
                ForecastData forecast2 = forecasts.get(1);
                ForecastData forecast3 = forecasts.get(2);


                txtForecastDate1.setText(forecast1.getDateTime().format(DateTimeFormatter.ofPattern("dd MMM, yyyy\nHH:mm")));
                txtForecastDesc1.setText(String.format("Температура: %.2f°C\nОщущается как: %.2f°C\nПогода: %s\nВлажность: %d%%\nВетер: %.2f м/с, порывы до %.2f м/с",
                        forecast1.getTemperature(), forecast1.getFeelsLike(), forecast1.getWeather(), forecast1.getHumidity(), forecast1.getwindSpeed(), forecast1.getWindGusts()));

                txtForecastDate2.setText(forecast2.getDateTime().format(DateTimeFormatter.ofPattern("dd MMM, yyyy\nHH:mm")));
                txtForecastDesc2.setText(String.format("Температура: %.2f°C\nОщущается: %.2f°C\nПогода: %s\nВлажность: %d%%\nВетер: %.2f м/с, порывы до %.2f м/с",
                        forecast2.getTemperature(), forecast2.getFeelsLike(), forecast2.getWeather(), forecast2.getHumidity(), forecast2.getwindSpeed(), forecast2.getWindGusts()));

                txtForecastDate3.setText(forecast3.getDateTime().format(DateTimeFormatter.ofPattern("dd MMM, yyyy\nHH:mm")));
                txtForecastDesc3.setText(String.format("Температура: %.2f°C\nОщущается: %.2f°C\nПогода: %s\nВлажность: %d%%\nВетер: %.2f м/с, порывы до %.2f м/с",
                        forecast3.getTemperature(), forecast3.getFeelsLike(), forecast3.getWeather(), forecast3.getHumidity(), forecast3.getwindSpeed(), forecast3.getWindGusts()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
