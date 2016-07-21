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
public class KeywordSearch {

    public static void main(String[] args){



        List<String> recursos = lookupURIs(new String[]{"KATRINA","2015"});
        for(String recurso:recursos){
            System.out.println(recurso);
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


        System.out.println("Obteniendo recursos para keywords: " + searchTerms);
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
