package npe.com.restonpe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import npe.com.restonpe.Beans.Address;
import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.Beans.Review;

/**
 * Handles the creation and maintenance of the database.
 * Provides data querying methods for the resto app context..
 *
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 11/23/2016
 */
public class RestoDAO extends SQLiteOpenHelper {

    // Tag
    private static String TAG = "RestoDAO";

    // Instance to share the database.
    private static RestoDAO dbh;

    // Database related information.
    private static final String DATABASE_NAME = "resto.db";
    private static final int DATABASE_VERSION = 4;

    // Table names
    private static final String TABLE_GENRE = "genre";
    private static final String TABLE_RESTO = "resto";
    private static final String TABLE_ADDRESS = "address";
    private static final String TABLE_REVIEW = "review";
    private static final String TABLE_USERS = "user";

    // Shared column names between tables
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CREATED = "created";
    private static final String COLUMN_MODIFIED = "modified";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USER_FK = "user_id";
    private static final String COLUMN_RESTO_FK = "resto_id";
    private static final String COLUMN_POSTAL = "postal";

    // Genre table
    private static final String COLUMN_GENRE = "genre_name";

    // Resto table
    private static final String COLUMN_ZOMATO_ID = "zomato_id";
    private static final String COLUMN_HEROKU_ID = "heroku_id";
    private static final String COLUMN_RESTO_NAME = "resto_name";
    private static final String COLUMN_PRICE_RANGE = "resto_price_range";
    private static final String COLUMN_LINK = "link";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_GENRE_FK = "genre_id";

    // Address table
    private static final String COLUMN_CIVIC = "civic";
    private static final String COLUMN_STREET_ADDRESS = "street_address";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_PROVINCE = "province";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LONG = "long";
    private static final String COLUMN_SUITE = "suite";

    // User Table
    private static final String COLUMN_USERNAME = "username";

    // Review Table
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_LIKES = "likes";

    // Create Tables Strings
    private static final String CREATE_USERS = "create table " + TABLE_USERS + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_USERNAME + " text not null, " +
            COLUMN_EMAIL + " text not null unique);";

    private static final String CREATE_GENRE = "create table " + TABLE_GENRE + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_GENRE + " text not null);";

