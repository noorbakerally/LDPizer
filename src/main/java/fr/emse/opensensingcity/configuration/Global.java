package fr.emse.opensensingcity.configuration;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.system.PrefixMap;

/**
 * Created by bakerally on 6/14/17.
 */
public class Global {
    public static boolean authorization  =false;
    public static String  baseURI = "http://ci.emse.fr/marmotta/ldp";
    public static String vocabularyPrefix = "http://opensensingcity.emse.fr/LDPDesignVocabulary/";

    public static String getVTerm(String lname){
        return vocabularyPrefix+lname;
    }

    public static String username;
    public static String password;

    public static String prefixes = "PREFIX ldp: <http://www.w3.org/ns/ldp#> \n" +
            "PREFIX :     <http://opensensingcity.emse.fr/LDPDesignVocabulary/> \n" +
            "PREFIX data:     <http://opensensingcity.emse.fr/LDPDesign/data/> \n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"  +
            "PREFIX dcat: <http://www.w3.org/ns/dcat#>\n";

    public enum TripleType {SubjectTriples,ObjectTriples}

    public enum ContainerType {Basic,Direct,Indirect}

    public static Property getLDPContains(){
        return ResourceFactory.createProperty("http://www.w3.org/ns/ldp#contains");
    }

    public static Resource getLDPBC(){
        return ResourceFactory.createResource("http://www.w3.org/ns/ldp#BasicContainer");
    }

    public static ResultSet exeQuery(String queryStr, Model model){
        queryStr = Global.prefixes + queryStr;
        //System.out.println(queryStr);
        Query query = QueryFactory.create(queryStr, Syntax.syntaxARQ);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet rs = qexec.execSelect() ;
        return rs;
    }

    public static Model exeGraphQuery(String queryStr, Model model){
        queryStr = Global.prefixes + queryStr;
        //System.out.println(queryStr);
        Query query = QueryFactory.create(queryStr, Syntax.syntaxARQ);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        return qexec.execConstruct();
    }

    public static String getBaseURI(){
        return baseURI;
    }


}
