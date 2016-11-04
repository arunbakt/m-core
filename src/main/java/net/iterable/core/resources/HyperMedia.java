package net.iterable.core.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by arun on 9/27/16.
 */
public class HyperMedia {

    private Set<StatefulLink> links;

    public HyperMedia() {
   }

    @JsonProperty("links")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Set<StatefulLink> links() {
        return links;
    }

    public void addLink(StatefulLink link) {
        if(links == null)
            links = new HashSet();
        this.links.add(link);
    }

    public void addLinks(Set<StatefulLink> links) {
        if(links == null)
            links = new HashSet();
        this.links = links;
    }

}
