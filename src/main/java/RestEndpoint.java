import java.util.ArrayList;
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
                return "[{\"city\":\"Zaragoza\"}]";


               // return DataRetriever.getHurricanesForKeywords(new String[]{name,season});
            }




        });
    }


    public static void calculate(String name, Integer year){
        List<Hurricane> hurricanes = DataRetriever.getHurricanesForKeywords(name,year);

        for (Hurricane hurricane : hurricanes){


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
