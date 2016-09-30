# microservice-core
Foundational library for creating RESTful microservice


## Question and Answers:

### What is the assumption/dependencies for the application built using this library?
All applications built using m-core are to be run as docker containers, and assumes a consul client is
run locally on the host machine, which in turn assumes consul cluster with servers is available for the
consul client to join.


### What happens when the app dies?
Consul client would know that as it does health checks on the app.

### What happens when the consul client crashes?
This is a unfortunate situation and very likely would not occur. If it occurs, consul servers would know
the unavailability of the consul client and with it all the registered services (if only one instance of an
app is running registered on the crashed consul client).
For now, we don't take care of the scenario where a crashed consul client is started on the same host whereas the
app instances are still running on that host and won't register again with the consul client. In this case,
consul client would not know of the existence of these app instances to do the health checks.


Features:

HyperMedia and Stateful Links: If you decide to develop API services that return resources with hypermedia
(Links mainly), m-core provides a custom version of Link with href, rel and methods attributes.

Currently there are several hypermedia formats such uber, HAL, siren, and I decided to follow the style used by
paypal api docs. One modification is to represent the available http methods for a given resource link based on the
application state.

The HyperMedia and StatefulLink depends and assumes the microservice to be using Jackson Json libraries. If you
choose to extend the HyperMedia class to represent the resources in your app, you will have to use Jackson JSON parser.
If not, you are free to choose other choices such as MOXy or JAXB implementations.


