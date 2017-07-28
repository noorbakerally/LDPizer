package fr.emse.opensensingcity.configuration;

import fr.emse.opensensingcity.RDF.Resource;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noor on 28/06/17.
 */
public class RelatedResource extends Resource {

    Model model;
    List<Model> models = new ArrayList<Model>();
    public RelatedResource(String iri){
        super(iri);

    }
    Map<String,ResourceMap> resourceMaps = new HashMap<>();
    public Map<String, ResourceMap> getResourceMaps() {
        return resourceMaps;
    }
    public void setResourceMaps(Map<String, ResourceMap> resourceMaps) {
        this.resourceMaps = resourceMaps;
    }
    public void addResourceMap(ResourceMap rs){
        resourceMaps.put(rs.getIRI(),rs);
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public void generateModel() {
        model = ModelFactory.createDefaultModel();
        for (Model cModel:models){
            model.add(cModel);
        }
    }

    public Model getGraph() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
