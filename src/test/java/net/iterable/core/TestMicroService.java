package net.iterable.core;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * Created by arun on 2/18/16.
 */
public class TestMicroService extends Microservice {


    @Override
    public String serviceName() {
        return "TestService";
    }

    @Override
    public int servicePort() {
        return getConfigProvider().getConfig().getInt("discovery.advertised.port");
    }

    @Override
    public Set<String> resourcePackages() {
        return Sets.newHashSet("net.iterable.core");
    }

    @Override
    public List<HealthCheck> healthCheckList() {
        return null;
    }

    public static void main(String[] args) {
        TestMicroService service = new TestMicroService();
        service.start();
    }

}

