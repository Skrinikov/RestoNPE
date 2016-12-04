package npe.com.restonpe.Beans;

/**
 * A bean which holds full address information for a single given location.
 *
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 11/24/2016
 */
public class Address {

    private String address;
    private String province;
    private String city;
    private String country;
    private String postal;
    private double latitude;
    private double longitude;
    private int suite;

    /**
     * Default constructor for the bean. Initializes all variables to default values.
     */
    public Address(){
        address = "";
        city = "";
        country = "";
        latitude = 0;
        longitude = 0;
        suite = 0;
        province = "";
    }

    /**
     * Returns the address. The address is formatted as: civic address Street name, City,
     * Province/State Postal/ZIP code
     *
     * @return The string representation of the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address. The format of the address should be: civic address Street name, City,
     * Province/State Postal/ZIP code
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
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
     * Returns the value of the instance suite variable.
     *
     * @return value of the internal suite variable.
     */
    public int getSuite() {
        return suite;
    }

    /**
     * Sets the instance suite variable with the provided int.
     *
     * @param suit
     */
    public void setSuite(int suit) {
        this.suite = suit;
    }

    /**
     * Fetches the postal code from the bean.
     *
     * @return string which holds the postal code for the given address.
     */
    public String getPostal() {
        return postal;
    }

    /**
     * Changes the value of the postal code with the provided value.
     *
     * @param postal new value for the postal code.
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
