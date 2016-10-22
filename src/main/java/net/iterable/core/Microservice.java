package net.iterable.core;


import com.google.common.collect.Sets;
import com.google.common.net.HostAndPort;
import net.iterable.core.config.ConfigProvider;
import net.iterable.core.discovery.consul.ConsulLifeCycleListener;
import com.codahale.metrics.health.HealthCheck;
import com.orbitz.consul.Consul;
import net.iterable.core.resources.ResourceConstants;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by arun on 2/17/16.
 */
public abstract class Microservice {

    private static final ConfigProvider configProvider = ConfigProvider.getInstance();

    private static final Logger logger =
            LoggerFactory.getLogger(ConsulLifeCycleListener.class);

    private static Consul consul;

    public abstract String serviceName();

    public abstract int servicePort();

    public abstract Set<String> resourcePackages();

    public abstract List<HealthCheck> healthCheckList();

    public void start() {

        logger.info("Starting {} microservice..", this.serviceName());

        int port = servicePort();
        final Server server = new Server(port);
        ServletContextHandler context =
                new ServletContextHandler(server, "/",
                        ServletContextHandler.NO_SESSIONS);

        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/"+serviceName()+"/*");
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter(ServerProperties.PROVIDER_PACKAGES, resourcePackageString());

        if(configProvider.getConfig().getBoolean("discovery.consul.enable")) {
            initializeConsul();
            jerseyServlet.addLifeCycleListener(createConsulLifeCycleListener());
        }

        try {
            server.setStopAtShutdown(true);
            server.start();
            logger.info("jetty server started successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.stop();
                    logger.info("jetty server successfully shutdown");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("Microservice {} ready to take requests..", this.serviceName());

    }

    public static ConfigProvider getConfigProvider() {
        return configProvider;
    }

    private void initializeConsul() {


        boolean consulInitialized=false;
        int tryCount = 0;
        Throwable initializationException = null;
        while(!consulInitialized && tryCount < 5) {
            try{
                tryCount++;
                logger.info("Initializing consul to register {} microservice..", this.serviceName());
                String host = configProvider.getConfig().getString("discovery.consul.host");
                int port = configProvider.getConfig().getInt("discovery.consul.port");
                Consul.Builder builder = Consul.builder();
                System.out.println("host and port " +host+":"+port);
                consul = builder.withHostAndPort(HostAndPort.fromParts(host, port)).build();
                consulInitialized = true;
                logger.info("Consul initialized for {}", this.serviceName());
            } catch(Throwable t) {
                initializationException = t;
                try {
                    logger.warn("Initializing consul encountered error, going to sleep before retrying..", t);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    logger.error("Interrupted while waiting to retry consul initialization routine", e);
                    e.printStackTrace();
                }
            }
        }

        if(!consulInitialized) {
            throw new RuntimeException("Couldn't initialize consul agent",initializationException);
        }

    }

    private ConsulLifeCycleListener createConsulLifeCycleListener() {

        ConsulLifeCycleListener consulLifeCycleListener = new ConsulLifeCycleListener(this, consul, configProvider.getConfig());
        return consulLifeCycleListener;

    }

    private String resourcePackageString() {
        Set<String> resources = Sets.newHashSet(ResourceConstants.RESOURCE_PACKAGE_NAME);
        resources.addAll(resourcePackages());
        return resources.stream()
                .collect(Collectors.joining(";"));
    }

}
