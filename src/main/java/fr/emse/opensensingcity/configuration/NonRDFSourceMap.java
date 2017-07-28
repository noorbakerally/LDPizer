package fr.emse.opensensingcity.configuration;

import fr.emse.opensensingcity.LDP.Container;
import fr.emse.opensensingcity.LDP.NonRDFSource;
import fr.emse.opensensingcity.LDP.RDFSource;
import fr.emse.opensensingcity.slugtemplate.IRIGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noor on 15/07/17.
 */
public class NonRDFSourceMap extends SourceMap {

    String contentType;
    public NonRDFSourceMap(String IRI){
        super(IRI);
    }
    public ResourceMap addResourceMap(String iri) {
        ResourceMap newResourceMap = new ResourceMap(iri);
        resourceMaps.put(iri,newResourceMap);
        return newResourceMap;
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
                    relatedResources.put(relatedResource, rr1);
                } else {
                    rr1 = relatedResources.get(relatedResource);
                }
            }
        }

    }

    public void generateResources(){
        generateRelatedResources();
        for (Map.Entry <String,RelatedResource> rrEntry:getRelatedResources().entrySet()){
            RelatedResource rr = rrEntry.getValue();
            NonRDFSource nonRdfSource = null;
            nonRdfSource = new NonRDFSource("temp");
            nonRdfSource.setRelatedResource(rr);
            String slug = IRIGenerator.getSlug(nonRdfSource, getSlugTemplate());
            nonRdfSource.setSlug(slug);
            nonRdfSource.setContainer(this.getContainer());
            nonRdfSource.setContentType(contentType);

            //get binary content of file
            File file = new File("DownloadedFile");
            byte [] binary = new byte[0];
            try {
                FileUtils.copyURLToFile(URI.create(rr.getIRI()).toURL(),file);
                binary = FileUtils.readFileToByteArray(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            nonRdfSource.setBinary(binary);
            resources.add(nonRdfSource);
        }
    }

    @Override
    public SourceMap copy() {
        NonRDFSourceMap newObject = new NonRDFSourceMap(getIRI());

        newObject.IRI = IRI;
        newObject.slugTemplate = slugTemplate;
        newObject.constant = constant;
        newObject.resourceMaps =resourceMaps;
        newObject.relatedResources = new HashMap<>();
        newObject.resources = new ArrayList<>();
        newObject.contentType = contentType;
        return newObject;
    }

    /*General Methods*/
    public Container getContainer() {
        return container;
    }
    public void setContainer(Container container) {
        this.container = container;
    }
    public String getIRI() {
        return IRI;
    }
    public void setIRI(String IRI) {
        this.IRI = IRI;
    }
    public String getSlugTemplate() {
        return slugTemplate;
    }
    public void setSlugTemplate(String slugTemplate) {
        this.slugTemplate = slugTemplate;
    }
    public String getConstant() {
        return constant;
    }
    public void setConstant(String constant) {
        this.constant = constant;
    }
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
