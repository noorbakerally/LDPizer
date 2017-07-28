package fr.emse.opensensingcity.LDP;

import fr.emse.opensensingcity.configuration.RelatedResource;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by bakerally on 5/29/17.
 */
public class RDFSource extends Resource {
    Model graph;
    public RDFSource(String iri) {
        super(iri);
        setSlug(iri);
        graph = ModelFactory.createDefaultModel();
    }

    public Model getGraph() {
        return graph;
    }
    public void setGraph(Model graph) {
        this.graph = graph;
    }
    public RelatedResource getRelatedResource() {
        return relatedResource;
    }
    public void setRelatedResource(RelatedResource relatedResource) {
        this.relatedResource = relatedResource;
    }
}
