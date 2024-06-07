package dev.skycast;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.awt.*;
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
    private String geoApiKey = "99407ebef7d84b7486cdb5ee1d1340b7";

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
    void openURL(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Ksaers"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");

    public void initialize() {
        setupVideoBackground();
        updateTime();
        startLiveTimeUpdate();
        updateWeatherData();
        setForecastData();
    }

    private void setupVideoBackground() {
        try {
            // Загрузка видеофайла
            String videoPath = "/dev/skycast/bgSkyCastRain.mp4";
            Media media = new Media(getClass().getResource(videoPath).toExternalForm());

            // Создание MediaPlayer
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Зацикливание видео

            // Создание MediaView для отображения видео
            MediaView mediaView = new MediaView(mediaPlayer);

            // Установка размера MediaView для соответствия размерам AnchorPane
            mediaView.fitWidthProperty().bind(bgSkyCast.widthProperty());
            mediaView.fitHeightProperty().bind(bgSkyCast.heightProperty());

            // Добавление MediaView в AnchorPane
            bgSkyCast.getChildren().add(mediaView);

            // Отправка MediaView на задний план
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
        txtTime.setText(LocalDateTime.now().format(timeFormatter)); // Обновление времени
        txtDate.setText(LocalDateTime.now().format(dateFormatter)); // Обновление даты
    }

    void updateWeatherData() {

        // Получение публичного IP-адреса
        String publicIP = IPFetcher.getPublicIP();
        if (publicIP != null) {
            // Если публичный IP-адрес успешно получен, получить широту и долготу
            double[] latLon = getLatitudeLongitude(publicIP);
            if (latLon != null) {
                // Получение данных о погоде
                String apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + latLon[0] + "&lon=" + latLon[1] + "&units=metric&appid=" + weatherApiKey;

                try {
                    WeatherData weatherData = WeatherData.fetchWeatherData(apiUrl);
                    setWeatherData(weatherData, publicIP);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Не удалось получить широту и долготу.");
            }
        } else {
            System.out.println("Не удалось получить публичный IP-адрес.");
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
        txtDirection.setText("Направление - " + weatherData.getWindDirection() + "°");


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
        return geoLocation.getCityAndCountry();
    }

    String getDirectionName(int windDirection) {
        String[] directions = {"Север", "Северо-восток", "Восток", "Юго-восток", "Юг", "Юго-запад", "Запад", "Северо-запад"};
        int index = (int) Math.round((windDirection % 360) / 45.0);
        return directions[index % 8];
    }

    void setForecastData() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/forecast?lat=-11.8092&lon=51.509865&units=metric&appid=" + weatherApiKey;

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
