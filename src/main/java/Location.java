/**
 * Created by piraces on 21/7/16.
 */
public class Location {

    String original;
    String country;
    String name;
    String type;

    public Location(String original, String country, String name, String type){
        this.country = country;
        this.name = name;
        this.type = type;
        this.original = original;
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

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    @Override
    public String toString() {
        return "Location{" +
                "original='" + original + '\'' +
                ", country='" + country + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (country != null ? !country.equals(location.country) : location.country != null) return false;
        if (name != null ? !name.equals(location.name) : location.name != null) return false;
        return type != null ? type.equals(location.type) : location.type == null;

    }

    @Override
    public int hashCode() {
        int result = country != null ? country.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}

