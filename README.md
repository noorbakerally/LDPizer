# LDPizer
LDPizer process a design document written the [vocabulary](https://github.com/noorbakerally/LDPDesignVocabulary) and generate POST request which are then sent to an LDP for LDPR (LDP Resource) materialization.

## Build  
LDPizer can be used via its command line interface. Build it using `mvn clean install` which is going to generate a self-contained jar in the `target` directory. 


### Options
1. -b,--baseURL <arg>
  - URL of the LDP endpoint where POST request have to be sent

2. -d,--designdocument <arg>   
  - Path to design document. The design document is written using the vocabulary (https://github.com/noorbakerally/LDPDesignVocabulary)
  
3. -l                          
  - Disable logging, by default logging is enabled

4. -p,--password <arg>         
  - Password for authentication
  
5. -u,--username <arg>         
  - Username authentication
  
6. -v
  - Generate Virtual LDP design file

