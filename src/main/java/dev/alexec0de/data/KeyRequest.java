package dev.alexec0de.data;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * class for converting to JSON
 */
@AllArgsConstructor
@Getter
public class KeyRequest  {
    private final String ip;
    private final String key;
    private final String pluginId;
}
