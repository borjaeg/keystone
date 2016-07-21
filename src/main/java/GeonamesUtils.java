import org.geonames.*;

import java.util.Set;

/**
 * Created by b3j90 on 21/07/16.
 */
public class GeonamesUtils {

    public static String getAdminName(String location){
        //First, must be registered on Geonames. Then, place the user here...
        WebService.setUserName("piraces");

        ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
        searchCriteria.setQ(location);
        searchCriteria.setStyle(Style.FULL);
        ToponymSearchResult searchResult;
        String adminCode1 = "";

        try {
            searchResult = WebService.search(searchCriteria);
            for (Toponym toponym : searchResult.getToponyms()) {
                adminCode1 = toponym.getAdminName1();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminCode1;
    }
}
