package npe.com.restonpe.Beans;

/**
 * A bean which holds full address information for a single given location.
 *
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 11/24/2016
 */
public class Address {

    private int civic;
    private String street;
    private String city;
    private String country;
    private double latitude;
    private double longitude;
    private int suit;

    /**
     * Default constructor for the bean. Initializes all variables to default values.
     */
    public Address(){
        civic = 0;
        street = "";
        city = "";
        country = "";
        latitude = 0;
        longitude = 0;
        suit = 0;
    }
    
    /**
     * Returns the civic address of the address.
     * 
     * @return value of the civic number.
     */
    public int getCivic() {
        return civic;
    }
    
    /**
     * Sets the civic number for the address. 
     * 
     * @param civic civic number of the address.
     */
    public void setCivic(int civic) {
        this.civic = civic;
    }
    
    /**
     * Returns the street name of the address.
     * 
     * @return name of the street.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street for the address.
     * 
     * @param street new street name for the address.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the city of this address.
     * 
     * @return the city where this address is.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the internal city variable with the provided value.
     * 
     * @param city new city for the address.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the value of the internal country variable.
     * 
     * @return value of the country for this address.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country for the address with the provided String value.
     *
     * @param country new country for the object.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns the value of the internal latitude variable for the address.
     *
     * @return value of the internal latitude variable.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the internal latitude variable with the provided double value.
     *
     * @param latitude double which represents the latitude of the address.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the value of the internal longitude variable for the address.
     *
     * @return value of the internal longitude variable.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the internal longitude variable with the provided value.
     *
     * @param longitude double which represents the longitude of said address.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the value of the instance suit variable.
     *
     * @return value of the internal suit variable.
     */
    public int getSuit() {
        return suit;
    }

    /**
     * Sets the instance suit variable with the provided int.
     *
     * @param suit
     */
    public void setSuit(int suit) {
        this.suit = suit;
    }

}
