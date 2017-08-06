package fr.emse.opensensingcity;

import fr.emse.opensensingcity.configuration.*;
import fr.emse.opensensingcity.ldprgenerator.LDPResourceRequestGenerator;
import fr.emse.opensensingcity.slugtemplate.IRIGenerator;
import fr.emse.opensensingcity.tests.TestSlug;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

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
        File file = new File(classLoader.getResource("Configuration.ttl").getFile());

        //Configuration configuration = ConfigurationFactory.createConfiguration(file.getAbsolutePath());
        //configuration.print();
        //configuration.execute();
        //LDPResourceRequestGenerator rg = new LDPResourceRequestGenerator(configuration);

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
                File designDocument = new File(designDocumentPath);
                System.out.println("Loading design documnent");
                Configuration configuration = ConfigurationFactory.createConfiguration(file.getAbsolutePath());

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




        //System.out.println("test");
        //LDPRGenerator.sendRequest(configuration);
        //TestSlug.test3("__r.iri");
        //TestSlug.testHierarchicalSlug("{_r.iri.path[4]}");

    }
}
