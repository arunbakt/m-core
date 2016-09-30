package net.iterable.core.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.Set;

/**
 * Created by arun on 9/29/16.
 */

public class StatefulLink {

    private Set<HttpMethod> methods;

    private javax.ws.rs.core.Link link;

    public StatefulLink(javax.ws.rs.core.Link link, Set<HttpMethod> methods) {
        this.link = link;
        this.methods = methods;
    }

    @JsonProperty("href")
    public URI getUri() {
        return link.getUri();
    }

    public String getRel() {
        return link.getRel();
    }

    public Set<HttpMethod> getMethods() {
        return methods;
    }
}
