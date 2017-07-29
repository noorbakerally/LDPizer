# LDPizer
LDPizer process a design document written the [vocabulary](https://github.com/noorbakerally/LDPDesignVocabulary) and generate POST request which are then sent to an LDP for LDPR (LDP Resource) materialization.

## Build  
LDPizer can be used via its command line interface. Build it using `mvn clean install` which is going to generate a self-contained jar in the `target` directory. 


## Usage
1. -b,--baseURL <arg>:URL of the LDP endpoint where POST request have to be sent. If not provided, LDPizer will not process the design document unless the -v option is specified in which case, a virtual LDP design document is generated

2. -d,--designdocument <arg>: Path to design document. The design document is written using the vocabulary (https://github.com/noorbakerally/LDPDesignVocabulary)
  
3. -l: Disable logging, by default logging is enabled

4. -p,--password <arg>: Password for authentication. `username` and `password` must be provided together.           
  
5. -u,--username <arg>: Username authentication. `username` and `password` must be provided together.           
  
6. -v: Generate Virtual LDP design document

## Abstract model of design document

![Abstract Model](https://raw.githubusercontent.com/noorbakerally/LDPDesignVocabulary/master/abstract_model.png)

