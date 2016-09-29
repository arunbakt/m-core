package net.iterable.core.resources;

import net.iterable.core.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by arun on 9/12/16.
 */
@Path("/config")
public class Configuration {

    private static final Logger logger =
            LoggerFactory.getLogger(Configuration.class);


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get() {
        return ConfigProvider.getInstance().getConfig().atPath("cart-checkout-api").toString();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String update(@NotNull Map<String, String> newConfig) {
        logger.info("Update for configuration received with payload {}", newConfig);
        return ConfigProvider.getInstance().update(newConfig).toString();
    }

}

