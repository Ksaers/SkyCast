package dev.skycast;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SkyCastApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SkyCastApplication.class.getResource("weatherpulse-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());


        stage.getIcons().add(new Image(SkyCastApplication.class.getResourceAsStream("icon.png")));


        stage.setResizable(false);

        stage.setTitle("SkyCast (Ваш ежедневный погодный компаньон)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        if (isNetworkAvailable()) {
            launch(args);
        } else {
            showAlertAndExit();
        }
    }


    private static void showAlertAndExit() {

        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Network Error");
            alert.setHeaderText("No Network Connection");
            alert.setContentText("The application requires an internet connection to work. Please check your connection and try again.");
            alert.showAndWait();
            Platform.exit();
            System.exit(0);
        });
    }


    private static boolean isNetworkAvailable() {
        try {
            URL url = new URL("https://github.com/Ksaers");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("HEAD");
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            return (200 <= responseCode && responseCode < 400);
        } catch (IOException e) {
            return false;
        }
    }
}
