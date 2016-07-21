import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by ismaro3 on 21/07/16.
 */
public class DataRetriever {


    public static void main(String[] args){


    }


    public static String getAbstract(String instanceURI){

        return "hola";



    }

    private static Scanner ejecutarConsultaSparql(String consulta) throws IOException {

        String url_path = "http://dbpedia.org/sparql/query?output=tsv6format=text%2Ftab-separated-values&query=" + URLEncoder.encode(consulta, "UTF-8");
        URL url = new URL(url_path);
        return new Scanner(url.openStream());


    }
}
