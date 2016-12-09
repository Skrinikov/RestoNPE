package npe.com.restonpe.Beans;

/**
 * Bean which holds all the information of a single review.
 *
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 11/29/2016
 */
public class Review {

    private long id;
    private String title;
    private String content;
    private double rating;
    private String submitter;
    private String submitterEmail;
    private int likes;

    /**
     * Creates a Review bean with the given fields.
     */
    public Review(long id, String title, String content, double rating, String submitter, String submitterEmail, int likes) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.submitter = submitter;
        this.submitterEmail = submitterEmail;
        this.likes = likes;
    }

    /**
     * Creates a Review bean.
     */
    public Review() {
        id = 0;
        title = "";
        content = "";
        rating = 0;
        submitter = "";
        submitterEmail = "";
        likes = 0;
    }

    /**
     * Gets the id of the Review.
     *
     * @return The id of the Review.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id of the Review.
     *
     * @param id The new id of the Review.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the title of the Review.
     *
     * @return The title of the Review.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the Review.
     *
     * @param title The new title of the Review.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the contents of the Review.
     *
     * @return The contents of the Review.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the contents of the Review.
     *
     * @param content The new content of the Review.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the rating of the Review.
     *
     * @return The rating of the Review.
     */
    public double getRating() {
        return rating;
    }

    /**
     * Sets the rating of the Review.
     *
     * @param rating The new rating of the Review.
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Gets the submitter of the Review.
     *
     * @return The submitter of the Review.
     */
    public String getSubmitter() {
        return submitter;
    }

    /**
     * Sets the submitter of the Review.
     *
     * @param submitter The new submitter of the Review.
     */
    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    /**
     * Gets the Review's submitter's email.
     *
     * @return The Review's submitter's email.
     */
    public String getSubmitterEmail() {
        return submitterEmail;
    }

    /**
     * Sets the Review's submitter's email.
     *
     * @param submitterEmail The new email of the Review's submitter.
     */
    public void setSubmitterEmail(String submitterEmail) {
        this.submitterEmail = submitterEmail;
    }

    /**
     * Gets the number of likes this Review has received.
     *
     * @return The number of likes this Review has received.
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Sets the number of likes of the Review.
     *
     * @param likes The new number of likes of the Review.
     */
    public void setLikes(int likes) {
        this.likes = likes;
    }
}
