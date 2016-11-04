package net.iterable.core.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arun on 3/8/16.
 */
public final class ConfigProvider {

    private static final Logger logger =
            LoggerFactory.getLogger(ConfigProvider.class);
    private Config configFromFile;
    private Config runtimeConfig;
    private static String ORIGIN_DESCRIPTION = "Consul";
    private static final ConfigProvider THIS = new ConfigProvider();
    private String serviceName;

    private ConfigProvider() {
        this.configFromFile = ConfigFactory.load();
        runtimeConfig = configFromFile;
    }

    public static ConfigProvider getInstance() {
        return THIS;
    }

    public void initializeServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Config getConfig()
    {
       return runtimeConfig;
    }

    public Config update(Map<String, String> configUpdate) {

        Config newConfig = ConfigFactory.parseMap(normalizedMap(configUpdate), ORIGIN_DESCRIPTION);
        runtimeConfig = newConfig.withFallback(configFromFile);
        logger.debug("Contents of config {}", runtimeConfig.toString());
        return runtimeConfig;

    }

    public Map<String, Object> normalizedMap(Map<String, String> configUpdate) {
        final Map<String, Object> normalizedMap = new HashMap();
        configUpdate.entrySet().stream()
                .forEach(entry -> {
                    String encodedString = entry.getValue();
                    int firstIndexOfKeySpace = entry.getKey().lastIndexOf("/")+1;
                    String[] configContextPath = entry.getKey().split("/");
                    if(configContextPath.length < 2
                            || !(configContextPath[0].equals(serviceName))) {
                        logger.debug("Not a valid config update for {} - was notified with  {}",
                                serviceName, configUpdate.toString());
                        return;
                    }

                    if(entry.getKey().length() == firstIndexOfKeySpace
                            || entry.getKey().length() < firstIndexOfKeySpace) {
                        return;
                    }
                    String decodedString = new String(Base64.getDecoder().decode(encodedString));
                    String keyPath = entry.getKey().substring(firstIndexOfKeySpace);
                    normalizedMap.put(keyPath, decodedString);
                    logger.debug("Key path {} getting an update value of {}", keyPath, decodedString);
                });
        return normalizedMap;
    }

}
