import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by b3j90 on 21/07/16.
 */
public class App {

    public static ArrayList<String> getLocations(String input){
        Scanner scanner = new Scanner(input);
        ArrayList<String> locations = new ArrayList<String>();
        while(scanner.hasNext()){
            String word = scanner.next();
            if(word.contains("<LOCATION>")){
                word = word.replace("<LOCATION>","");
                while(scanner.hasNext() && !word.contains("</LOCATION>")){
                    word = word + " " + scanner.next();
                }
                word = word.replace("</LOCATION>", "");
                locations.add(word);
            }
        }
        return locations;
    }


    public static void main(String[] args) {
        Model m = FileManager.get().loadModel("Hurricane_Katrina.rdf");
        StmtIterator it =  m.listStatements();
        while (it.hasNext()) {
            Statement stmt = it.next();
            if (stmt.getPredicate().toString().equals("http://dbpedia.org/ontology/abstract")){
                // Silly Heuristic to filter english abstracts
                if (stmt.getObject().toString().contains("hurricane")){
                    String processed = NERProcessing.process(stmt.getObject().toString());
                    System.out.println(getLocations(processed));
                }
            }
        }
    }

}
