package npe.com.restonpe.Beans;

/**
 * Bean which holds all the information of a single review.
 *
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 11/29/2016
 */
public class Review {

    private String title;
    private String content;
    private String rating;
    private String submitter;
    private String submitterEmail;
    private int likes;

    public Review(){
        title = "";
        content = "";
        rating = "";
        submitter = "";
        submitterEmail = "";
        likes = 0;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getSubmitterEmail() {
        return submitterEmail;
    }

    public void setSubmitterEmail(String submitterEmail) {
        this.submitterEmail = submitterEmail;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
