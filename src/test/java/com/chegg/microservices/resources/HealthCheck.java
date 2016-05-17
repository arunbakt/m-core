package com.chegg.microservices.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by arun on 3/9/16.
 */
public class HealthCheck {

    @GET
    public String health() {

        return "health ok";

    }


}
