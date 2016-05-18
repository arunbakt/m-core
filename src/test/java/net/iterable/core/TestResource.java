package net.iterable.core;

import net.iterable.core.resources.HealthCheck;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by arun on 2/19/16.
 */
@Path("/")
public class TestResource {

    @GET
    public String hello() {

        com.typesafe.config.Config config = TestMicroService.getConfigProvider().getConfig();
        String greeting = config.getString("chegg.greeting");
        return greeting;

    }

    @Path("/health")
    public  HealthCheck healthCheck() {
        return new HealthCheck();
    }

}
