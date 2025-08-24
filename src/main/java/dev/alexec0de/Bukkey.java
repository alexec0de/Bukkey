package dev.alexec0de;

import com.google.gson.Gson;
import dev.alexec0de.data.KeyRequest;
import dev.alexec0de.utils.PublicIPUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;



public class Bukkey {

    private final String licenseKey;
    private final String pluginId;
    private final Logger logger;

    public Bukkey(String licenseKey, String pluginId, Logger logger) {
        this.licenseKey = licenseKey;
        this.pluginId = pluginId;
        this.logger = logger;
    }


    public boolean check() {
        final Gson gson = new Gson();
        final String publicIP = PublicIPUtils.getPublicIP();
        if (publicIP == null) {
            if (logger != null) {
                logger.warning("Public IP is null!! License not verified!");
            } else {
                System.out.println("Public IP is null!! License not verified!");
            }
            return false;
        }
        final KeyRequest keyRequest = new KeyRequest(publicIP, licenseKey, pluginId);
        final String json = gson.toJson(keyRequest);
        try {
            final URL url = new URL("https://api.bukkey.ru/check/key");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            try (final OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            final StringBuilder responseBuilder = getStringBuilder(connection);

            final String response = responseBuilder.toString();
            if (response.contains("\"success\":true")) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            if (logger != null) {
                logger.severe("License server error:  " + e.getMessage() + "!! License not verified!");
            } else {
                System.out.println("License server error:  " + e.getMessage() + "!! License not verified!");
            }
            return false;
        }
    }

    private @NonNull StringBuilder getStringBuilder(HttpURLConnection connection) throws IOException {
        final int statusCode = connection.getResponseCode();
        final InputStream inputStream = (statusCode >= 200 && statusCode < 300)
                ? connection.getInputStream()
                : connection.getErrorStream();

        final StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
        }
        return responseBuilder;
    }
}