import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

/**
 * Created by ismaro3 on 22/07/16.
 */
public class RestEndpoint {

    static NERProcessing nerp = new NERProcessing();
    static Gson gson = new Gson();

    public static void main(String[] args) {

        staticFileLocation( "/web" );

        before((request, response) -> response.type("application/json"));

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
                return gson.toJson(calculate(name,season));


               // return DataRetriever.getHurricanesForKeywords(new String[]{name,season});
            }




        });

        //calculate("Katrina",2005);
    }


    public static List<Location> calculate(String name, Integer year){

        List<Location> result = MongoUtils.retrieve(name,year);
        if(result!=null){
            System.out.println("Already in mongo!");
            return result;
        }

        System.out.println("Not in mongoDB... calculating");

        List<Hurricane> hurricanes = DataRetriever.getHurricanesForKeywords(name,year);

        Set<String> uniqueLocations = new HashSet<>();

        for (Hurricane hurricane : hurricanes){ //De cada huracán, obtenemos sus localizaciones en texto.

            String processed = nerp.process(hurricane.abstract_);   //Extract processed text
            Set<String> locations = Utils.getLocations(processed);
            //Put locations in uniqueLocations
            for(String loc: locations){
                uniqueLocations.add(loc);
            }
        }

        Set<Location> adminCodes = new HashSet<Location>();
        for(String location: uniqueLocations){
                adminCodes.add(GeonamesUtils.getData(location));
        }

        result = new ArrayList<Location>();
        result.addAll(adminCodes);

        MongoUtils.insert(name,year,result);
        return result;
    }

}
