package fr.emse.opensensingcity.configuration;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;


/**
 * Created by bakerally on 6/14/17.
 */
public abstract class  DataSource {
    String IRI;
    String location;
    Model model;

    public DataSource(String dataSourceIRI) {
        this.IRI = dataSourceIRI;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getIRI() {
        return IRI;
    }

    public void setIRI(String IRI) {
        this.IRI = IRI;
    }

    public abstract ResultSet executeResourceQuery(String query);
    public abstract Model executeGraphQuery(String query);


    public String toString(int level) {
        String str="";
        String tab= StringUtils.repeat("\t", level);
        String atttab= StringUtils.repeat("\t", level);
        str += tab+"\t\t\tLocation: "+getLocation()+"\n";
        return str;
    }
}
