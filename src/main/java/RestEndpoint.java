import com.google.gson.Gson;
import com.google.gson.internal.Excluder;

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
    static MongoUtils mongo = new MongoUtils();

    public static void main(String[] args) {

        



        staticFileLocation("/web");


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
                    try{
                        season = Integer.parseInt(req.queryParamsValues(param)[0]);
                    } catch(Exception ex){}
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

        ArrayList<Location> result = mongo.retrieve(name,year);
        if(result!=null){
            System.out.println("Already in mongo!");
            return result;
        }

        System.out.println("Not in mongoDB... calculating");

        List<Hurricane> hurricanes = DataRetriever.getHurricanesForKeywords(name,year).subList(0,1);

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
            Location loc = GeonamesUtils.getData(location);
            if(loc != null) {
                adminCodes.add(GeonamesUtils.getData(location));
            }
        }

        result = new ArrayList<Location>();
        result.addAll(adminCodes);

        mongo.insert(name,year,result);
        return result;
    }

}
