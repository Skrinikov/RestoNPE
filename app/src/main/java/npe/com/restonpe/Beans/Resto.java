package npe.com.restonpe.Beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean which holds information of a single restaurant. Provides getters and setters for all the
 * fields.
 *
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 11/24/2016
 */
public class Resto {
    private long localId;
    private long zomatoId;
    private long herokuId;

    // Resto
    private String name;
    private String genre;
    private String priceRange;
    private String email;
    private String link;
    private long phone;
    private Address address;
    private List<Review> reviews;

    // Submitter info
    private String submitterEmail;
    private String submitterName;

    /**
     * Default constructor, sets the variables to default values and initializes
     * other beans.
     */
    public Resto() {
        localId = -1;
        zomatoId = -1;
        herokuId = -1;
        name = "";
        genre = "";
        priceRange = "";
        email = "";
        link = "";
        phone = 0;
        address = new Address();
        reviews = new ArrayList<>();
    }

    /**
     * Gets the local database's id. This id could be -1 if this RestoItem does not belong to the local database
     *
     * @return The local database's id for this RestoItem, or -1 if it does not belong to the local database
     */
    public long getId() {
        return localId;
    }

    /**
     * Sets the local database's id for this RestoItem
     *
     * @param id The new local database id
     */
    public void setId(long id) {
        this.localId = id;
    }

    /**
     * Gets the Zomato id. This id could be -1 if this RestoItem does not belong to Zomato
     *
     * @return Zomato's id for this RestoItem, or -1 if it does not belong to Zomato
     */
    public long getZomatoId() {
        return zomatoId;
    }

    /**
     * Sets Zomato's id for this RestoItem
     *
     * @param id The new zomato id
     */
    public void setZomatoId(long id) {
        this.zomatoId = id;
    }

    /**
     * Gets the Heroku id. This id could be -1 if this RestoItem does not belong to Heroku
     *
     * @return Heroku's id for this RestoItem, or -1 if it does not belong to Heroku
     */
    public long getHerokuId() {
        return herokuId;
    }

    /**
     * Sets Heroku's id for this RestoItem
     *
     * @param id The new Heroku id
     */
    public void setHerokuId(long id) {
        this.herokuId = id;
    }

    /**
     * Gets the name of the restaurant.
     *
     * @return name of restaurant.
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the restaurant.
     *
     * @param name new name for the restaurant.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the genre for this restaurant.
     *
     * @return the genre of the restaurant.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Changes the genre of this restaurant with the provided genre.
     *
     * @param genre new genre for the restaurant.
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the price range of this restaurant in a String.
     *
     * @return price range of the restaurant.
     */
    public String getPriceRange() {
        return priceRange;
    }

    /**
     * Changes the current price range of the restaurant with the new provided
     * price range.
     *
     * @param priceRange new price range for the restaurant.
     */
    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    /**
     * Gets the email address of the restaurant.
     *
     * @return email address of the restaurant.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changes the email address of the restaurant with a new provided email address.
     *
     * @param email new email address for the restaurant.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the web link to this restaurant's web page.
     *
     * @return string which holds the address link to the restaurant's website.
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the web link of the restaurant with the given web link.
     *
     * @param link new web link for the restaurant.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets the restaurant's phone number.
     *
     * @return the phone number of the restaurant.
     */
    public long getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the restaurant.
     *
     * @param phone new phone number of the restaurant.
     */
    public void setPhone(long phone) {
        this.phone = phone;
    }

    /**
     * Returns a list of addresses
     *
     * @return all the addresses that match this restaurant.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Adds a single address to this restaurant.
     *
     * @param address single address for the restaurant.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Returns a list of reviews for this restaurant.
     *
     * @return
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * Sets the reviews for this restaurant
     *
     * @param reviews list of reviews.
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Returns the email of the submitter.
     *
     * @return String which holds the email.
     */
    public String getSubmitterEmail() {
        return submitterEmail;
    }

    /**
     * Sets the email address of the submitter. So the add can be attributed
     *
     * @param email new email for this object.
     */
    public void setSubmitterEmail(String email) {
        this.submitterEmail = email;
    }

    /**
     * Gets the name of the person/company who submitted it.
     *
     * @return
     */
    public String getSubmitterName() {
        return submitterName;
    }

    /**
     * Sets the name of the person who submitted it.
     *
     * @param submitterName
     */
    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String toString() {
        String s = "Resto:\n" +
                "" +
                "\tName: " + name + "\n" +
                "\tGenre: " + genre + "\n" +
                "\tP-R: " + priceRange + "\n" +
                "\tEmail: " + email + "\n" +
                "\tLink: " + link + "\n" +
                "\tPhone: " + phone + "\n" +
                "\tSubmitter Email: " + submitterEmail + "\n" +
                "\tSubmiter name: " + submitterName + "\n" +
                "\tAddress: " + address.toString() + "\n" +
                "\tReviews: " + reviews.toString() + "\n";
        return s;
    }
}
