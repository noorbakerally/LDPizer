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


## How does it works ?

### Loading Part

- the LDPizer loads all ContainerMaps, their respective RDFSourceMap and NonRDFSourceMap. Also, all SourceMap's ResourceMap are loaded together with their DataSource.

- Cycles are currently not supported. If a ContainerMap has a link to another ContainerMap which is among its ancestor, the LDPization process stops. 

- currently the design document is not validated. In future versions, we intend to use the [SHACLE W3C recommendation](https://www.w3.org/TR/shacl/) for validating the design document. 

### Processing of the Design Document

- the processing starts with the top ContainerMap, that is root ContainerMap with no parents
- currently, only LDP basic containers are supported

- Processing of `ContainerMap` containerMap:
	- the `ResourceMap` of the `ContainerMap` are processed which returns a list of resources and associated with each resource is an RDF graph
	- let `resources` denotes the list of resources obtained from executing all the `ResourceMap` and `resource.graph` refer to the RDF graph of `resource`
	- for each `resource` in `resources`:
		- create an LDPBC ldpbc 
		- ldpbc.graph = resource.graph
		- if (containerMap.linkToSource != null):
			- ldprs.graph.appendTriple(<>,containerMap.linkToSource,`resource`)
		- else if (containerMap.linkFromSource != null):
			- ldprs.graph.appendTriple(`resource`,containerMap.linkFromSource,<>)
		- ldpbc.container = containerMap.container
		- ldpbc.slug = processSlug(`resource`,containerMap.slugTemplate)
		- ldpbc.containerMaps = containerMap.containerMaps
		- ldpbc.rdfSourceMaps = containerMap.rdfSourceMaps
		- ldpbc.nonRDFSourceMaps = containerMap.nonRDFSourceMaps
		- ldpbc.processContainerMaps
		- ldpbc.processRDFSourceMaps
		- ldpbc.processNonRDFSourceMaps

- Processing of `RDFSourceMap` rdfSourceMap:
	- the `ResourceMap` of the `RDFSourceMap` are processed which returns a list of resources and associated with each resource is an RDF graph
	- let `resources` denotes the list of resources obtained from executing all the `ResourceMap` and `resource.graph` refer to the RDF graph of `resource`
	- for each `resource` in `resources`:
		- create an LDPRS ldprs 
		- ldprs.graph = resource.graph
		- if (rdfSourceMap.linkToSource != null):
			- ldprs.graph.appendTriple(<>,rdfSourceMap.linkToSource,`resource`)
		- else if (rdfSourceMap.linkFromSource != null):
			- ldprs.graph.appendTriple(`resource`,rdfSourceMap.linkFromSource,<>)
		- ldprs.container = rdfSourceMap.container
		- ldprs.slug = processSlug(`resource`,rdfSourceMap.slugTemplate)

- Processing of `NonRDFSourceMap` nonRDFSourceMap:
	- the `ResourceMap` of the `NonRDFSourceMap` are processed which returns a list of resources and associated with each resource is a byte array which refers to the binary content of `resource` at its URL 
	- let `resources` denotes the list of resources obtained from executing all the `ResourceMap` and `resource.byte` refer to the byte array of the `resource`
	- for each `resource` in `resources`:
		- create an LDPNR ldpnr 
		- ldpnr.graph = resource.graph
		- ldpnr.container = nonRDFSourceMap.container
		- ldpnr.slug = processSlug(`resource`,nonRDFSourceMap.slugTemplate)

- Processing of `ResourceMap`
	- the `resourceQuery` of the `ResourceMap` is processed using its `SourceMap` to obtain the final `resourceQuery`
	- `resourceQuery` is executed on the `DataSource`
	- for a particular `resource` obtained by executing the `resourceQuery` on the `DataSource`, if its belonging `SourceMap` is a `ContainerMap` or `RDFSourceMap`, then 
	a graph for the resource is generated from the particular `DataSource` using the `graphQuery` or `graphTemplate` of the `ResourceMap`
	- In the current version of the LDPizer, `DataSource` are considered separately and therefore the `resourceQuery` is executed on each `DataSource` without considering distributed query federation semantics
















