package fr.emse.opensensingcity.RDF;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noor on 28/06/17.
 */
public abstract class Resource {
    String iri;

    public Resource(String iri){
        this.iri = iri;
    }

    public String getIRI() {
        return iri;
    }
    public void setIRI(String iri) {
        this.iri = iri;
    }
}
