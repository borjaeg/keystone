import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;


public class LocationCodec implements Codec<Location> {

    private CodecRegistry codecRegistry;

    public LocationCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    public Location decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        String name = reader.readString("name");
        String country = reader.readString("country");
        String type = reader.readString("type");
        String original = reader.readString("original");
        Double lat = reader.readDouble("lat");
        Double lon = reader.readDouble("lon");
        reader.readEndDocument();

        Location user = new Location(original, country, name, type, lat, lon);
        return user;
    }

    public void encode(BsonWriter writer, Location user, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeName("original");
        writer.writeString(user.getOriginal());
        writer.writeName("name");
        writer.writeString(user.getName());
        writer.writeName("country");
        writer.writeString(user.getCountry());
        writer.writeName("type");
        writer.writeString(user.getType());
        writer.writeName("lat");
        writer.writeDouble(user.getLat());
        writer.writeName("lon");
        writer.writeDouble(user.getLon());
        writer.writeEndDocument();
    }

    public Class<Location> getEncoderClass() {
        return Location.class;
    }
}