import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by piraces on 22/7/16.
 */
public class LocationCodecProvider implements CodecProvider {
        @SuppressWarnings("unchecked")
        public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
            if (clazz == Location.class) {
                return (Codec<T>) new LocationCodec(registry);
            }
            return null;
        }
}
