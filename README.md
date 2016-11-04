# m-core

m-core (microservice core) is a minimalist shell/chasis that is meant to aid in developing API services modeled as a microservice. m-core by itself doesn't enforce or guide the developer to  develop a well defined microservice. The scope of definition is still the responsibilty of the developer as it should be. m-core just helps in avoiding having to repeat few things that developing a microservice requires.



## Features and Dependencies

Despite the earnest desire to not pick any framework, library and force it on the developer, m-core makes few choices on behalf of the user of this library and they are the following:

Dependencies

1. Jetty server/servlet container for your java based microservice
2. JAXRS Jersey REST API framework
3. Configuration library from Typesafe, now lightbend
4. Orbitz's Consul client 
5. dropwizard's metrics library for monitoring and healthchecks


Now, what do these dependencies get you?

#### ..and more importantly, what does m-core specifically get you? 

In the order of importance

1. Assuming you are intending to use consul based key-value configurations and consul discovery for your microservice, m-core will register the application on startup to a consul server, enabling discovery.

2. m-core also will update configurations dynamically when paired with consul watch and when running in consul enabled mode. Note that consul dependency can be disabled in development mode.

3. Will enforce health checks to be defined by each microservice application that uses m-core

4. A server that is REST enabled for your application

5. HyperMedia and Stateful Links. If you decide to develop API services that return resources with hypermedia (Links mainly) HATEOAS style, m-core provides a custom version of Link with href, rel and methods attributes.


## Quickstart

1. Add m-core dependency to your project

2. Extend the Microservice class found in m-core from your bootstrap class that has a main method and implement the following abstract methods 

- serviceName() -- returns the name of your microservice
- servicePort() -- returns the port number where your application listens to requests
- resourcePackages() -- returns a string set with the java package name(s) that contains resource handlers. Here resource handlers refer to the java classes that use JAXRS annotations to route or handle rest api requests.
- healthCheckList() - returns a list of HealthCheck object that you want to be called for validating the health of your service. The default health check by m-core just validates whether the server backing the service is reachable or not. 

3. Write the resource handler classes with JAXRS annotations for url paths you want to support

4. gradle run, you should have your app running at http://localhost:<port>/<app-name>/



This documentation is a work-in-progress...so expect more udpates to come. 
