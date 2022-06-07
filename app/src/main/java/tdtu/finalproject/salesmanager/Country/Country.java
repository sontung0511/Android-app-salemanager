package tdtu.finalproject.salesmanager.Country;

public class Country {
    String countryname,timezone;

    public Country(){}

    public Country(String countryname, String timezone) {
        this.countryname = countryname;
        this.timezone = timezone;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
