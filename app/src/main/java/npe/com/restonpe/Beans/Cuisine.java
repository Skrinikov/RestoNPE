package npe.com.restonpe.Beans;

/**
 * A data bean which holds all the information for a single cuisine.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 04/12/2016
 */
public class Cuisine {
    private int id;
    private String name;

    /**
     * Creates a bean with default, empty values.
     */
    public Cuisine() {
        id = 0;
        name = "";
    }

    /**
     * Creates a bean with the given name.
     *
     * @param name The name to use.
     */
    public Cuisine(String name) {
        id = 0;
        this.name = name;
    }

    /**
     * Gets the id of the cuisine
     *
     * @return The id of the cuisine
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the cuisine
     *
     * @param id The new id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the cuisine
     *
     * @return The name of the cuisine
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the cuisine
     *
     * @param name The new name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public  String toString() {
        return name;
    }
}
