package fr.emse.opensensingcity.slugtemplate;
import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.ParseNode;
import fr.emse.opensensingcity.LDP.Resource;
import fr.emse.opensensingcity.configuration.ConfigurationFactory;
import fr.emse.opensensingcity.configuration.Global;
import fr.emse.opensensingcity.configuration.RelatedResource;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.path.Path;
import org.apache.jena.sparql.path.PathFactory;
import org.apache.jena.sparql.path.PathParser;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by noor on 28/06/17.
 */
public class IRIGenerator {
    static Resource r;
    static int level = 0;

    public static String getSlug(Resource r, String slugTemplate){
        IRIGenerator.r = r;
        int i=0;
        while (i<slugTemplate.length()){
            if (slugTemplate.charAt(i)=='{'){
                int j = i;
                while (j<slugTemplate.length()){
                    if (slugTemplate.charAt(j)=='}'){
                        String varTemplate = slugTemplate.substring(i+1,j);
                        String varValue = processVarTemplate(varTemplate,r);
                        slugTemplate = slugTemplate.replace("{"+varTemplate+"}",varValue);
                        break;
                    }
                    j++;
                }
            }
            i++;
        }
        return slugTemplate;
    }

    private static String processVarTemplate(String varTemplate, Resource r) {
        String result = null;
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
            result = handleRootNode(rootNode);
        } catch (BnfParser.ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    /** Parser Function **/
    public static String handleRootNode(ParseNode rootNode){
        String result = null;
        if (rootNode.getToken().toString().equals("<exp>")){
            result =  handleExp(rootNode);
        }
        return result;
    }

    private static String handleQueryPart(ParseNode nodes) {
        //System.out.println("Function:handleQueryPart");

        String iriStr = null;
        int num = -1;
        String result = null;
        String qpOperation = null;
        //System.out.println(node.toString());

        for (ParseNode node:nodes.getChildren()){
            if (node.getToken().equals("<exp>")){
                iriStr = handleExp(node);
                //System.out.println("IRI:"+iriStr);
            } else if (node.getToken().equals("<num>")){
                num = handleNumber(node);
            } else if (node.getToken().equals("<queryFuncPart>")){
                qpOperation = node.getChildren().get(0).getToken();
            }
        }
        URI iri = URI.create(iriStr);

        result = getQueryPart(iri.getQuery(),qpOperation,num);
        return result;
    }

    private static String getQueryPart(String queryPart,String qpOperation,int num){

        String queryParts[] = queryPart.split("&");

        if (num == -1){
            return queryPart;
        }
        queryPart = queryParts[num];

        String result = null;
        if (qpOperation == null){
            result = queryPart;
        } else {
            if (qpOperation.equals("key")){
                String qps[] = queryPart.split("=");
                result = qps[0];
            } else if (qpOperation.equals("value")){
                String qps[] = queryPart.split("=");
                result = qps[1];
            }
        }
        return result;
    }
    private static String handleURIFunc(ParseNode nodes) {
        //System.out.println("Function:handleURIFunc");

        String iriStr = null;
        String result = null;
        String flag = null;
        //System.out.println(node.toString());

        for (ParseNode node:nodes.getChildren()){
            if (node.getToken().equals("<exp>")){
                iriStr = handleExp(node);
                //System.out.println("IRI:"+iriStr);
            } else if (node.getToken().equals("<URIFlag>")){
                flag = node.getChildren().get(0).getToken();
            }
        }
        URI iri = URI.create(iriStr);
        //System.out.println("Flag:"+flag);
        if (flag.equals("SCHEME")){
            result = iri.getScheme();
        } else if (flag.equals("USER")){
            result = iri.getUserInfo();
        } else if (flag.equals("PASSWORD")){

        } else if (flag.equals("HOST")){
            result = iri.getHost();
        } else if (flag.equals("PORT")){
            result = String.valueOf(iri.getPort());
        } else if (flag.equals("QUERY")){
            result = iri.getQuery();
        } else if (flag.equals("FRAGMENT")){
            result = iri.getFragment();
        }
        //System.out.println("handleURIFunc result:"+result);
        return result;
    }

    private static int handleNumber(ParseNode nodes) {
        int number = 0;
        for (ParseNode node:nodes.getChildren()){
            number = Integer.parseInt(node.getToken());
        }
        return number;
    }

    private static String handleExp(ParseNode node) {
        //System.out.println("Function:handleExp");
        String result = null;
        //System.out.println(node.toString());
        ParseNode rootChild = node.getChildren().get(0);
        if (rootChild.getToken().equals("<builtInCall>")){
            result = handleBuiltInCall(rootChild);
        } else if (rootChild.getToken().equals("<rFunc>")){
            result = handleResourceFunc(rootChild);
        }
        else if (rootChild.getToken().equals("'asd'")){
            return "http://example.com/test?k1=v1&k2=v2";
        }
        return result;
    }

    private static String handleResourceFunc(ParseNode nodes) {
        String iriR = null;

        String result = null;
        //System.out.println("Function:handleResourceFunc");

        for (ParseNode node:nodes.getChildren()){

            if (node.getToken().contains("<r>")){
                String rStr = node.getChildren().get(0).getToken();
                int numUnder = rStr.lastIndexOf("_") -1 ;
                while (numUnder>=0){
                    r = r.getContainer();
                    numUnder--;
                }
                iriR = r.getRelatedResource().getIRI();
            }

            if (node.getToken().contains("iri")){
                result = iriR;

            }

            if (node.getToken().equals("ppath")){

                String ppath = nodes.getChildren().get(3).getChildren().get(0).getToken();
                ppath = ppath.substring(1,ppath.length()-1);

                Path p = PathParser.parse(ppath, ConfigurationFactory.prefixMap);
                Query query = new Query();
                query.setQuerySelectType();

                Node oResource = NodeFactory.createURI(r.getRelatedResource().getIRI());
                Node res = NodeFactory.createVariable("result");
                TriplePath triplePattern = new TriplePath(oResource,p,res);
                ElementPathBlock tp = new ElementPathBlock();
                tp.addTriplePath(triplePattern);
                query.setQueryPattern(tp);
                query.setQueryResultStar(true);

                ResultSet rs = Global.exeQuery(query.serialize(), r.getRelatedResource().getGraph());
                while (rs.hasNext()){
                    String varResult = rs.next().get("?result").toString();
                    return varResult;
                }
            }
            else if (node.getToken().equals("<rFunPart>")){
                result = handleResourceFuncPart(iriR,node);

            }
        }
        //System.out.println("handleResourceFunc:"+result);
        return result;
    }

    private static String handleResourceFuncPart(String resource,ParseNode nodes) {
        //System.out.println("Function:handleResourceFuncPart");
        String result = null;
        URI iri = URI.create(resource);
        for (ParseNode node:nodes.getChildren()){
            String flag = node.getToken();
            if (flag.equals("scheme")){
                result = iri.getScheme();
            } else if (flag.equals("user")){
                result = iri.getUserInfo();
            } else if (flag.equals("password")){

            } else if (flag.equals("host")){
                result = iri.getHost();
            } else if (flag.equals("port")){
                result = String.valueOf(iri.getPort());
            } else if (flag.equals("query")) {
                result = iri.getQuery();
            }
            else if (flag.equals("query[")) {
                result = handleResourceQueryPart(iri, nodes);
            }
            else if (flag.equals("host[")){
                result = handleResourceHostPart(iri,nodes);
            } else if (flag.equals("path")){
                result = iri.getPath();
            }
            else if (flag.equals("path[")){
                result = handleResourcePathPart(iri,nodes);
            }
            else if (flag.equals("fragment")) {
                result = iri.getFragment();
            }
        }
        //System.out.println("handleResourceFuncPart:"+result);
        return result;
    }

    private static String handleResourceHostPart(URI iri,ParseNode nodes){
        String result = null;
        int num = -1;
        for (ParseNode node:nodes.getChildren()){
            if (node.getToken().equals("<num>")){
                num = handleNumber(node);
            }
        }
        result = getHostPart(iri.getHost(),num);
        return result;
    }
    private static String handleResourcePathPart(URI iri,ParseNode nodes){
        String result = null;
        int num = -1;
        for (ParseNode node:nodes.getChildren()){
            if (node.getToken().equals("<num>")){
                num = handleNumber(node);
            }
        }
        result = getPathPart(iri.getPath(),num);
        return result;
    }

    private static String getPathPart(String path, int num) {
        if (path.charAt(0) == '/'){
            path = path.substring(1);
        }
        String pathParts [] = path.split("/");
        return pathParts[num];
    }

    private static String handleResourceQueryPart(URI iri,ParseNode nodes){
        String result = null;
        String qpOperation = null;
        int num = -1;
        for (ParseNode node:nodes.getChildren()){
            if (node.getToken().equals("<num>")){
                num = handleNumber(node);
            } else if (node.getToken().equals("<queryFuncPart>")){
                qpOperation = node.getChildren().get(0).getToken();
            }
        }
        result = getQueryPart(iri.getQuery(),qpOperation,num);
        return result;
    }

    private static String handleBuiltInCall(ParseNode node) {
        //System.out.println("Function:handleBuiltInCall");

        String result = null;
        ParseNode rootChild = node.getChildren().get(0);
        //System.out.println(rootChild.getToken());
        if (rootChild.getToken().equals("QueryPart")){
            result = handleQueryPart(node);
        } else if (rootChild.getToken().equals("URIPart")){
            result = handleURIFunc(node);
        } else if (rootChild.getToken().equals("PathPart")){
            result = handlePathPart(node);
        } else if (rootChild.getToken().equals("HostPart")){
            result = handleHostPart(node);
        }
        else if (rootChild.getToken().equals("Split")){
            result = handleSplit(node);
        }
        return result;
    }

    private static String handleSplit(ParseNode nodes) {
        String result = null;
        String str = null;
        String delimeter = null;
        int num = -1;
        for (ParseNode node:nodes.getChildren()){
            if (node.getToken().equals("<exp>")){
                str = handleExp(node);
            } else if (node.getToken().equals("<num>")){
                num = handleNumber(node);
            } else if (node.getToken().equals("<string>")){
                delimeter = node.getChildren().get(0).getToken();
                delimeter = delimeter.substring(1,delimeter.length()-1);
            }
        }
        result = str.split(delimeter)[num];
        return result;
    }

    private static String handlePathPart(ParseNode nodes) {
        //System.out.println("Function:handlePathPart");

        String iriStr = null;
        String result = null;
        int num = 0;

        for (ParseNode node:nodes.getChildren()){
            if (node.getToken().equals("<exp>")){
                iriStr = handleExp(node);
                //System.out.println("IRI:"+iriStr);
            } else if (node.getToken().equals("<num>")){
                num = handleNumber(node);
                //System.out.println("Num:"+num);
            }
        }
        URI iri = URI.create(iriStr);
        String pathParts[] = iri.getPath().split("/");
        result = pathParts[num];
        //System.out.println("handlePathPart result:"+result);
        return result;
    }

    private static String handleHostPart(ParseNode nodes) {
        //System.out.println("Function:handleHostPart");

        String iriStr = null;
        String result = null;
        int num = 0;

        for (ParseNode node:nodes.getChildren()){
            if (node.getToken().equals("<exp>")){
                iriStr = handleExp(node);
                //System.out.println("IRI:"+iriStr);
            } else if (node.getToken().equals("<num>")){
                num = handleNumber(node);
                //System.out.println("Num:"+num);
            }
        }
        URI iri = URI.create(iriStr);
        result = getHostPart(iri.getHost(),num);
        //System.out.println("handleHostPart result:"+result);
        return result;
    }

    static String getHostPart(String host, int num){
        String hostParts[] = host.split("\\.");
        String result = hostParts[hostParts.length - 1 - num];
        return result;
    }


}