    private static final String CREATE_RESTO = "create table " + TABLE_RESTO + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_ZOMATO_ID + " integer default -1, " +
            COLUMN_HEROKU_ID + " integer default -1, " +
            COLUMN_RESTO_NAME + " text not null, " +
            COLUMN_PRICE_RANGE + " text, " +
            COLUMN_PHONE + " integer, " +
            COLUMN_EMAIL + " text, " +
            COLUMN_CREATED + " datetime default current_timestamp, " +
            COLUMN_MODIFIED + " datetime default current_timestamp, " +
            COLUMN_USER_FK + " integer, " +
            COLUMN_GENRE_FK + " integer, " +
            COLUMN_LINK + " text, " +
            "FOREIGN KEY(" + COLUMN_USER_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE);" +
            "FOREIGN KEY(" + COLUMN_GENRE_FK + ") REFERENCES " + TABLE_RESTO + "(" + COLUMN_ID + ") ON DELETE CASCADE);";

    private static final String CREATE_ADDRESS = "create table " + TABLE_ADDRESS + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_STREET_ADDRESS + " text null, " +
            COLUMN_COUNTRY + " text null, " +
            COLUMN_CITY + " text null, " +
            COLUMN_PROVINCE + " text null, " +
            COLUMN_LONG + " real null, " +
            COLUMN_LAT + " real null, " +
            COLUMN_POSTAL + " text null, " +
            COLUMN_SUITE + " text, " +
            COLUMN_RESTO_FK + " integer, " +
            "FOREIGN KEY (" + COLUMN_RESTO_FK + ") REFERENCES " + TABLE_RESTO + "(" + COLUMN_ID + ") ON DELETE CASCADE);";

    private static final String CREATE_REVIEW = "create table " + TABLE_REVIEW + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_TITLE + " text, " +
            COLUMN_CONTENT + " text not null, " +
            COLUMN_RATING + " real not null, " +
            COLUMN_LIKES + " integer not null, " +
            COLUMN_USER_FK + " integer, " +
            COLUMN_RESTO_FK + " integer, " +
            "FOREIGN KEY (" + COLUMN_USER_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ") ON DELETE CASCADE, " +
            "FOREIGN KEY (" + COLUMN_RESTO_FK + ") REFERENCES " + TABLE_RESTO + "(" + COLUMN_ID + ") ON DELETE CASCADE);";

    // Drop tables
    private static final String DROP_REVIEW = "DROP TABLE IF EXISTS " + TABLE_REVIEW + " ;";
    private static final String DROP_ADDRESS = "DROP TABLE IF EXISTS " + TABLE_ADDRESS + " ;";
    private static final String DROP_RESTO = "DROP TABLE IF EXISTS " + TABLE_RESTO + " ;";
    private static final String DROP_USER = "DROP TABLE IF EXISTS " + TABLE_USERS + " ;";
    private static final String DROP_GENRE = "DROP TABLE IF EXISTS " + TABLE_GENRE + " ;";

    // Query Strings
    private static final String GET_GENRE = COLUMN_GENRE + "=?";
    private static final String GET_USER = COLUMN_EMAIL + "=?";

    /**
     * Constructor for the object. Private just to let the factory method to initialize the
     * object.
     *
     * @param context The activity that instantiate this object.
     */
    private RestoDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Factory method to create a RestoDAO object. Only creates the database if there is
     * only one reference to it.
     *
     * @param context The activity that instantiated this object.
     * @return RestoDAO database object ready to use.
     */
    public static RestoDAO getDatabase(Context context) {
        /*
         * Use the application context, which will ensure that you don't
		 * accidentally leak an Activity's context.
		 *
		 * See this article for more
		 * information: http://bit.ly/6LRzfx
		 *
		 * Taken from Tricia at: https://github.com/Android518-2016/week09-SQLite-database
		 */
        if (dbh == null) {
            dbh = new RestoDAO(context.getApplicationContext());
            Log.i(TAG, "getDBHelper, dbh == null");
        }
        Log.i(TAG, "getDBHelper()");
        return dbh;
    } // getDBHelper()

    /**
     * Only used when the database is created for the first time.
     *
     * @param database The database to create.
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_GENRE);
        database.execSQL(CREATE_USERS);
        database.execSQL(CREATE_RESTO);
        database.execSQL(CREATE_ADDRESS);
        database.execSQL(CREATE_REVIEW);
        Log.i(TAG, "onCreate() - Created all needed tables.");
    }

    /**
     * Drops all tables from the database if they exists and then calls the onCreate method in order
     * to rebuild them in a fashion which is supported by the new version.
     *
     * @param database   link to the device internal sqlite database.
     * @param oldVersion integer which represents the old version.
     * @param newVersion integer which represents the new version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to version " + newVersion + ". All data" +
                " will be destroyed");

        database.execSQL(DROP_REVIEW);
        database.execSQL(DROP_ADDRESS);
        database.execSQL(DROP_RESTO);
        database.execSQL(DROP_USER);
        database.execSQL(DROP_GENRE);

        onCreate(database);
        Log.i(TAG, "onUpgrade()");
    }

    /**
     * Inserts the restaurant into the restaurant table.
     *
     * @param resto The restaurant to add
     */
    public long addRestaurant(Resto resto) throws IllegalArgumentException {
        long restoId = insertResto(resto);
        insertAddress(resto.getAddress(), restoId);
        insertReviews(resto.getReviews(), restoId);
        return restoId;
    }

    /**
     * Fetches a small portion of the information on all restaurants in a database and stores it into
     * a list.
     *
     * @return limited data for each restaurant in the database.
     */
    public List<RestoItem> getAllRestaurantsSmall() {
        Cursor c = getReadableDatabase().query(TABLE_RESTO, new String[]{COLUMN_ID, COLUMN_ZOMATO_ID, COLUMN_HEROKU_ID, COLUMN_RESTO_NAME, COLUMN_PRICE_RANGE, COLUMN_PHONE}, null, null, null, null, null);
        List<RestoItem> restos = new ArrayList<>();
        RestoItem temp;
        while (c.moveToNext()) {
            temp = new RestoItem();
            temp.setId(c.getLong(c.getColumnIndex(COLUMN_ID)));
            temp.setZomatoId(c.getLong(c.getColumnIndex(COLUMN_ZOMATO_ID)));
            temp.setHerokuId(c.getLong(c.getColumnIndex(COLUMN_HEROKU_ID)));
            temp.setName(c.getString(c.getColumnIndex(COLUMN_RESTO_NAME)));
            temp.setPriceRange(c.getString(c.getColumnIndex(COLUMN_PRICE_RANGE)));
            temp.setPhone(c.getLong(c.getColumnIndex(COLUMN_PHONE)));
            getAddressForResto(temp);
            getRatingForResto(temp);

            restos.add(temp);
        }
        c.close();
        Log.i(TAG, "getAllRestaurantsSmall(), Size: " + restos.size());

        return restos;
    }


    /**
     * Gets a list of RestoItems by name.
     *
     * @param name The name of the resto
     */
    public List<RestoItem> getRestoByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        Cursor c = getReadableDatabase().query(TABLE_RESTO, new String[]{COLUMN_ID, COLUMN_ZOMATO_ID, COLUMN_HEROKU_ID, COLUMN_RESTO_NAME, COLUMN_PRICE_RANGE, COLUMN_PHONE}, COLUMN_RESTO_NAME + "=?", new String[]{ name }, null, null, null);
        List<RestoItem> restos = new ArrayList<>();
        RestoItem temp;
        while (c.moveToNext()) {
            temp = new RestoItem();
            temp.setId(c.getLong(c.getColumnIndex(COLUMN_ID)));
            temp.setZomatoId(c.getLong(c.getColumnIndex(COLUMN_ZOMATO_ID)));
            temp.setHerokuId(c.getLong(c.getColumnIndex(COLUMN_HEROKU_ID)));
            temp.setName(c.getString(c.getColumnIndex(COLUMN_RESTO_NAME)));
            temp.setPriceRange(c.getString(c.getColumnIndex(COLUMN_PRICE_RANGE)));
            temp.setPhone(c.getLong(c.getColumnIndex(COLUMN_PHONE)));
            getAddressForResto(temp);
            getRatingForResto(temp);

            restos.add(temp);
        }
        c.close();
        Log.i(TAG, "getAllRestaurantsSmall(), Size: " + restos.size());

        return restos;
    }

    /**
     * Retrieves a single restaurant object from the local database with all its related information
     * which contains: One or Many addresses, zero or many reviews, a genre and a submitter.
     *
     * @param id primary key of the restaurant to retrieve.
     * @return Full Resto bean which contains all the data about this restaurant in the database.
     */
    public Resto getSingleRestaurant(long id) {
        if (id < 1)
            return null;
        Resto r = new Resto();

        Cursor c = getReadableDatabase().query(TABLE_RESTO, null, COLUMN_ID + "=?", new String[]{id + ""}, null, null, null);

        if (c.moveToNext()) {
            r.setId(c.getLong(c.getColumnIndex(COLUMN_ID)));
            r.setZomatoId(c.getLong(c.getColumnIndex(COLUMN_ZOMATO_ID)));
            r.setHerokuId(c.getLong(c.getColumnIndex(COLUMN_HEROKU_ID)));
            r.setName(c.getString(c.getColumnIndex(COLUMN_RESTO_NAME)));
            r.setPriceRange(c.getString(c.getColumnIndex(COLUMN_PRICE_RANGE)));
            r.setEmail(c.getString(c.getColumnIndex(COLUMN_EMAIL)));
            r.setLink(c.getString(c.getColumnIndex(COLUMN_LINK)));
            r.setPhone(c.getLong(c.getColumnIndex(COLUMN_PHONE)));

            getGenre(r, c.getLong(c.getColumnIndex(COLUMN_GENRE_FK)));
            getAddressList(r);
            getReviewList(r);
            getUser(r, c.getLong(c.getColumnIndex(COLUMN_USER_FK)));
        }
        c.close();
        Log.i(TAG, "getSingleRestaurant()");
        return r;
    }

    /**
     * Deletes a single email from the TABLE_RESTO_COLUMN. ALl subsequent foreign key constraints
     * will delete on cascade.
     * <p>
     * ISSUE: When adding a restaurant row into the database, if the submitter(user) does not exist he
     * will be added to the database. However when a restaurant is deleted the user is NOT affected.
     * This can cause a problem if the phone will continually store lots of restaurants and deleting
     * them.
     *
     * @param id primary key of the restaurant to delete.
     * @return number of rows affected by the deletion. Should be one, cascaded deletions do not count
     * towards this return.
     */
    public int deleteRestaurant(long id) {
        return getWritableDatabase().delete(TABLE_RESTO, COLUMN_ID + "=?", new String[]{id + ""});
    }

    /**
     * Updates the information of the restaurant. Only the primary key which is given by the id in the
     * resto bean and the submitter will remain unchanged. All other data WILL be overriden.
     *
     * @param resto updated version of the resto. Must contain a primary key.
     */
    public void updateRestaurant(Resto resto) {
        if (resto.getId() < 1)
            throw new IllegalArgumentException("No valid id given");
        ContentValues cv = new ContentValues();
        long genreId = getGenreID(resto.getGenre());

        cv.put(COLUMN_RESTO_NAME, resto.getName());
        cv.put(COLUMN_EMAIL, resto.getEmail());
        cv.put(COLUMN_PHONE, resto.getPhone());
        cv.put(COLUMN_LINK, resto.getLink());
        cv.put(COLUMN_PRICE_RANGE, resto.getPriceRange());
        cv.put(COLUMN_GENRE_FK, genreId);

        getWritableDatabase().update(TABLE_RESTO, cv, COLUMN_ID + "=?", new String[]{resto.getId() + ""});

        updateAddress(resto.getAddress(), resto.getId());
    }

    /**
     * Updates the address for a given restaurant. This method WILL override any previous existing data!
     *
     *
     * @param address address to be changed.
     * @param restoId primary key of restaurant to which change the address.
     */
    public void updateAddress(Address address, Long restoId) {
        if (restoId < 1)
            throw new IllegalArgumentException("No valid id given");

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_STREET_ADDRESS, address.getAddress());
        cv.put(COLUMN_COUNTRY, address.getCountry());
        cv.put(COLUMN_CITY, address.getCity());
        cv.put(COLUMN_POSTAL, address.getPostal());
        cv.put(COLUMN_LONG, address.getLongitude());
        cv.put(COLUMN_LAT, address.getLatitude());
        cv.put(COLUMN_SUITE, address.getSuite());
        cv.put(COLUMN_PROVINCE, address.getProvince());

        Log.d(TAG,"cv contains: " + cv.toString());

        getWritableDatabase().update(TABLE_ADDRESS, cv, COLUMN_RESTO_FK + "=?", new String[]{String.valueOf(restoId)});
    }

    /**
     * Adds a review to the restaurant under the given id. Does not check if such review already exists.
     *
     * @param review Review to add to the restaurant
     * @param restoId primary key of the restaurant.
     * @return primary key of the newly created review.
     */
    public long addReview(Review review, long restoId){
        if(restoId < 1)
            throw new IllegalArgumentException("Invalid id given");
        if(review == null)
            throw new IllegalArgumentException("No review to add");

        return insertReview(review, restoId);
    }

    /*
      Private Methods.
    ------------------------------------------------------------------------------------------------
     */

    /**
     * Fecthes the genre of a restaurant using the provided genre foreign key.
     *
     * @param resto Bean containing the id and to which add the genre.
     */
    private void getGenre(Resto resto, long genreId) {

        Cursor c = getReadableDatabase().query(TABLE_GENRE, new String[]{COLUMN_GENRE}, COLUMN_ID + "=?", new String[]{genreId + ""}, null, null, null);

        if (c.moveToNext())
            resto.setGenre(c.getString(c.getColumnIndex(COLUMN_GENRE)));

        c.close();
    }

    /**
     * Using the resto id, fetches all the addresses matching to that restaurant in the database and
     * puts them into a list which is then added to the bean.
     *
     * @param resto bean to which add the address.
     */
    private void getAddressList(Resto resto) {
        long localId = resto.getId();

        Cursor c = getReadableDatabase().query(TABLE_ADDRESS, null, COLUMN_RESTO_FK + "=?", new String[]{localId + ""}, null, null, null);
        if (c.moveToNext()) {
            Address address = new Address();
            address.setCity(c.getString(c.getColumnIndex(COLUMN_CITY)));
            address.setAddress(c.getString(c.getColumnIndex(COLUMN_STREET_ADDRESS)));
            address.setCountry(c.getString(c.getColumnIndex(COLUMN_COUNTRY)));
            address.setLatitude(c.getDouble(c.getColumnIndex(COLUMN_LAT)));
            address.setLongitude(c.getDouble(c.getColumnIndex(COLUMN_LONG)));
            address.setPostal(c.getString(c.getColumnIndex(COLUMN_POSTAL)));
            address.setProvince(c.getString(c.getColumnIndex(COLUMN_PROVINCE)));
            address.setSuite(c.getString(c.getColumnIndex(COLUMN_SUITE)));

            resto.setAddress(address);
        }
        c.close();
    }

    /**
     * Using the resto id, fetches all the reviews for a given restaurant from the database and puts
     * them in a list which is then added to the resto bean.
     *
     * @param resto bean to which set the reviews
     */
    private void getReviewList(Resto resto) {
        long id = resto.getZomatoId();

        Cursor c = getReadableDatabase().query(TABLE_REVIEW, null, COLUMN_RESTO_FK + "=?", new String[]{id + ""}, null, null, null);
        List<Review> reviews = new ArrayList<>();
        while (c.moveToNext()) {
            Review rev = new Review();
            rev.setTitle(c.getString(c.getColumnIndex(COLUMN_TITLE)));
            rev.setContent(c.getString(c.getColumnIndex(COLUMN_CONTENT)));
            rev.setLikes(c.getInt(c.getColumnIndex(COLUMN_LIKES)));
            rev.setRating(c.getDouble(c.getColumnIndex(COLUMN_RATING)));
            rev.setRestoId(resto.getId());
            getUser(rev, c.getLong(c.getColumnIndex(COLUMN_USER_FK)));
            reviews.add(rev);
        }

        c.close();
        resto.setReviews(reviews);

    }

    /**
     * Fetches all the ratings in the reviews for a single restaurant in the database. Then computes
     * the average an sets it in to the bean.
     *
     * @param temp bean to which add the average rating of the resto.
     */
    private void getRatingForResto(RestoItem temp) {
        long id = temp.getId();

        Cursor c = getReadableDatabase().query(TABLE_REVIEW, new String[]{COLUMN_RATING}, COLUMN_RESTO_FK + "=?", new String[]{id + ""}, null, null, null);
        int reviews = 0;
        double score = 0;
        // Alternative was to use raw query and call the AVG() on the column.
        while (c.moveToNext()) {
            score += c.getDouble(c.getColumnIndex(COLUMN_RATING));
            reviews++;
        }

        if (reviews == 0)
            temp.setRating(0);
        else {
            temp.setRating(score / reviews);
        }
        c.close();
        Log.i(TAG, "getRatingForResto()");
    }

    /**
     * Fetches the city, longitude and latitude for a given restaurant.
     *
     * @param temp resto item for which look up the long and lat
     */
    private void getAddressForResto(RestoItem temp) {
        long id = temp.getId();

        Cursor c = getReadableDatabase().query(TABLE_ADDRESS, new String[]{COLUMN_CITY, COLUMN_LONG, COLUMN_LAT}, COLUMN_ID + "=?", new String[]{id + ""}, null, null, null, "1");

        if (c.moveToNext()) {
            temp.setCity(c.getString(c.getColumnIndex(COLUMN_CITY)));
            temp.setLongitude(c.getDouble(c.getColumnIndex(COLUMN_LONG)));
            temp.setLatitude(c.getDouble(c.getColumnIndex(COLUMN_LAT)));
        }
        c.close();
        Log.i(TAG, "getAddressForResto()");
    }


    /**
     * Adds all the reviews for this restaurant to the database.
     *
     * @param reviews List of reviews that this restaurant has.
     * @param restoId primary key for the restaurant to reference.
     */
    private void insertReviews(List<Review> reviews, long restoId) {
        if (reviews != null) {
            long userId;
            for (Review r : reviews) {
                insertReview(r, restoId);
            }
        }
        Log.i(TAG, "insertReviews()");
    }

    private long insertReview(Review review, long restoId){
        ContentValues cv;
        cv = new ContentValues();

        cv.put(COLUMN_TITLE, review.getTitle());
        cv.put(COLUMN_CONTENT, review.getContent());
        cv.put(COLUMN_RATING, review.getRating());
        cv.put(COLUMN_LIKES, review.getLikes());
        long userId = getUserId(review.getSubmitterEmail(), review.getSubmitter());
        cv.put(COLUMN_USER_FK, userId);
        cv.put(COLUMN_RESTO_FK, restoId);

        return getWritableDatabase().insert(TABLE_REVIEW, null, cv);
    }

    /**
     * Adds all the addresses in the bean to the the database.
     *
     * @param address List of addresses fir the given restaurant.
     * @param restoId primary kry of a restaurant to reference.
     */
    private void insertAddress(Address address, long restoId) {
        if (address != null) {
            ContentValues cv;
            cv = new ContentValues();

            cv.put(COLUMN_STREET_ADDRESS, address.getAddress());
            cv.put(COLUMN_COUNTRY, address.getCountry());
            cv.put(COLUMN_CITY, address.getCity());
            cv.put(COLUMN_POSTAL, address.getPostal());
            cv.put(COLUMN_LONG, address.getLongitude());
            cv.put(COLUMN_LAT, address.getLatitude());
            cv.put(COLUMN_SUITE, address.getSuite());
            cv.put(COLUMN_RESTO_FK, restoId);
            cv.put(COLUMN_PROVINCE, address.getProvince());

            getWritableDatabase().insert(TABLE_ADDRESS, null, cv);
        }

        Log.i(TAG, "insertAddress()");
    }

    /**
     * Adds the restaurant information to the rest table in the local database.
     *
     * @param resto bean which holds information about the restaurant.
     * @return newly created id for the restaurant row.
     */
    private long insertResto(Resto resto) throws IllegalArgumentException {

        //validateBean(resto);

        long genreId = getGenreID(resto.getGenre());
        long userId = getUserId(resto.getSubmitterEmail(), resto.getSubmitterName());

        ContentValues cv = new ContentValues();

        Log.d(TAG, resto.getName() + " is the name");

        cv.put(COLUMN_ZOMATO_ID, resto.getZomatoId());
        cv.put(COLUMN_HEROKU_ID, resto.getHerokuId());
        cv.put(COLUMN_RESTO_NAME, resto.getName());
        cv.put(COLUMN_EMAIL, resto.getEmail());
        cv.put(COLUMN_PHONE, resto.getPhone());
        cv.put(COLUMN_LINK, resto.getLink());
        cv.put(COLUMN_PRICE_RANGE, resto.getPriceRange());
        cv.put(COLUMN_USER_FK, userId);
        cv.put(COLUMN_GENRE_FK, genreId);

        long id = getWritableDatabase().insert(TABLE_RESTO, null, cv);
        Log.i(TAG, "insertResto()");
        return id;
    }

    /**
     * Checks if all the needed fields are initialized and if they are not empty.
     *
     * @throws IllegalArgumentException if any of the required fields is not initialized.
     */
    @Deprecated //For now
    private void validateBean(Resto r) {

        if (r.getName() == null || r.getName().length() < 1)
            throw new IllegalArgumentException(COLUMN_RESTO_NAME + " cannot be empty");
        if (r.getEmail() == null || r.getEmail().length() < 1)
            throw new IllegalArgumentException(COLUMN_EMAIL + " cannot be empty");
        if (r.getPriceRange() == null || r.getPriceRange().length() < 1)
            throw new IllegalArgumentException(COLUMN_PRICE_RANGE + " cannot be empty");
        if (r.getLink() == null || r.getLink().length() < 1)
            throw new IllegalArgumentException(COLUMN_LINK + " cannot be empty");
        if (r.getSubmitterEmail() == null || r.getSubmitterEmail().length() < 1)
            throw new IllegalArgumentException(COLUMN_RESTO_NAME + " cannot be empty");
    }

    /**
     * Fetches the primary key for a given genre from the database. If it does not exists, inserts the
     * genre into the databsase and then returns the newly created id of the genre.
     *
     * @param genre Genre of the restaurant.
     * @return primary key of that genre row.
     */
    private long getGenreID(String genre) throws IllegalArgumentException {

        if (genre == null)
            throw new IllegalArgumentException("Cannot add empty genre.");

        long id = -1;

        Cursor c = getReadableDatabase().query(TABLE_GENRE, new String[]{COLUMN_ID}, GET_GENRE, new String[]{genre}, null, null, null, null);

        if (c.moveToNext())
            id = c.getInt(c.getColumnIndex(COLUMN_ID));

        if (id < 0) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_GENRE, genre);
            id = getWritableDatabase().insert(TABLE_GENRE, null, cv);
        }
        c.close();
        Log.i(TAG, "getGenreID()");
        return id;
    }

    /**
     * Adds a user to the database or fetches the id of the user with a matching email address.
     *
     * @param submitterEmail email a
     */
    private long getUserId(String submitterEmail, String submitter) {
        long id = -1;

        Cursor c = getReadableDatabase().query(TABLE_USERS, new String[]{COLUMN_ID}, GET_USER, new String[]{submitterEmail}, null, null, null, null);
        if (c.moveToNext())
            id = c.getInt(c.getColumnIndex(COLUMN_ID));

        if (id < 0) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_EMAIL, submitterEmail);
            cv.put(COLUMN_USERNAME, submitter);
            id = getWritableDatabase().insert(TABLE_USERS, null, cv);
        }
        c.close();
        Log.i(TAG, "getUserId()");
        return id;
    }

    /**
     * Fetches a user from the database using the supplied foreign key, then adds a reference of oit
     * to the given resto bean
     *
     * @param resto  Bean to which add the user.
     * @param userId Primary key of the user.
     */
    private void getUser(Resto resto, long userId) {
        Cursor c = getReadableDatabase().query(TABLE_USERS, null, COLUMN_ID + "=?", new String[]{userId + ""}, null, null, null);

        if (c.moveToNext()) {
            resto.setSubmitterEmail(c.getString(c.getColumnIndex(COLUMN_EMAIL)));
            resto.setSubmitterName(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
        }
        c.close();
    }

    /**
     * Fetches a user from the database using the supplied foreign key, then adds a reference of oit
     * to the given review bean
     *
     * @param review Bean to which add the user.
     * @param userId Primary key of the user.
     */
    private void getUser(Review review, long userId) {
        Cursor c = getReadableDatabase().query(TABLE_USERS, null, COLUMN_ID + "=?", new String[]{userId + ""}, null, null, null);

        if (c.moveToNext()) {
            review.setSubmitterEmail(c.getString(c.getColumnIndex(COLUMN_EMAIL)));
            review.setSubmitter(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
        }
        c.close();
        //I did overloaded methods because I was too lazy to make a common interface.
    }


    public List<Resto> getAllRestaurants() {
        Cursor c = getReadableDatabase().query(TABLE_RESTO, null, null, null, null, null, null);
        List<Resto> restos = new ArrayList<>();
        Resto temp = new Resto();

        while (c.moveToNext()) {
            temp.setId(c.getLong(c.getColumnIndex(COLUMN_ID)));
            temp.setZomatoId(c.getLong(c.getColumnIndex(COLUMN_ZOMATO_ID)));
            temp.setHerokuId(c.getLong(c.getColumnIndex(COLUMN_HEROKU_ID)));
            temp.setName(c.getString(c.getColumnIndex(COLUMN_RESTO_NAME)));
            temp.setPriceRange(c.getString(c.getColumnIndex(COLUMN_PRICE_RANGE)));
            temp.setEmail(c.getString(c.getColumnIndex(COLUMN_EMAIL)));
            temp.setLink(c.getString(c.getColumnIndex(COLUMN_LINK)));
            temp.setPhone(c.getLong(c.getColumnIndex(COLUMN_PHONE)));

            getGenre(temp, c.getLong(c.getColumnIndex(COLUMN_GENRE_FK)));
            getAddressList(temp);
            getReviewList(temp);
            getUser(temp, c.getLong(c.getColumnIndex(COLUMN_USER_FK)));

            restos.add(temp);
        }
        c.close();
        Log.i(TAG, "getAllRestaurant()");
        return restos;
    }
}
