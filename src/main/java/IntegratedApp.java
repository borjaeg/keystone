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
        String[] keywords = new String[]{name, year};
        List<Hurricane> hurricanes = DataRetriever.getHurricanesForKeywords(name,Integer.parseInt(year));

        NERProcessing nerp = new NERProcessing();
        for (Hurricane hurricane : hurricanes){
            System.out.println(hurricane.abstract_);

            String processed = nerp.process(hurricane.abstract_);
            Set<String> locations = Utils.getLocations(processed);
            ArrayList<Location> adminCodes = new ArrayList<Location>();
            for(String location : locations){
                adminCodes.add(GeonamesUtils.getData(location));
            }
            System.out.println("Full data: " + adminCodes);
        }
    }
}
