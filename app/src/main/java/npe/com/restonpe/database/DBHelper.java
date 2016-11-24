package npe.com.restonpe.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Handles creation of the connection, creation of the tables and upgrading of the database.
 * Does not provide any data returning methods.
 *
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 11/23/2016
 */
public class DBHelper extends SQLiteOpenHelper{

    // Tag
    private static String TAG = "DBHelper";

    // Instance to share the database.
    private static DBHelper dbh;

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
            COLUMN_LINK+" text, "+
            "FOREIGN KEY("+COLUMN_USER_FK+") REFERENCES "+TABLE_USERS+"("+COLUMN_ID+") ON DELETE CASCADE);";

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
            "FOREIGN KEY ("+COLUMN_USER_FK+") REFERENCES "+TABLE_USERS+"("+COLUMN_ID+") ON DELETE CASCADE"+
            "FOREIGN KEY ("+COLUMN_RESTO_FK+") REFERENCES "+TABLE_RESTO+"("+COLUMN_ID+") ON DELETE CASCADE);";

    // Drop tables
    private static final String DROP_REVIEW = "DROP TABLE IF EXISTS "+TABLE_REVIEW+" ;";
    private static final String DROP_ADDRESS = "DROP TABLE IF EXISTS "+TABLE_ADDRESS+" ;";
    private static final String DROP_RESTO = "DROP TABLE IF EXISTS "+TABLE_RESTO+" ;";
    private static final String DROP_USER = "DROP TABLE IF EXISTS "+TABLE_USERS+" ;";
    private static final String DROP_GENRE = "DROP TABLE IF EXISTS "+TABLE_GENRE+" ;";



    /**
     * Constructor for the object. Private just to let the factory method to initialize the
     * object.
     *
     * @param context
     */
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Factory method to create a DBHelper object. Only creates the database if there is
     * only one reference to it.
     *
     * @param context
     * @return DBHelper
     */
    public static DBHelper getDatabase(Context context) {
		/*
		 * Use the application context, which will ensure that you don't
		 * accidentally leak an Activity's context. See this article for more
		 * information: http://bit.ly/6LRzfx
		 */
        if (dbh == null) {
            dbh = new DBHelper(context.getApplicationContext());
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


}
