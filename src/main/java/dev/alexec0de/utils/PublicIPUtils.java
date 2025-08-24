package dev.alexec0de.utils;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@UtilityClass
public class PublicIPUtils {


    public String getPublicIP() {
        try {
            final URL url = new URL("https://ifconfig.me/ip");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return reader.readLine().trim();
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            return null;
        }
    }

}
