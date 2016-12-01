package npe.com.restonpe.Beans;

/**
 * Represents a fraction of a real Resto bean. Used to display limited information about the restaurant.
 *
 * @author Danieil Skrinikov
 * @version 1.0
 * @since 11-29-2016
 */
public class RestoItem {
    private int id;
    private String name;
    private String priceRange;
    private String city;
    private double rating;
    private double latitude;
    private double longitude;
    private String address;


    public RestoItem(){
        id = -1;
        name = "";
        priceRange = "";
        city = "";
        rating = 0;
        latitude = 0;
        longitude = 0;
        address = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
}
