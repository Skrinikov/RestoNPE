package npe.com.restonpe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import npe.com.restonpe.Beans.Address;
import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.Review;

/**
 * Handles the creation and maintenance of the database.
 * Provides data querying methods for the resto app context..
 *
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 11/23/2016
 */
public class RestoDAO extends SQLiteOpenHelper{

    // Tag
    private static String TAG = "RestoDAO";

    // Instance to share the database.
    private static RestoDAO dbh;

    // Database related information.
    private static final String DATABASE_NAME = "resto.db";
    private static final int  DATABASE_VERSION = 1;

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
    private static final String COLUMN_EMAIL= "email";
    private static final String COLUMN_USER_FK = "user_id";
    private static final String COLUMN_RESTO_FK = "resto_id";
    private static final String COLUMN_POSTAL = "postal";

    // Genre table
    private static final String COLUMN_GENRE = "genre_name";

    // Resto table
    private static final String COLUMN_RESTO_NAME = "resto_name";
    private static final String COLUMN_PRICE_RANGE = "resto_price_range";
    private static final String COLUMN_LINK = "link";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_GENRE_FK = "genre_id";

    // Address table
    private static final String COLUMN_CIVIC = "civic";
    private static final String COLUMN_STREET = "street";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LONG = "long";
    private static final String COLUMN_SUITE = "suite";

    // User Table
    private static final String COLUMN_USERNAME= "username";

    // Review Table
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_lIKES = "likes";

    // Create Tables Strings
    private static final String CREATE_USERS = "create table "+TABLE_USERS+"( " +
            COLUMN_ID+" integer primary key autoincrement, "+
            COLUMN_USERNAME+" text not null, "+
            COLUMN_EMAIL+" text not null unique);";

    private static final String CREATE_GENRE = "create table "+TABLE_GENRE+"( "+
            COLUMN_ID+" integer primary key autoincrement, "+
            COLUMN_GENRE+" text not null);";

    private static final String CREATE_RESTO = "create table "+TABLE_RESTO+"( "+
            COLUMN_ID+" integer primary key autoincrement, "+
            COLUMN_RESTO_NAME+" text not null, "+
            COLUMN_PRICE_RANGE+" text, "+
            COLUMN_PHONE+" integer, "+
            COLUMN_EMAIL+" text, "+
            COLUMN_CREATED+" datetime default current_timestamp, "+
            COLUMN_MODIFIED+" datetime default current_timestamp, "+
            COLUMN_USER_FK+" integer, "+
            COLUMN_GENRE_FK+" integer, "+
            COLUMN_LINK+" text, "+
            "FOREIGN KEY("+COLUMN_USER_FK+") REFERENCES "+TABLE_USERS+"("+COLUMN_ID+") ON DELETE CASCADE);"+
            "FOREIGN KEY("+COLUMN_GENRE_FK+") REFERENCES "+TABLE_RESTO+"("+COLUMN_ID+") ON DELETE CASCADE);";

    private static final String CREATE_ADDRESS = "create table "+TABLE_ADDRESS+"( "+
            COLUMN_ID+" integer primary key autoincrement, "+
            COLUMN_CIVIC+" integer not null, "+
            COLUMN_STREET+" text not null, "+
            COLUMN_COUNTRY+" text not null, "+
            COLUMN_CITY+" text not null, "+
            COLUMN_LONG+" real not null, "+
            COLUMN_LAT+" real not null, "+
            COLUMN_POSTAL+" text not null, "+
            COLUMN_SUITE+" integer not null, "+
            "FOREIGN KEY ("+COLUMN_RESTO_FK+") REFERENCES "+TABLE_RESTO+"("+COLUMN_ID+") ON DELETE CASCADE);";

    private static final String CREATE_REVIEW = "create table "+TABLE_REVIEW+"( "+
            COLUMN_ID+" integer primary key autoincrement, "+
            COLUMN_TITLE+" text, "+
            COLUMN_CONTENT+" text not null, "+
            COLUMN_RATING+" real not null, "+
            COLUMN_lIKES+" integer not null, "+
            COLUMN_USER_FK+" integer, "+
            COLUMN_RESTO_FK+" integer, "+
            "FOREIGN KEY ("+COLUMN_USER_FK+") REFERENCES "+TABLE_USERS+"("+COLUMN_ID+") ON DELETE CASCADE"+
            "FOREIGN KEY ("+COLUMN_RESTO_FK+") REFERENCES "+TABLE_RESTO+"("+COLUMN_ID+") ON DELETE CASCADE);";

    // Drop tables
    private static final String DROP_REVIEW = "DROP TABLE IF EXISTS "+TABLE_REVIEW+" ;";
    private static final String DROP_ADDRESS = "DROP TABLE IF EXISTS "+TABLE_ADDRESS+" ;";
    private static final String DROP_RESTO = "DROP TABLE IF EXISTS "+TABLE_RESTO+" ;";
    private static final String DROP_USER = "DROP TABLE IF EXISTS "+TABLE_USERS+" ;";
    private static final String DROP_GENRE = "DROP TABLE IF EXISTS "+TABLE_GENRE+" ;";

    // Query Strings
    private static final String GET_GENRE = COLUMN_GENRE+"=?";
    private static final String GET_USER = COLUMN_EMAIL+"=?";



