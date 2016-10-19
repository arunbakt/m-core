package net.iterable.core.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Link;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by arun on 9/27/16.
 */
public class HyperMedia {

    private Set<StatefulLink> links;

    public HyperMedia() {
        links = new HashSet<StatefulLink>();
    }

    @JsonProperty("links")
    public Set<StatefulLink> links() {
        return links;
    }

    protected void addLink(StatefulLink link) {
        this.links.add(link);
    }

    protected void addLinks(Set<StatefulLink> links) {
        this.links = links;
    }

}
