package com.physphil.android.restaurantroulette.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.physphil.android.restaurantroulette.models.Restaurant;
import com.physphil.android.restaurantroulette.models.RestaurantHistory;

import java.util.ArrayList;
import java.util.Date;
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
    public static final String COLUMN_RESTAURANT_PRICE_LEVEL = "priceLevel";
    public static final String COLUMN_RESTAURANT_NOTES = "notes";
    public static final String COLUMN_RESTAURANT_ADDRESS = "address";
    public static final String COLUMN_RESTAURANT_PHONE = "phone";

    public static final String[] COLUMNS_RESTAURANT_TABLE = {COLUMN_RESTAURANT_ID, COLUMN_RESTAURANT_NAME, COLUMN_RESTAURANT_GENRE, COLUMN_RESTAURANT_USER_RATING, COLUMN_RESTAURANT_PRICE_LEVEL, COLUMN_RESTAURANT_NOTES};

    // History Table columns
    public static final String COLUMN_HISTORY_ID = "id";
    public static final String COLUMN_HISTORY_RESTAURANT_ID = "restaurantId";
    public static final String COLUMN_HISTORY_DATE = "date";

    public static final String[] COLUMNS_HISTORY_TABLE = {COLUMN_HISTORY_ID, COLUMN_HISTORY_RESTAURANT_ID, COLUMN_HISTORY_DATE};

    // Database creation SQL statements
    private static final String CREATE_TABLE_RESTAURANTS =
            "CREATE table " + TABLE_RESTAURANTS + " (" + COLUMN_RESTAURANT_ID + " TEXT PRIMARY KEY, " +
                COLUMN_RESTAURANT_NAME + " TEXT, " +
                COLUMN_RESTAURANT_GENRE + " TEXT, " +
                COLUMN_RESTAURANT_USER_RATING + " INTEGER, " +
                COLUMN_RESTAURANT_PRICE_LEVEL + " INTEGER, " +
                COLUMN_RESTAURANT_NOTES + " TEXT, " +
                COLUMN_RESTAURANT_ADDRESS + " TEXT, " +
                COLUMN_RESTAURANT_PHONE + " TEXT);";

    private static final String CREATE_TABLE_HISTORY =
            "CREATE table " + TABLE_HISTORY + " (" + COLUMN_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HISTORY_RESTAURANT_ID + " TEXT, " +
                COLUMN_HISTORY_DATE + " TEXT);";


    public static DatabaseHelper getInstance(Context context){

        if(mInstance == null){

            mInstance = new DatabaseHelper(context.getApplicationContext());
        }

        return mInstance;
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

        addRestaurant(db, new Restaurant("Burger King", Restaurant.GENRE_FAST_FOOD, 3, 1));
        addRestaurant(db, new Restaurant("McDonald's", Restaurant.GENRE_FAST_FOOD, 3, 1));
        addRestaurant(db, new Restaurant("Wendy's", Restaurant.GENRE_FAST_FOOD, 3, 1));
        addRestaurant(db, new Restaurant("Pizza Hut", Restaurant.GENRE_PIZZA, 3, 2));
        addRestaurant(db, new Restaurant("Olive Garden", Restaurant.GENRE_ITALIAN, 3, 1));
        addRestaurant(db, new Restaurant("East Side Mario's", Restaurant.GENRE_ITALIAN, 3, 2));
        addRestaurant(db, new Restaurant("Cheesecake Factory", Restaurant.GENRE_NORTH_AMERICAN, 3, 2));
        addRestaurant(db, new Restaurant("Red Lobster", Restaurant.GENRE_SEAFOOD, 3, 2));
    }

    /**
     * Update restaurant in database. Restaurant will be added if it doesn't exist, or updated if already present.
     * @param db database
     * @param restaurant Restaurant to add
     */
    public void addRestaurant(SQLiteDatabase db, Restaurant restaurant){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RESTAURANT_ID, restaurant.getId());
        cv.put(COLUMN_RESTAURANT_NAME, restaurant.getName());
        cv.put(COLUMN_RESTAURANT_GENRE, restaurant.getGenre());
        cv.put(COLUMN_RESTAURANT_USER_RATING, restaurant.getUserRating());
        cv.put(COLUMN_RESTAURANT_PRICE_LEVEL, restaurant.getPriceLevel());
        cv.put(COLUMN_RESTAURANT_NOTES, restaurant.getNotes());

        db.replace(TABLE_RESTAURANTS, null, cv);
    }

    /**
     * Update restaurant in database. Restaurant will be added if it doesn't exist, or updated if already present.
     * @param restaurant Restaurant to add
     */
    public void addRestaurant(Restaurant restaurant){

        addRestaurant(mDb, restaurant);
    }

    public Restaurant getRestaurantById(String id){

        Restaurant r = new Restaurant();
        String selection = COLUMN_RESTAURANT_ID + " = '" + id + "'";

        Cursor c = mDb.query(TABLE_RESTAURANTS, COLUMNS_RESTAURANT_TABLE, selection, null, null, null, null);

        if(c.moveToFirst()){

            r.setId(c.getString(c.getColumnIndex(COLUMN_RESTAURANT_ID)));
            r.setName(c.getString(c.getColumnIndex(COLUMN_RESTAURANT_NAME)));
            r.setGenre(c.getString(c.getColumnIndex(COLUMN_RESTAURANT_GENRE)));
            r.setUserRating(c.getInt(c.getColumnIndex(COLUMN_RESTAURANT_USER_RATING)));
            r.setPriceLevel(c.getInt(c.getColumnIndex(COLUMN_RESTAURANT_PRICE_LEVEL)));
            r.setNotes(c.getString(c.getColumnIndex(COLUMN_RESTAURANT_NOTES)));
        }

        c.close();
        return r;
    }

    /**
     * Get list of all Restaurants stored in database with specified genre, sorted in alphabetical order
     * @param genre genre to search for
     * @return list of restaurants
     */
    public List<Restaurant> getRestaurantsByGenre(String genre){

        String filter = COLUMN_RESTAURANT_GENRE + " = '" + genre + "'";
        return getRestaurants(filter);
    }

    /**
     * Get list of all Restaurants stored in database, sorted in alphabetical order
     * @return list of all restaurants
     */
    public List<Restaurant> getAllRestaurants(){
        return getRestaurants(null);
    }

    /**
     * Get restaurants from database
     * @param filter selection for query, formatted as SQL string (minus the WHERE clause). Passing in null returns all restaurants with no filter.
     * @return list of restaurants
     */
    private List<Restaurant> getRestaurants(String filter){

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        String[] columns = new String[] {COLUMN_RESTAURANT_ID, COLUMN_RESTAURANT_NAME, COLUMN_RESTAURANT_GENRE, COLUMN_RESTAURANT_USER_RATING, COLUMN_RESTAURANT_PRICE_LEVEL};

        Cursor c = mDb.query(TABLE_RESTAURANTS, columns, filter, null, null, null, COLUMN_RESTAURANT_NAME);

        if(c.moveToFirst()){

            while(!c.isAfterLast()){

                Restaurant r = new Restaurant();
                r.setId(c.getString(c.getColumnIndex(COLUMN_RESTAURANT_ID)));
                r.setName(c.getString(c.getColumnIndex(COLUMN_RESTAURANT_NAME)));
                r.setGenre(c.getString(c.getColumnIndex(COLUMN_RESTAURANT_GENRE)));
                r.setUserRating(c.getInt(c.getColumnIndex(COLUMN_RESTAURANT_USER_RATING)));
                r.setPriceLevel(c.getInt(c.getColumnIndex(COLUMN_RESTAURANT_PRICE_LEVEL)));

                restaurants.add(r);

                c.moveToNext();
            }
        }
        c.close();

        return restaurants;
    }

    /**
     * Delete restaurant from database, including all its history
     * @param id id of restaurant to delete
     */
    public void deleteRestaurantById(String id){

        String selection = COLUMN_RESTAURANT_ID + " = '" + id + "'";
        mDb.delete(TABLE_RESTAURANTS, selection, null);

        selection = COLUMN_HISTORY_RESTAURANT_ID + " = '" + id + "'";
        mDb.delete(TABLE_HISTORY, selection, null);
    }

    /**
     * Delete all restaurants and history stored in database
     */
    public void deleteAllRestaurants(){

        mDb.delete(TABLE_RESTAURANTS, null, null);
        deleteRestaurantHistory();
    }

    /**
     * Add restaurant selection to history
     * @param id
     */
    public void addRestaurantHistory(String id){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_HISTORY_RESTAURANT_ID, id);
        cv.put(COLUMN_HISTORY_DATE, new Date().getTime());

        mDb.insert(TABLE_HISTORY, null, cv);
    }

    /**
     * Get selection history for an individual restaurant, sorted by date of selection in descending order
     * @param id restaurant id
     * @return list of RestaurantHistory objects
     */
    public List<RestaurantHistory> getHistoryByRestaurant(String id){

        List<RestaurantHistory> history = new ArrayList<RestaurantHistory>();
        String selection = COLUMN_HISTORY_RESTAURANT_ID + " = '" + id + "'";
        String order = COLUMN_HISTORY_DATE + " DESC";

        Cursor c = mDb.query(TABLE_HISTORY, COLUMNS_HISTORY_TABLE, selection, null, null, null, order);

        if(c.moveToFirst()){

            while(!c.isAfterLast()){

                history.add(new RestaurantHistory(
                        c.getInt(c.getColumnIndex(COLUMN_HISTORY_ID)),
                        c.getString(c.getColumnIndex(COLUMN_HISTORY_RESTAURANT_ID)),
                        c.getString(c.getColumnIndex(COLUMN_HISTORY_DATE))));

                c.moveToNext();
            }
        }

        c.close();
        return history;
    }

    public void deleteRestaurantHistory(){

        mDb.delete(TABLE_HISTORY, null, null);
    }

    public List<RestaurantHistory> getAllHistory(){

        List<RestaurantHistory> historyList = new ArrayList<RestaurantHistory>();
        String query =
                "SELECT " +
                    "h." + COLUMN_HISTORY_ID + ", " +
                    "h." + COLUMN_HISTORY_DATE + ", " +
                    "h." + COLUMN_HISTORY_RESTAURANT_ID + ", " +
                    "r." + COLUMN_RESTAURANT_NAME + " " +
                "FROM " +
                    TABLE_HISTORY + " as h " +
                "INNER JOIN " +
                    TABLE_RESTAURANTS + " as r " +
                "ON " +
                    "h." + COLUMN_HISTORY_RESTAURANT_ID + " = r." + COLUMN_RESTAURANT_ID + " " +
                "ORDER BY " +
                    COLUMN_HISTORY_DATE + " DESC";

        Cursor c = mDb.rawQuery(query, null);

        if(c.moveToFirst()){

            while(!c.isAfterLast()){

                RestaurantHistory history = new RestaurantHistory(
                        c.getInt(c.getColumnIndex(COLUMN_HISTORY_ID)),
                        c.getString(c.getColumnIndex(COLUMN_HISTORY_RESTAURANT_ID)),
                        c.getString(c.getColumnIndex(COLUMN_HISTORY_DATE)));
                history.setName(c.getString(c.getColumnIndex(COLUMN_RESTAURANT_NAME)));

                historyList.add(history);
                c.moveToNext();
            }
        }
        c.close();

        return historyList;
    }

}
