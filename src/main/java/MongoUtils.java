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

    public static MongoCollection<Document> connect(){
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(new LocationCodecProvider()),
                MongoClient.getDefaultCodecRegistry());
        MongoClientOptions options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry).build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(), options);
        MongoDatabase database = mongoClient.getDatabase("hurricane");
        MongoCollection<Document> collection = database.getCollection("queries");
        return collection;
    }

    public static void insert(String keyword, String season, ArrayList<Location> locations){
        Document doc = new Document("keyword", keyword)
                .append("season", season)
                .append("locations", locations);

        connect().insertOne(doc);
    }

    public static ArrayList<Location> retrieve(String keyword, String season){
        Document myDoc = connect().find(and(eq("keyword", keyword), eq("season", season))).first();
        try{
            return (ArrayList<Location>) myDoc.get("locations");
        } catch(Exception ex){
            return null;
        }

    }
}