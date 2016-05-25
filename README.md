# microservice-core
Foundational library for creating RESTful microservice


## Question and Answers:

1. What is the assumption/dependencies for the application built using this library?
All applications built using m-core are to be run as docker containers, and assumes a consul client is
run locally on the host machine, which in turn assumes consul cluster with servers is available for the
consul client to join.


2. What happens when the app dies?
Consul client would know that as it does health checks on the app.

3. What happens when the consul client crashes?
This is a unfortunate situation and very likely would not occur. If it occurs, consul servers would know
the unavailability of the consul client and with it all the registered services (if only one instance of an
app is running registered on the crashed consul client).
For now, we don't take care of the scenario where a crashed consul client is started on the same host whereas the
app instances are still running on that host and won't register again with the consul client. In this case,
consul client would not know of the existence of these app instances to do the health checks.



