import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by piraces on 22/7/16.
 */
public class MongoUtils {

    public static MongoCollection<Document> connect(String col){
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(new LocationCodecProvider()),
                MongoClient.getDefaultCodecRegistry());
        MongoClientOptions options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(), options);
        MongoDatabase database = mongoClient.getDatabase("hurricane");
        MongoCollection<Document> collection = database.getCollection(col);
        return collection;
    }

    public static void insert(String keyword, Integer season, ArrayList<Location> locations){
        Document doc = new Document("keyword", keyword)
                .append("season", season)
                .append("locations", locations);

        connect("queries").insertOne(doc);
    }

    public static ArrayList<Location> retrieve(String keyword, Integer season){
        Document myDoc = connect("queries").find(and(eq("keyword", keyword), eq("season", season))).first();
        try{
            return (ArrayList<Location>) myDoc.get("locations");
        } catch(Exception ex){
            return null;
        }

    }

    public static void insertGeonames(Location location){
        Document doc = new Document("name", location.getName())
                .append("country", location.getCountry())
                .append("type", location.getType())
                .append("original", location.getOriginal())
                .append("lat", location.getLat())
                .append("lon", location.getLon());
        connect("geonames").insertOne(doc);
    }

    public static ArrayList<String> retrieveGeonames(String keyword){
        Document myDoc = connect("geonames").find(eq("original", keyword)).first();
        try{
            ArrayList<String> result = new ArrayList<String>();
            result.add(myDoc.getString("country"));
            result.add(myDoc.getString("name"));
            result.add(myDoc.getString("type"));
            result.add(myDoc.getDouble("lat").toString());
            result.add(myDoc.getDouble("lon").toString());
            return result;
        } catch(Exception ex){
            return null;
        }

    }
}
