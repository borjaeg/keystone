import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Created by b3j90 on 21/07/16.
 */
public class IntegratedApp {

    static MongoUtils mongo = new MongoUtils();

    public static void main(String[] args){
        Logger mongoLogger = Logger.getLogger( "com.mongodb" );
        mongoLogger.setLevel(Level.SEVERE);
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the hurricane name: ");
        String name = input.nextLine().toLowerCase().trim();
        System.out.print("Enter the hurricane year: ");
        String year = input.nextLine().toLowerCase().trim();
        long time1 = System.currentTimeMillis();
        List<Location> retrieved;
        // Checks MongoDB cache
        if(year.length()>0) {
            retrieved = mongo.retrieve(name, Integer.parseInt(year));
        } else {
            retrieved = mongo.retrieve(name, null);
        }
        if(retrieved != null){
            System.out.println(retrieved);
        } else {
            List<Hurricane> hurricanes;
            if(year.length()>0) {
                hurricanes = DataRetriever.getHurricanesForKeywords(name, new Integer(year));
            } else {
                hurricanes = DataRetriever.getHurricanesForKeywords(name, null);
            }
            for (Hurricane hurricane : hurricanes){
                System.out.println(hurricane.abstract_);
                NERProcessing np = new NERProcessing();
                String processed = np.process(hurricane.abstract_);
                Set<String> locations = Utils.getLocations(processed);
                System.out.println("Locations: " + locations);
                ArrayList<Location> adminCodes = new ArrayList<Location>();
                for(String location : locations){
                    Location loc = GeonamesUtils.getData(location);
                    if(loc != null) {
                        adminCodes.add(loc);
                    }
                }
                System.out.println("Full data: " + adminCodes);
                // Inserts in MongoDB cache
                if(year.length()>0) {
                    mongo.insert(name, Integer.parseInt(year), adminCodes);
                } else {
                    mongo.insert(name, null, adminCodes);
                }
                CSVUtils.writeCSV(hurricane.label, hurricane.season, adminCodes);
                break;
            }
        }
        long time2 = System.currentTimeMillis();
        System.out.println("Total time: " + ((time2-time1)/1000.0) + " s.");

    }
}
