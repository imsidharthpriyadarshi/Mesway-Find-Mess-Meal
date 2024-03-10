package in.mesway.Models;

public class LocationModels {
    private String main_location;
    private String secondary_location;

    public LocationModels(String main_location, String secondary_location) {
        this.main_location = main_location;
        this.secondary_location = secondary_location;
    }

    public String getMain_location() {
        return main_location;
    }

    public void setMain_location(String main_location) {
        this.main_location = main_location;
    }

    public String getSecondary_location() {
        return secondary_location;
    }

    public void setSecondary_location(String secondary_location) {
        this.secondary_location = secondary_location;
    }
}
