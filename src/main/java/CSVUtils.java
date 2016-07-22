import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by piraces on 22/7/16.
 */
public class CSVUtils {

    public static void writeCSV(String name, Integer season, ArrayList<Location> locations){
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(name + ".csv"), ',');
            // feed in your array (or convert your data to an array)
            String[] entries = {"stormname", "season", "country", "name_1", "type_1"};
            writer.writeNext(entries);
            for(Location location : locations){
                String[] entries2 = {name, season.toString(), location.getCountry(), location.getName(),
                        location.getType()};
                writer.writeNext(entries2);
            }
            writer.close();
        } catch(Exception ex){
            System.out.println("Error writing to CSV");
        }
    }
}
