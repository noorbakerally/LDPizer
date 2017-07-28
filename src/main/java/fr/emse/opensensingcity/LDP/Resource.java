package fr.emse.opensensingcity.LDP;


import fr.emse.opensensingcity.configuration.RelatedResource;

/**
 * Created by noor on 28/06/17.
 */
public abstract class Resource extends fr.emse.opensensingcity.RDF.Resource {
    String relatedResourceIRI;
    String slug;
    Container container;
    RelatedResource relatedResource;
    public Resource(String iri) {
        super(iri);
    }

    public String getRelatedResourceIRI() {
        return relatedResourceIRI;
    }

    public void setRelatedResourceIRI(String relatedResourceIRI) {
        this.relatedResourceIRI = relatedResourceIRI;
    }
    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }
    public Container getContainer() {
        return container;
    }
    public void setContainer(Container container) {
        this.container = container;
    }
    public RelatedResource getRelatedResource() {
        return relatedResource;
    }

    public void setRelatedResource(RelatedResource relatedResource) {
        this.relatedResource = relatedResource;
    }
}
