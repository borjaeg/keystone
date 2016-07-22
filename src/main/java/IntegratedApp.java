import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by b3j90 on 21/07/16.
 */
public class IntegratedApp {

    public static void main(String[] args){

        String[] keywords = new String[]{"katrina"};

         List<Hurricane> hurricanes = DataRetriever.getHurricanesForKeywords(keywords);

        for (Hurricane hurricane : hurricanes){
            System.out.println(hurricane.abstract_);
            String processed = NERProcessing.process(hurricane.abstract_);
            Set<String> locations = Utils.getLocations(processed);
            System.out.println("Locations: " + locations);
            ArrayList<Location> adminCodes = new ArrayList<Location>();
            for(String location : locations){
                adminCodes.add(GeonamesUtils.getData(location));
            }
            System.out.println("Full data: " + adminCodes);
        }
    }
}
