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


        List<Hurricane> list = getHurricanesForKeywords("katrina",null);

        for(Hurricane h:list){
            System.out.println(h.season);
            System.out.println();
        }

    }



    public static List<Hurricane> getHurricanesForKeywords(String name, Integer season){

        List<String> instanceURIs = lookupURIs(name,season);
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


        String query = "select ?abstract ?areas ?season ?label" +
                "{" +
                  "<" + instanceURI + "> <http://dbpedia.org/ontology/abstract> ?abstract ." +
                  "<" + instanceURI + "> <http://dbpedia.org/property/areas> ?areas ." +
                "<" + instanceURI + "> <http://dbpedia.org/property/hurricaneSeason> ?season ." +
                "<" + instanceURI + "> <http://www.w3.org/2000/01/rdf-schema#label> ?label ." +
                  "FILTER (lang(?abstract) = 'en')" +
                 "FILTER (lang(?label) = 'en')" +
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

                if(terms.length>2 && terms[2].length()>0){

                    try{
                        hurricane.season = Integer.parseInt(terms[2]);
                    }
                    catch(Exception ex){
                        hurricane.season = null;
                    }

                }

                if(terms.length>3){
                    hurricane.label = terms[3].substring(1,terms[3].length()-1);
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
    public static List<String> lookupURIs(String name, Integer season){



        System.out.println("Obteniendo recursos para nombre " + name + " y año " + season);


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
                "              ?s1 a  <http://dbpedia.org/class/yago/Hurricane111467018> .";

                if(season!=null){
                    query+=  "  ?s1 <http://dbpedia.org/property/hurricaneSeason> \"" + season + "\"^^xsd:integer.";
                }

               query+= "    ?o1 bif:contains ' ( " + name  + "  ) ' option ( score ?sc ) ." +
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
