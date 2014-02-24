package com.physphil.android.restaurantroulette.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.physphil.android.restaurantroulette.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pshadlyn on 2/24/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RESTAURANT_ROULETTE";
    public static final int DATABASE_VERSION = 1;
    private static DatabaseHelper mInstance;
    private static SQLiteDatabase mDb;

    // Database tables
    public static final String TABLE_RESTAURANTS = "Restaurants";
    public static final String TABLE_HISTORY = "RestaurantHistory";

    // Restaurants Table columns
    public static final String COLUMN_RESTAURANT_ID = "id";
    public static final String COLUMN_RESTAURANT_NAME = "name";
    public static final String COLUMN_RESTAURANT_GENRE = "genre";
    public static final String COLUMN_RESTAURANT_USER_RATING = "userRating";

    // History Table columns
    public static final String COLUMN_HISTORY_ID = "id";
    public static final String COLUMN_HISTORY_RESTAURANT_ID = "restaurantId";
    public static final String COLUMN_HISTORY_DATE = "date";

    // Database creation SQL statements
    private static final String CREATE_TABLE_RESTAURANTS =
            "CREATE table " + TABLE_RESTAURANTS + " (" + COLUMN_RESTAURANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RESTAURANT_NAME + " TEXT, " +
                COLUMN_RESTAURANT_GENRE + " INTEGER, " +
                COLUMN_RESTAURANT_USER_RATING + " INTEGER);";

    private static final String CREATE_TABLE_HISTORY =
            "CREATE table " + TABLE_HISTORY + " (" + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HISTORY_RESTAURANT_ID + " INTEGER, " +
                COLUMN_HISTORY_DATE + " TEXT);";

    // Restaurant genres
    public static final int GENRE_DEFAULT = 0;
    public static final int GENRE_NORTH_AMERICAN = 1;
    public static final int GENRE_BREAKFAST = 2;
    public static final int GENRE_CHINESE = 3;
    public static final int GENRE_ETHNIC = 4;
    public static final int GENRE_FAST_FOOD = 5;
    public static final int GENRE_INDIAN = 6;
    public static final int GENRE_ITALIAN = 7;
    public static final int GENRE_JAPANESE = 8;
    public static final int GENRE_MEXICAN = 9;
    public static final int GENRE_OTHER = 10;
    public static final int GENRE_PIZZA = 11;
    public static final int GENRE_PUB = 12;
    public static final int GENRE_SEAFOOD = 13;
    public static final int GENRE_SUSHI = 14;
    public static final int GENRE_VEGETARIAN = 15;



    public static DatabaseHelper getInstance(Context context){

        if(mInstance == null){

            mInstance = new DatabaseHelper(context.getApplicationContext());
        }

        return mInstance;
    }

    public static SQLiteDatabase getDatabase(){

        return mDb;
    }

    private DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mDb = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_RESTAURANTS);
        db.execSQL(CREATE_TABLE_HISTORY);

        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion){

    }

    /**
     * Insert initial data when database is first created
     */
    private void insertInitialData(SQLiteDatabase db){

        addRestaurant(db, new Restaurant("Burger King", GENRE_FAST_FOOD, 3));
        addRestaurant(db, new Restaurant("McDonald's", GENRE_FAST_FOOD, 3));
        addRestaurant(db, new Restaurant("Wendy's", GENRE_FAST_FOOD, 3));
        addRestaurant(db, new Restaurant("Pizza Hut", GENRE_PIZZA, 3));
        addRestaurant(db, new Restaurant("Olive Garden", GENRE_ITALIAN, 3));
        addRestaurant(db, new Restaurant("East Side Mario's", GENRE_ITALIAN, 3));
        addRestaurant(db, new Restaurant("Cheesecake Factory", GENRE_NORTH_AMERICAN, 3));
        addRestaurant(db, new Restaurant("Red Lobster", GENRE_SEAFOOD, 3));
    }

    /**
     * Add restaurant to database
     * @param db database
     * @param restaurant Restaurant to add
     */
    public void addRestaurant(SQLiteDatabase db, Restaurant restaurant){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RESTAURANT_NAME, restaurant.getName());
        cv.put(COLUMN_RESTAURANT_GENRE, restaurant.getGenre());
        cv.put(COLUMN_RESTAURANT_USER_RATING, restaurant.getUserRating());

        db.insert(TABLE_RESTAURANTS, null, cv);
    }

    /**
     * Add restaurant to database
     * @param restaurant Restaurant to add
     */
    public void addRestaurant(Restaurant restaurant){

        addRestaurant(mDb, restaurant);
    }

    public List<Restaurant> getAllRestaurants(){

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        String[] columns = new String[] {COLUMN_RESTAURANT_ID, COLUMN_RESTAURANT_NAME, COLUMN_RESTAURANT_GENRE, COLUMN_RESTAURANT_USER_RATING};

        Cursor c = mDb.query(TABLE_RESTAURANTS, columns, null, null, null, null, null);

        if(c.moveToFirst()){

            while(!c.isAfterLast()){

                Restaurant r = new Restaurant();
                r.setId(c.getInt(c.getColumnIndex(COLUMN_RESTAURANT_ID)));
                r.setName(c.getString(c.getColumnIndex(COLUMN_RESTAURANT_NAME)));
                r.setGenre(c.getInt(c.getColumnIndex(COLUMN_RESTAURANT_GENRE)));
                r.setUserRating(c.getInt(c.getColumnIndex(COLUMN_RESTAURANT_USER_RATING)));

                restaurants.add(r);

                c.moveToNext();
            }
        }
        c.close();

        return restaurants;
    }
}
