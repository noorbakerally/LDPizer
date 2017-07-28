package fr.emse.opensensingcity.configuration;

import fr.emse.opensensingcity.LDP.Container;
import fr.emse.opensensingcity.LDP.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noor on 15/07/17.
 */
public abstract class SourceMap {
    Container container;
    String IRI;
    String slugTemplate;;
    String constant;
    Map<String,ResourceMap> resourceMaps = new HashMap<String, ResourceMap>();
    Map <String,RelatedResource> relatedResources = new HashMap<>();
    List<Resource> resources = new ArrayList<>();


    public SourceMap(String IRI){
        this.IRI = IRI;
    }
    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public ResourceMap addResourceMap(String iri) {
        ResourceMap newResourceMap = new ResourceMap(iri);
        resourceMaps.put(iri,newResourceMap);
        return newResourceMap;
    }

    public abstract SourceMap copy();
    public Container getContainer() {
        return container;
    }
    public void setContainer(Container container) {
        this.container = container;
    }
    public String getIRI() {
        return IRI;
    }
    public void setIRI(String iri) {
        this.IRI = iri;
    }

}
