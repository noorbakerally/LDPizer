package fr.emse.opensensingcity.configuration;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Created by noor on 28/06/17.
 */
public class RDFContentDataSource extends DataSource {

    public RDFContentDataSource(String dataSourceIRI, String location) {
        super(dataSourceIRI);
        setLocation(location);
        loadModel();
    }

    public void loadModel(){
        if (model==null){
            model = ModelFactory.createDefaultModel();
            model.read(location);
        }
    }

    @Override
    public ResultSet executeResourceQuery(String query) {
        return Global.exeQuery(query,model);

    }

    @Override
    public Model executeGraphQuery(String query) {
        Model results = Global.exeGraphQuery(query,model);
       return results;
    }
}
