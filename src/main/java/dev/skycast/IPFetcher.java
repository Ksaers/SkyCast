package dev.skycast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;

public class IPFetcher {

    public static String getPublicIP() {
        String urlString = "http://checkip.amazonaws.com/";
        URL url;
        try {
            url = new URL(urlString);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
