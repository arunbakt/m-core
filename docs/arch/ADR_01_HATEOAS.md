# HATEOAS 

m-core provides a custom version of Link with href, rel and methods attributes to support HATEOAS style responses on api calls.
 
Currently there are several hypermedia formats such uber, HAL, siren..and more. There is no consensus or standard that is recommended or adopted industry wide at this point. I decided to follow the style used by [paypal](https://developer.paypal.com/docs/api/payments/). 

The customization done on top of Link from jaxrs is a modification to represent the available http methods for a given resource link based on the application state.

The HyperMedia and StatefulLink depends and assumes the microservice to be using Jackson Json libraries. If you choose to extend the HyperMedia class to represent the resources in your app, you will have to use Jackson JSON parser.
If not, you are free to choose other choices such as MOXy or JAXB implementations.

