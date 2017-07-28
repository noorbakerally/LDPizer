package fr.emse.opensensingcity.configuration;

import fr.emse.opensensingcity.LDP.Container;
import fr.emse.opensensingcity.LDP.RDFSource;
import fr.emse.opensensingcity.LDP.Resource;
import fr.emse.opensensingcity.slugtemplate.IRIGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.Model;


import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by noor on 26/06/17.
 */
public class RDFSourceMap extends SourceMap {

    public RDFSourceMap(String RDFSourceMapIRI) {
        super(RDFSourceMapIRI);
    }

    @Override
    public SourceMap copy() {
        SourceMap newObject = new RDFSourceMap(getIRI());
        newObject.IRI = IRI;
        newObject.slugTemplate = slugTemplate;
        newObject.constant = constant;
        newObject.resourceMaps =resourceMaps;
        newObject.relatedResources = new HashMap<>();
        newObject.resources = new ArrayList<>();
        return newObject;
    }

    //generate all the list of related resources
    //together with their graphs
    public void generateRelatedResources(){

        //iterate through all resource maps
        //and generate the resources for each resource maps
        for (Map.Entry <String,ResourceMap> resourceMap:resourceMaps.entrySet()) {
            String resourceMapIRI = resourceMap.getKey();
            ResourceMap cResourceMap = resourceMap.getValue();
            //get the related resource from the ResourceMap
            //add it to the relatedResources for the RDFSourceMap
            Map<String, List<Model>> relatedResourcesMap = cResourceMap.getResources(this);
            for (String relatedResource : relatedResourcesMap.keySet()) {
                //if relatedResources already contain the relatedResource
                //check if the relatedResource has a link to the cResourceMap
                //if not add it
                RelatedResource rr1;
                if (!relatedResources.containsKey(relatedResource)) {
                    //create the relatedResource
                    //add it to the relatedResources of the RDFSourceMap
                    rr1 = new RelatedResource(relatedResource);
                    rr1.addResourceMap(cResourceMap);
                    relatedResources.put(relatedResource, rr1);
                } else {
                    rr1 = relatedResources.get(relatedResource);
                }

                //merge RelatedResources Graphs
                rr1.getModels().addAll(relatedResourcesMap.get(relatedResource));
            }
        }
        //merge all RelatedResource Model
        for (String relatedResource:relatedResources.keySet()){
            relatedResources.get(relatedResource).generateModel();
        }
    }

    //create resources RDFSourceMap generates
    public void generateResources() throws IOException {
        generateRelatedResources();
        for (Map.Entry <String,RelatedResource> rrEntry:getRelatedResources().entrySet()){
            RelatedResource rr = rrEntry.getValue();
            RDFSource rdfSource = null;
            rdfSource = new RDFSource("temp");
            rdfSource.setRelatedResource(rr);
            rdfSource.setGraph(rr.getGraph());
            String slug = IRIGenerator.getSlug(rdfSource, getSlugTemplate());
            rdfSource.setSlug(slug);
            rdfSource.setContainer(this.getContainer());
            resources.add(rdfSource);
        }
    }

    /*General Methods*/
    public Map<String, ResourceMap> getResourceMaps() {
        return resourceMaps;
    }
    public void setResourceMaps(Map<String, ResourceMap> resourceMaps) {
        this.resourceMaps = resourceMaps;
    }
    public Map<String, RelatedResource> getRelatedResources() {
        return relatedResources;
    }
    public void setRelatedResources(Map<String, RelatedResource> relatedResources) {
        this.relatedResources = relatedResources;
    }
    public Container getContainer() {
        return container;
    }
    public void setContainer(Container container) {
        this.container = container;
    }
    public String getSlugTemplate() {
        return slugTemplate;
    }
    public void setSlugTemplate(String slugTemplate) {
        this.slugTemplate = slugTemplate;
    }
    public String toString(int level) {
        String str = "";
        String tab= StringUtils.repeat("\t", level);
        String title = "RDFSourceMap:";
        if (this instanceof ContainerMap){
            title = "ContainerMap:";
        }
        String titleUnderline = StringUtils.repeat("", title.length());
        str = tab+title + "\n";
        str += tab+"\t\tIRI: "+getIRI()+"\n";
        for (Map.Entry <String,ResourceMap> resourceMapEntry:resourceMaps.entrySet()){
            str = str + "\n"+resourceMapEntry.getValue().toString(level+2);
        }
        return str;
    }
    public String getConstant() {
        return constant;
    }
    public void setConstant(String constant) {
        this.constant = constant;
    }


}
