import org.geonames.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by b3j90 on 21/07/16.
 */
public class GeonamesUtils {

    public static Location getData(String location){
        ArrayList<String> retrieved = MongoUtils.retrieveGeonames(location);
        if(retrieved != null){
            return new Location(location, retrieved.get(0), retrieved.get(1), retrieved.get(2),
                    new Double(retrieved.get(3)), new Double(retrieved.get(4)));
        } else {
            // User enabled for consulting the Geonames API
            WebService.setUserName("piraces");

            ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setQ(location);
            searchCriteria.setStyle(Style.FULL);
            ToponymSearchResult searchResult;
            Location location2 = new Location();

            try {
                searchResult = WebService.search(searchCriteria);
                List<Toponym> toponyms = searchResult.getToponyms();
                // Gets the first toponym in search (best match)
                if (toponyms.size() > 0) {
                    Toponym toponym = toponyms.get(0);
                    // Split feature class name to get the first class
                    location2 = new Location(location, toponym.getCountryName(), toponym.getAdminName1(),
                            toponym.getFeatureClassName().split(",")[0], toponym.getLatitude(), toponym.getLongitude());
                } else {
                    location2 = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            MongoUtils.insertGeonames(location2);
            return location2;
        }
    }
}
