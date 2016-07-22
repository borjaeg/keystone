import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by b3j90 on 21/07/16.
 */
public class IntegratedApp {

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the hurricane name: ");
        String name = input.nextLine();
        System.out.print("Enter the hurricane year: ");
        String year = input.nextLine();
        // Checks MongoDB cache
        List<Location> retrieved = MongoUtils.retrieve(name, Integer.parseInt(year));
        if(retrieved != null){
            System.out.println(retrieved);
        } else {
            List<Hurricane> hurricanes = DataRetriever.getHurricanesForKeywords(name, new Integer(year));
            for (Hurricane hurricane : hurricanes){
                System.out.println(hurricane.abstract_);
                NERProcessing np = new NERProcessing();
                String processed = np.process(hurricane.abstract_);
                Set<String> locations = Utils.getLocations(processed);
                System.out.println("Locations: " + locations);
                ArrayList<Location> adminCodes = new ArrayList<Location>();
                for(String location : locations){
                    adminCodes.add(GeonamesUtils.getData(location));
                }
                System.out.println("Full data: " + adminCodes);
                // Inserts in MongoDB cache
                MongoUtils.insert(name, Integer.parseInt(year), adminCodes);
                break;
            }
        }
    }
}
