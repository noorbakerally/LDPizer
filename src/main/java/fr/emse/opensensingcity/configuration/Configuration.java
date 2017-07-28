package fr.emse.opensensingcity.configuration;

import fr.emse.opensensingcity.LDP.Container;
import fr.emse.opensensingcity.LDP.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bakerally on 5/29/17.
 */
public class Configuration {

    /*Attributes*/
    String baseURI;
    public Map<String,ContainerMap> containerMaps = new HashMap<String, ContainerMap>();
    public Map<String,DataSource> dataSources = new HashMap<String, DataSource>();
    public Configuration(){
        containerMaps = new HashMap<String, ContainerMap>();
    }
    //private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    /*Core Methods*/
    public void execute() throws IOException {
        for (Map.Entry <String,ContainerMap> entry :containerMaps.entrySet()){
            ContainerMap containerMap = entry.getValue();
            if (containerMap.getParentContainerMap() !=null) continue;
            containerMap.generateResources();
        }
    }

    /*General Methods*/
    public String getBaseURI() {
        return baseURI;
    }
    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }
    public Map<String, ContainerMap> getContainerMap() {
        return containerMaps;
    }
    public void setContainerMap(Map<String, ContainerMap> containerMap) {
        this.containerMaps = containerMap;
    }
    public void print() {
        for (Map.Entry <String,ContainerMap> entry :containerMaps.entrySet()){
            ContainerMap containerMap = entry.getValue();
            if (containerMap.getParentContainerMap() !=null) continue;
            System.out.println(containerMap.toString(0));
        }
    }
    public void addDataSource(DataSource ds){
        dataSources.put(ds.getIRI(),ds);
    }
    public DataSource getDataSource(String dataSourceIRI){
        return dataSources.get(dataSourceIRI);
    }
}
