package fr.emse.opensensingcity;

import fr.emse.opensensingcity.configuration.*;
import fr.emse.opensensingcity.ldprgenerator.LDPResourceRequestGenerator;
import fr.emse.opensensingcity.slugtemplate.IRIGenerator;
import fr.emse.opensensingcity.tests.TestSlug;
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
    public static void main(String args []) throws IOException {
        ClassLoader classLoader = main.class.getClassLoader();
        File file = new File(classLoader.getResource("Configuration.ttl").getFile());

        Configuration configuration = ConfigurationFactory.createConfiguration(file.getAbsolutePath());
        //configuration.print();
        configuration.execute();
        LDPResourceRequestGenerator rg = new LDPResourceRequestGenerator(configuration);





        //System.out.println("test");
        //LDPRGenerator.sendRequest(configuration);
        //TestSlug.test3("__r.iri");
        //TestSlug.testHierarchicalSlug("{_r.iri.path[4]}");

    }
}
