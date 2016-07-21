/**
 * Created by piraces on 21/7/16.
 */
public class Location {

    String country;
    String name;
    String type;

    public Location(String country, String name, String type){
        this.country = country;
        this.name = name;
        this.type = type;
    }

    public Location(){}

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Location{" +
                "country='" + country + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
