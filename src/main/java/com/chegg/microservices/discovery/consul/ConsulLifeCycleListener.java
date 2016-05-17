package com.chegg.microservices.discovery.consul;

import com.chegg.microservices.Microservice;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.Registration;
import com.typesafe.config.Config;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * Created by arun on 3/7/16.
 */
public class ConsulLifeCycleListener extends AbstractLifeCycle.AbstractLifeCycleListener {

    private Consul consul;
    private Config config;
    private Microservice microservice;
    private String serviceId;

    private static final Logger logger =
            LoggerFactory.getLogger(ConsulLifeCycleListener.class);

    public ConsulLifeCycleListener(Microservice microservice, Consul consul, Config config) {

        this.consul = consul;
        this.config = config;
        this.microservice = microservice;
        this.serviceId = String.format("%s-%s", microservice.serviceName(), UUID.randomUUID().toString());

    }

    @Override
    public void lifeCycleStarted(LifeCycle event) {

        String host = getHostAddress();
        int port = config.getInt("discovery.advertised.port");
        String name = microservice.serviceName();

        try {
            URL healthUrl = new URL("http", host, port, "/"+microservice.serviceName()+"/health");
            Registration registration = new Registration();

            registration.setAddress(host);
            registration.setPort(port);
            registration.setId(serviceId);
            registration.setName(name);

            Registration.Check check = new Registration.Check();

            check.setHttp(healthUrl.toExternalForm());
            check.setInterval("5s");

            registration.setCheck(check);

            consul.agentClient().register(registration);

           // consul.keyValueClient().

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void lifeCycleStopping(LifeCycle event) {
        System.out.println("Stopping");
        consul.agentClient().deregister(serviceId);
    }


    private String getHostAddress() {

        try {
            // TODO: The following IP address detection might need some work when deploying
            // to ECS/Docker environment potentially
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            String ERROR_MESSAGE = "Cannot determine the IP address of the host";
            logger.error(ERROR_MESSAGE, e);
            throw new RuntimeException(ERROR_MESSAGE, e);
        }

    }

}
