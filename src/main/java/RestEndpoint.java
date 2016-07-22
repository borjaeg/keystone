import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static spark.Spark.get;

/**
 * Created by ismaro3 on 22/07/16.
 */
public class RestEndpoint {

    static NERProcessing nerp = new NERProcessing();

    public static void main(String[] args) {
        get("/path", (req, res) -> {
            Set<String> params = req.queryParams();
            String name ="";
            Integer season = null;
            for(String param: params){

                if (param.equalsIgnoreCase("name")) {

                    name = req.queryParamsValues(param)[0];

                }
                if (param.equalsIgnoreCase("season")) {

                    season = Integer.parseInt(req.queryParamsValues(param)[0]);

                }





            }

            if(name==null || name.length()==0){
                return "Error, please add at least name";
            }
            else{
                return calculate(name,season);


               // return DataRetriever.getHurricanesForKeywords(new String[]{name,season});
            }




        });

        //calculate("Katrina",2005);
    }


    public static ArrayList<Location> calculate(String name, Integer year){
        List<Hurricane> hurricanes = DataRetriever.getHurricanesForKeywords(name,year);

        Set<String> uniqueLocations = new HashSet<>();

        for (Hurricane hurricane : hurricanes){

            String processed = nerp.process(hurricane.abstract_);   //Extract processed text
            Set<String> locations = Utils.getLocations(processed);
            //Put locations in uniqueLocations
            for(String loc: locations){
                uniqueLocations.add(loc);
            }
        }

        ArrayList<Location> adminCodes = new ArrayList<Location>();
        for(String location: uniqueLocations){
                System.out.println("Querying " + location);
                adminCodes.add(GeonamesUtils.getData(location));
        }
        return adminCodes;
    }

}
