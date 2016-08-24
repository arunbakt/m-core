package net.iterable.core;


import com.google.common.net.HostAndPort;
import net.iterable.core.config.ConfigProvider;
import net.iterable.core.discovery.consul.ConsulLifeCycleListener;
import com.codahale.metrics.health.HealthCheck;
import com.orbitz.consul.Consul;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by arun on 2/17/16.
 */
public abstract class Microservice<T> {

    private static final ConfigProvider configProvider = new ConfigProvider();

    private static Consul consul;

    public abstract String serviceName();

    public abstract int servicePort();

    public abstract Set<String> resourcePackages();

    public abstract List<HealthCheck> healthCheckList();

    public void start() {

        boolean consulInitialized=false;
        int tryCount = 0;
        Throwable initializationException = null;
        while(!consulInitialized && tryCount < 5) {
            try{
                tryCount++;
                initializeConsul();
                consulInitialized = true;
            } catch(Throwable t) {
                initializationException = t;
                System.out.println(t.getMessage());
                try {
                    System.out.println("Going to sleep..");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted from sleep...");
                    e.printStackTrace();
                }
            }
        }
        if(!consulInitialized) {
            throw new RuntimeException("Couldn't initialize consul agent",initializationException);
        }

        int port = servicePort();
        final Server server = new Server(port);
        ServletContextHandler context =
                new ServletContextHandler(server, "/",
                        ServletContextHandler.NO_SESSIONS);

        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/"+serviceName()+"/*");
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter(ServerProperties.PROVIDER_PACKAGES, resourcePackageString());
        jerseyServlet.addLifeCycleListener(createConsulLifeCycleListener());

        try {
            server.setStopAtShutdown(true);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.stop();
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

    }

    public static ConfigProvider getConfigProvider() {
        return configProvider;
    }

    private void initializeConsul() {

        String host = configProvider.getConfig().getString("discovery.consul.host");
        int port = configProvider.getConfig().getInt("discovery.consul.port");
        Consul.Builder builder = Consul.builder();
        System.out.println("host and port " +host+":"+port);
        consul = builder.withHostAndPort(HostAndPort.fromParts(host, port)).build();

    }

    private ConsulLifeCycleListener createConsulLifeCycleListener() {

        ConsulLifeCycleListener consulLifeCycleListener = new ConsulLifeCycleListener(this, consul, configProvider.getConfig());
        return consulLifeCycleListener;

    }

    private String resourcePackageString() {
        return resourcePackages().stream()
                .collect(Collectors.joining(";"));
    }

}
