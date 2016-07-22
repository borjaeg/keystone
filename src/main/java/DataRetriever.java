import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by ismaro3 on 21/07/16.
 */
public class DataRetriever {


    public static void main(String[] args){
        Hurricane a  = getHurricaneData("http://dbpedia.org/resource/Hurricane_Katrina");
        System.out.println("Abstract: " + a.abstract_);
    }



    public static List<Hurricane> getHurricanesForKeywords(String[] keywords){

        List<String> instanceURIs = lookupURIs(keywords);
        List<Hurricane> hurricans = new ArrayList<Hurricane>();

        for(String uri: instanceURIs){
            hurricans.add(getHurricaneData(uri));
        }

        return hurricans;
    }
    /**
     * Devuelve datos de un huracán.
     * @param instanceURI
     * @return
     */
    public static Hurricane getHurricaneData(String instanceURI){

        String query = "select ?abstract ?areas" +
                "{" +
                  "<" + instanceURI + "> <http://dbpedia.org/ontology/abstract> ?abstract ." +
                  "<" + instanceURI + "> <http://dbpedia.org/property/areas> ?areas ." +
                  "FILTER (lang(?abstract) = 'en')" +
                "}";

        Hurricane hurricane = new Hurricane();

        try{
            Scanner scanner = ejecutarConsultaSparql(query);

            scanner.next();  //Skip header

            scanner.nextLine(); //Skip white line

            if(scanner.hasNextLine()){


                String linea = scanner.nextLine();
                String[] terms = linea.split("\t");




                hurricane.URI = instanceURI;
                if(terms.length > 0){
                    hurricane.abstract_ = terms[0];
                }

                if(terms.length>1){
                    hurricane.areas = terms[1];
                }


                return hurricane;
            }
            else{
                return hurricane;
            }
        }
        catch(Exception ex){

            ex.printStackTrace();
            return hurricane;
        }





    }


    /**
     * Devuelve array de instancias o recursos de dbpedia que tienen que ver
     * con el array de keywords pasadas.
     */
    public static List<String> lookupURIs(String[] terminos){


        String searchTerms = "";

        for(int i = 0; i < terminos.length; i++){

            if(i>0){
                searchTerms += " AND";

            }
            boolean isNumber = NumberUtils.isNumber(terminos[i]);

            if(isNumber){
                searchTerms += " \"" + terminos[i] + "\"";
            }
            else{
                searchTerms +=  terminos[i];
            }

        }


        System.out.println("Obtaining resources for keywords: " + searchTerms);
        String query = "select DISTINCT ?resource where \n" +
                "{" +
                " {" +
                "  {" +
                "   select ?s1 as ?resource where \n" +
                "     {" +
                "        quad map virtrdf:DefaultQuadMap" +
                "          {" +
                "            graph ?g" +
                "            {" +
                "              ?s1 ?s1textp ?o1 ." +
                "              ?s1 a  <http://dbpedia.org/class/yago/Hurricane111467018> ." +
                "              ?o1 bif:contains ' ( " + searchTerms  + "  ) ' option ( score ?sc ) ." +
                "            }" +
                "           }" +
                "         }" +
                "       order by desc ( ?sc * 3e-1 + sql:rnk_scale ( <LONG::IRI_RANK> ( ?s1 ) ) )" +
                "      }" +
                "     }" +
                "   }";

        try {
            Scanner s =  ejecutarConsultaSparql(query);
            List<String> result = new ArrayList<String>();
            s.next(); //Eliminar cabecera
            while (s.hasNextLine()) {

                String line = s.nextLine();
                if(line!=null && line.length()>0){
                    result.add(line.substring(1,line.length()-1));
                }


            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static Scanner ejecutarConsultaSparql(String consulta) throws IOException {

        String url_path = "http://dbpedia.org/sparql/query?output=tsv6format=text%2Ftab-separated-values&query=" + URLEncoder.encode(consulta, "UTF-8");
        URL url = new URL(url_path);
        return new Scanner(url.openStream());


    }
}
