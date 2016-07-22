import org.geonames.*;

import java.util.List;

/**
 * Created by b3j90 on 21/07/16.
 */
public class GeonamesUtils {

    public static Location getData(String location){
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
            Toponym toponym = toponyms.get(0);
            // Split feature class name to get the first class
            location2 = new Location(toponym.getCountryName(), toponym.getAdminName1(),
                    toponym.getFeatureClassName().split(",")[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location2;

    }
}
