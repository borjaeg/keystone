import org.geonames.*;

import java.util.List;

/**
 * Created by b3j90 on 21/07/16.
 */
public class GeonamesUtils {

    public static Location getData(String location){
        //First, must be registered on Geonames. Then, place the user here...
        WebService.setUserName("piraces");

        ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
        searchCriteria.setQ(location);
        searchCriteria.setStyle(Style.FULL);
        ToponymSearchResult searchResult;
        Location location2 = new Location();

        try {
            /**for (Toponym toponym : searchResult.getToponyms()) {
             adminCode1 = toponym.getAdminName1();
             break;
             }**/

            searchResult = WebService.search(searchCriteria);
            List<Toponym> toponyms = searchResult.getToponyms();
            Toponym toponym = toponyms.get(0);
            location2 = new Location(toponym.getCountryName(), toponym.getAdminName1(), toponym.getFeatureClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location2;

    }
}
