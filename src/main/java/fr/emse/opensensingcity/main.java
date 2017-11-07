package fr.emse.opensensingcity;

import fr.emse.opensensingcity.configuration.*;
import fr.emse.opensensingcity.ldprgenerator.LDPResourceRequestGenerator;
import fr.emse.opensensingcity.slugtemplate.IRIGenerator;
import fr.emse.opensensingcity.tests.TestSlug;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by bakerally on 5/29/17.
 */
public class main {
    public static void main(String args []) throws ParseException {
        ClassLoader classLoader = main.class.getClassLoader();

        //RDFDataMgr.loadModel("https://raw.githubusercontent.com/noorbakerally/ISWC2017Demo/master/ParisGeo.ttl", Lang.TTL);

        /*Global.baseURI = "http://localhost:8080/marmotta/ldp";
        String cfile;

        cfile = "file:///home/bakerally/Downloads/dds/ParisCatalog.dd.ttl";
        //cfile = "file:///home/bakerally/Downloads/dds/ParisCatalog1.dd.ttl";

        Configuration configuration = ConfigurationFactory.createConfiguration(cfile);
        try {
            configuration.execute();
            LDPResourceRequestGenerator rg = new LDPResourceRequestGenerator(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }*/



        /*try {
            Global.baseURI = "http://localhost:8080/marmotta/ldp";
            Configuration configuration = ConfigurationFactory.createConfiguration("file:///home/bakerally/Downloads/ParisCatalog.dd.ttl");
            configuration.execute();
            LDPResourceRequestGenerator rg = new LDPResourceRequestGenerator(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

       String baseURL = "";
        CommandLine cl = CMDConfigurations.parseArguments(args);
        if (cl.getOptions().length == 0){
            CMDConfigurations.displayHelp();
            return;
        }

        //verify base IRI not provided
        if (!cl.hasOption("b") && !cl.hasOption("v")){
            System.out.println("Base IRI must be provided unless a virtual design document is required");
        }

        if (cl.hasOption("v")){
            System.out.println("Generation of design document for Virtual Deployment not yet implemented");
        }

        if (cl.hasOption("b")){
            baseURL = cl.getOptionValue("b");
            try {
                URL u = new URL(baseURL);
            } catch (MalformedURLException e) {
                System.out.println("Invalid URL");
                e.printStackTrace();
                return;
            }
            Global.baseURI = baseURL;
        }

        if (cl.hasOption("u")){
          if (!cl.hasOption("p")){
              System.out.println("Password not provided");
              return;
          }
          Global.username = cl.getOptionValue("u");
          Global.password = cl.getOptionValue("p");
          Global.authorization = true;
        }


        //verify design documents
        if (cl.hasOption("d")){
            String designDocumentPath = cl.getOptionValue("d");
            try{

                System.out.println("Loading the design documnent");
                Configuration configuration = ConfigurationFactory.createConfiguration(designDocumentPath);

                System.out.println("Design Document loaded");
                configuration.print();

                System.out.println("Processing Design Document...");
                configuration.execute();
                System.out.println("Processing Complete");

                System.out.println("Sending LDP Requests to:");
                LDPResourceRequestGenerator rg = new LDPResourceRequestGenerator(configuration);

            } catch (Exception exception){
                System.out.println("Error while trying to load file:"+designDocumentPath);
            }
        }
    }
}
