package npe.com.restonpe.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Provides read and write to the database implementation fot this application.
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
    private static final String TABLE_USERS = "users";

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
            COLUMN_EMAIL+" text not null);";

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
            "FOREIGN KEY("+COLUMN_USER_FK+") REFERENCES "+TABLE_USERS+"("+COLUMN_ID+"));";

    private static final String CREATE_ADDRESS = "create table "+TABLE_GENRE+"( "+
            COLUMN_ID+" integer primary key autoincrement, "+
            COLUMN_CIVIC+" integer not null, "+
            COLUMN_STREET+" text not null, "+
            COLUMN_COUNTRY+" text not null, "+
            COLUMN_POSTAL+" text not null, ";
    //TODO finish tables.




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
     * @return DBHelper
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
