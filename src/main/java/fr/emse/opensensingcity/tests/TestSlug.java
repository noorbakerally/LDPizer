package fr.emse.opensensingcity.tests;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import fr.emse.opensensingcity.LDP.Container;
import fr.emse.opensensingcity.LDP.RDFSource;
import fr.emse.opensensingcity.configuration.RelatedResource;
import fr.emse.opensensingcity.slugtemplate.IRIGenerator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by noor on 05/07/17.
 */
public class TestSlug {

    public static void test1(){
        String slugTemplate = "{_r.iri.scheme}-ans";
        String iri = "https://opendata.paris.fr/api/v2/catalog/exports/ttl";
        RDFSource ldprs = new RDFSource("");
        ldprs.setRelatedResource(new RelatedResource(iri));
        String slug = IRIGenerator.getSlug(ldprs,slugTemplate);
        System.out.println(slug);
    }

    public static void test2(){
        String slugTemplate = "{_r.iri.path[2]}-ans";
        String iri = "";

        iri = "https://opendata.paris.fr/api/v2/catalog/exports/ttl";
        Container c1 = new Container("");
        c1.setRelatedResource(new RelatedResource(iri));

        iri = "https://opendata.paris.fr/api/v2/catalog/datasets/adresse_paris";
        Container c2 = new Container("");
        c2.setRelatedResource(new RelatedResource(iri));
        c2.setContainer(c1);

        iri = "https://opendata.paris.fr/api/v2/catalog/datasets/adresse_paris-csv";
        RDFSource ldprs = new RDFSource("");
        ldprs.setRelatedResource(new RelatedResource(iri));
        ldprs.setContainer(c2);


        String slug = IRIGenerator.getSlug(ldprs,slugTemplate);
        System.out.println(slug);
    }


    public static void test3(String varTemplate){
        String result = null;
        //URL bnfURL = IRIGenerator.class.getResource("/TestSlugTemplate.bnf");
        URL bnfURL = IRIGenerator.class.getResource("/SlugTemplate.bnf");
        File bnffile = null;
        try {
            bnffile = new File(bnfURL.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        BnfParser parser = null;
        try {
            parser = new BnfParser(bnffile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BnfParser.InvalidGrammarException e) {
            e.printStackTrace();
        }
        try {
            ParseNode rootNode = parser.parse(varTemplate);
            System.out.println(rootNode);

        } catch (BnfParser.ParseException e) {
            e.printStackTrace();
        }
    }

    public static void testHierarchicalSlug(String slugTemplate){

        String iri = "";

        iri = "https://opendata.paris.fr/api/v2/catalog1/exports/ttl";
        Container c1 = new Container("");
        c1.setRelatedResource(new RelatedResource(iri));

        iri = "https://opendata.paris.fr/api/v2/catalog2/datasets/adresse_paris";
        Container c2 = new Container("");
        c2.setRelatedResource(new RelatedResource(iri));
        c2.setContainer(c1);

        iri = "https://opendata.paris.fr/api/v2/catalog3/datasets/adresse_paris-csv";
        RDFSource ldprs = new RDFSource("");
        ldprs.setRelatedResource(new RelatedResource(iri));
        ldprs.setContainer(c2);


        String slug = IRIGenerator.getSlug(ldprs,slugTemplate);
        System.out.println("Slug Answer:"+slug);
    }
}
