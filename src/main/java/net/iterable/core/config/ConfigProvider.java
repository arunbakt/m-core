package net.iterable.core.config;

import com.google.common.base.Functions;
import com.typesafe.config.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by arun on 3/8/16.
 */
public final class ConfigProvider {

    private Config configFromFile;
    private Config runtimeConfig;

    private final static ConfigProvider THIS  = new ConfigProvider();

    private ConfigProvider () {
        this.configFromFile = ConfigFactory.load();
        runtimeConfig = configFromFile;
    }

    public static ConfigProvider getInstance() {
        return THIS;
    }

    public Config getConfig()
    {
       return runtimeConfig;
    }

    public Config update(Map<String, String> configUpdate) {

        String ORIGIN_DESCRIPTION = "Consul";
        Config newConfig = ConfigFactory.parseMap(configUpdate);
        Set<Map.Entry<String, ConfigValue>> entries = newConfig.entrySet();
        Map<String, ConfigValue> normalizedEntries = entries.stream()
                .map(entry -> {
                    String encodedString = (String) entry.getValue().unwrapped();
                    String decodedString = new String(Base64.getDecoder().decode(encodedString));
                    ConfigValue decodedValue = ConfigValueFactory.fromAnyRef(decodedString, ORIGIN_DESCRIPTION);
                    String normalizedKey = entry.getKey().replaceAll("/", ".");
                    return new AbstractMap.SimpleImmutableEntry<String, ConfigValue>(normalizedKey, decodedValue);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        newConfig = ConfigFactory.parseMap(normalizedEntries);
        runtimeConfig = newConfig.withFallback(configFromFile);
        return runtimeConfig;

    }


}
