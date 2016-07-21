import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

/**
 * Created by b3j90 on 21/07/16.
 */
public class App {

    public static void main(String[] args) {

        Model m = FileManager.get().loadModel("Hurricane_Katrina.rdf");
        StmtIterator it =  m.listStatements();
        while (it.hasNext()) {
            Statement stmt = it.next();

            if (stmt.getPredicate().toString().equals("http://dbpedia.org/ontology/abstract")){
                // Silly Heuristic to filter english abstracts
                if (stmt.getObject().toString().contains("hurricane"))
                    System.out.println(stmt.getObject().toString());
            }
        }
    }
}
