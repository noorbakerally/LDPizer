package fr.emse.opensensingcity;

import org.apache.commons.cli.*;

/**
 * Created by noor on 28/07/17.
 */
public class CMDConfigurations {
    public static CommandLine parseArguments(String[] args) throws ParseException {

        BasicParser commandLineParser = new BasicParser();
        CommandLine cl = commandLineParser.parse(getCMDOptions(), args);

         /*Process Options*/
        //print help menu
        if ( cl.hasOption('h') ) {
            CMDConfigurations.displayHelp();
        }
        return cl;
    }
    public static Options getCMDOptions(){
        Options opt = new Options()
                .addOption("h", "help",false, "Show help")
                .addOption("d", "designdocument", true, "Path to design document")
                .addOption("b", "baseURL", true, "URL of the LDP endpoint where POST request have to be sent")
                .addOption("u", "username", true, "Username authentication")
                .addOption("p","password",true,"Password for authentication")
                .addOption("l", false, "Disable logging, by default logging is enabled")
                .addOption("v", false, "Generate Virtual LDP design document")
                ;
        return opt;
    }
    public static void displayHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("LDPizer", getCMDOptions());
        System.exit(1);
    }
}