    /**
     * Constructor for the object. Private just to let the factory method to initialize the
     * object.
     *
     * @param context
     */
    private RestoDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Factory method to create a RestoDAO object. Only creates the database if there is
     * only one reference to it.
     *
     * @param context
     * @return RestoDAO
     */
    public static RestoDAO getDatabase(Context context) {
		/*
		 * Use the application context, which will ensure that you don't
		 * accidentally leak an Activity's context. See this article for more
		 * information: http://bit.ly/6LRzfx
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
     * @param database
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
     * @param database link to the device internal sqlite database.
     * @param oldVersion integer which represents the old version.
     * @param newVersion integer which represents the new version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version "+oldVersion+" to version "+newVersion+". All data" +
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
     * @param resto
     */
    public void addRestaurant(Resto resto) throws IllegalArgumentException{
        long restoId = insertResto(resto);
        insertAddress(resto.getAddress(),restoId);
        insertReviews(resto.getReviews(),restoId);
    }


    /**
     * Adds all the reviews for this restaurant to the database.
     *
     * @param reviews List of reviews that this restaurant has.
     * @param restoId primary key for the restaurant to reference.
     */
    private void insertReviews(ArrayList<Review> reviews, long restoId) {
        if(reviews != null){
            ContentValues cv;
            for(Review r : reviews){
                cv = new ContentValues();

                //cv.put(COLUMN_TITLE);

            }
        }
    }

    /*
            COLUMN_TITLE+" text, "+
            COLUMN_CONTENT+" text not null, "+
            COLUMN_RATING+" real not null, "+
            COLUMN_lIKES+" integer not null, "+
            COLUMN_USER_FK+" integer, "+
            COLUMN_RESTO_FK+" integer, "+
    */

    /**
     * Adds all the addresses in the bean to the the database.
     *
     * @param address List of addresses fir the given restaurant.
     * @param restoId primary kry of a restaurant to reference.
     */
    private void insertAddress(ArrayList<Address> address, long restoId) {
        if(address != null){
            ContentValues cv;
            for(Address addr : address){
                cv = new ContentValues();

                cv.put(COLUMN_CIVIC, addr.getCivic());
                cv.put(COLUMN_STREET, addr.getStreet());
                cv.put(COLUMN_COUNTRY, addr.getCountry());
                cv.put(COLUMN_CITY, addr.getCity());
                cv.put(COLUMN_POSTAL, addr.getPostal());
                cv.put(COLUMN_LONG, addr.getLongitude());
                cv.put(COLUMN_LAT, addr.getLatitude());
                cv.put(COLUMN_SUITE, addr.getSuite());
                cv.put(COLUMN_RESTO_FK, restoId);

                getWritableDatabase().insert(TABLE_ADDRESS, null, cv);
            }
        }
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
        long userId = getUserId(resto.getGenre());

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_RESTO_NAME,resto.getName());
        cv.put(COLUMN_EMAIL,resto.getEmail());
        cv.put(COLUMN_PHONE,resto.getPhone());
        cv.put(COLUMN_LINK,resto.getLink());
        cv.put(COLUMN_PRICE_RANGE,resto.getPriceRange());
        cv.put(COLUMN_USER_FK,userId);
        cv.put(COLUMN_GENRE_FK,genreId);

        long id = getWritableDatabase().insert(TABLE_RESTO, null, cv);
        return id;
    }

    /**
     * Checks if all the needed fields are initialized and if they are not empty.
     * @throws IllegalArgumentException if any of the required fields is not initialized.
     */
    private void validateBean(Resto r) {

        if(r.getName() == null || r.getName().length() < 1)
            throw  new IllegalArgumentException(COLUMN_RESTO_NAME+" cannot be empty");
        if(r.getEmail() == null || r.getEmail().length() < 1)
            throw  new IllegalArgumentException(COLUMN_EMAIL+" cannot be empty");
        if(r.getPriceRange() == null || r.getPriceRange().length() < 1)
            throw  new IllegalArgumentException(COLUMN_PRICE_RANGE+" cannot be empty");
        if(r.getLink() == null || r.getLink().length() < 1)
            throw  new IllegalArgumentException(COLUMN_LINK+" cannot be empty");
        if(r.getSubmitterEmail() == null || r.getSubmitterEmail().length() < 1)
            throw  new IllegalArgumentException(COLUMN_RESTO_NAME+" cannot be empty");
    }

    /**
     * Fetches the primary key for a given genre from the database. If it does not exists, inserts the
     * genre into the databsase and then returns the newly created id of the genre.
     *
     * @param genre Genre of the restaurant.
     * @return primary key of that genre row.
     */
    private long getGenreID(String genre) throws  IllegalArgumentException{

        if(genre.length() < 1)
            throw new IllegalArgumentException("Cannot add empty genre.");

        long id = -1;

        Cursor c = getReadableDatabase().query(TABLE_GENRE,new String[]{COLUMN_ID},GET_GENRE,new String[]{genre}, null,null, null, null);
        id = c.getInt(1);

        if(id < 0){
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_GENRE,genre);
            id = getWritableDatabase().insert(TABLE_GENRE,null,cv);
        }

        return id;
    }

    /**
     * Adds a user to the database or fetches the id of the user with a matching email address.
     *
     * @param submitterEmail email a
     */
    private long getUserId(String submitterEmail) {
        long id = -1;

        Cursor c = getReadableDatabase().query(TABLE_USERS,new String[]{COLUMN_ID},GET_USER,new String[]{submitterEmail}, null,null, null, null);
        id = c.getInt(1);

        if(id < 0){
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_GENRE,submitterEmail);
            id = getWritableDatabase().insert(TABLE_USERS,null,cv);
        }

        return id;
    }


}
