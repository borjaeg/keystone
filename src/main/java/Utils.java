import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by b3j90 on 21/07/16.
 */
public class Utils {

    public static Set<String> getLocations(String input){
        Scanner scanner = new Scanner(input);
        Set<String> locations = new HashSet<String>();
        while(scanner.hasNext()){
            String word = scanner.next();
            if(word.contains("<LOCATION>")){
                word = word.replace("<LOCATION>","");
                while(scanner.hasNext() && !word.contains("</LOCATION>")){
                    word = word + " " + scanner.next();
                }
                word = word.replace("</LOCATION>", "");
                word = word.replace(".", "");
                word = word.replace(",", "");
                locations.add(word);
            }
        }
        return locations;
    }
}
