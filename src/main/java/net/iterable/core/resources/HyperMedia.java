package net.iterable.core.resources;

import javax.ws.rs.core.Link;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by arun on 9/27/16.
 */
public class HyperMedia {

    private Set<Link> links;

    public HyperMedia() {
        links = new HashSet<>();
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void addLink(Link link) {
        this.links.add(link);
    }

}
