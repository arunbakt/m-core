package net.iterable.core.resources;

import javax.ws.rs.GET;

/**
 * Created by arun on 3/9/16.
 */
public class HealthCheck {

    @GET
    public String health() {

        return "health ok";

    }


}
