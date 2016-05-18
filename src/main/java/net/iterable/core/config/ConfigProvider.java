package net.iterable.core.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by arun on 3/8/16.
 */
public class ConfigProvider {

    private Config config;

    public ConfigProvider () {
        this.config = ConfigFactory.load();
    }

    public Config getConfig() {
       return config;
    }

}
