package net.iterable.core.resources;

import net.iterable.core.config.ConfigProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by arun on 9/12/16.
 */
@Path("/config")
public class Configuration {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String update(String newConfig) {
        return ConfigProvider.getInstance().update(newConfig).toString();
    }

}

