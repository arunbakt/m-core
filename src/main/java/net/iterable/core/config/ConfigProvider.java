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
    private static String ORIGIN_DESCRIPTION = "Consul";

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

        Config newConfig = ConfigFactory.parseMap(normalizedMap(configUpdate), ORIGIN_DESCRIPTION);
        runtimeConfig = newConfig.withFallback(configFromFile);
        logger.debug("Contents of config {}", newConfig.toString());
        return runtimeConfig;

    }

    public Map<String, Object> normalizedMap(Map<String, String> configUpdate) {
        final Map<String, Object> normalizedMap = new HashMap();
        configUpdate.entrySet().stream()
                .forEach(entry -> {
                    String encodedString = entry.getValue();
                    String decodedString = new String(Base64.getDecoder().decode(encodedString));

                    String[] keyTokens = entry.getKey().split("/");
                    String previousKey = null;
                    Map<String, Object> parent = null;
                    for (String token : keyTokens) {
                        if (previousKey == null) {
                            if (!normalizedMap.containsKey(token)) {
                                normalizedMap.put(token, null);
                            }
                            parent = normalizedMap;
                        } else {
                            Map<String, Object>  child = (Map<String, Object>) parent.get(previousKey);
                            if(child == null) {
                                child = new HashMap();
                                parent.put(previousKey, child);
                            }
                            if(!child.containsKey(token))
                                child.put(token, null);
                            parent = child;
                        }
                        previousKey = token;
                    }
                    parent.put(previousKey, decodedString);
                });
        return normalizedMap;
    }

}
