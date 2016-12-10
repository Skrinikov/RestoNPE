package npe.com.restonpe.Zomato;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import npe.com.restonpe.Beans.Address;
import npe.com.restonpe.Beans.Cuisine;
import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.R;

/**
 * Manages calls to the Zomato API.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 30/11/2016
 */
public class ZomatoRestos {

    private static final String TAG = ZomatoRestos.class.getSimpleName();

    private static final String RESTO_ID = "id";
    private static final String RESTO_NAME = "name";
    private static final String RESTO_URL = "url";
    private static final String RESTO_CUISINES = "cuisines";
    private static final String RESTO_PHONE = "phone";
    private static final String RESTO_PRICE = "price";

    private static final String RESTO_LOCATION_ADDRESS = "address";
    private static final String RESTO_LOCATION_CITY = "city";
    private static final String RESTO_LOCATION_PROVINCE = "province";
    private static final String RESTO_LOCATION_LATITUDE = "latitude";
    private static final String RESTO_LOCATION_LONGITUDE = "longitude";
    private static final String RESTO_LOCATION_POSTAL = "postal";
    private static final String RESTO_LOCATION_COUNTRY = "country";

    private static final String RESTO_REVIEW_AVG = "aggregate_rating";

    private Context mContext;

    /**
     * Creates a class that will handle calls to the Zomato API
     *
     * @param context The {@code Context} of the calling {@code Activity}
     */
    public ZomatoRestos(Context context) {
        this.mContext = context;
    }

    /**
     * Gets Cuisines from the Zomato API.
     *
     * @param reader A {@code JsonReader} with the next object being a cuisine array
     * @return A {@code List} of {@code Cuisine}s from the Zomato API.
     * @throws IOException If an IOException occurs with the JSON text
     */
    public List<Cuisine> readCuisines(JsonReader reader) throws IOException {
        List<Cuisine> list = new ArrayList<>();

        // Start reading the text, by beginning the root object.
        reader.beginObject();

        while (reader.hasNext()) {

            // Get next token in the reader
            JsonToken token = reader.peek();

            // If token is a NAME, continue, otherwise, skip it
            if (token.name().equals(JsonToken.NAME.toString())) {
                String name = reader.nextName();

                // Find "cuisines" in JSON response
                if (name.equals("cuisines")) {
                    reader.beginArray();

                    // Read all cuisines
                    while (reader.hasNext()) {

                        // Get each cuisine from the response
                        reader.beginObject();
                        if (reader.nextName().equals("cuisine")) {
                            Log.i(TAG, "Found a cuisine!");
                            Log.i(TAG, "Getting its information...");

                            list.add(getCuisine(reader));
                        } else {
                            // The object wasn't a "cuisine" object, so skip it
                            // This should not happen
                            reader.skipValue();
                        }
                        reader.endObject();
                    }

                    // End cuisines array
                    reader.endArray();
                } else {
                    Log.i(TAG, "Skipping " + name);
                    reader.skipValue();
                }
            } else {
                reader.skipValue();
            }
        }

        // End root object
        reader.endObject();
        reader.close();

        return list;
    }

    /**
     * Reads the given JSON text of restaurant and returns a Resto object
     *
     * @param reader A {@code JsonReader} with the next object being the root
     * @return A List of one {@code Resto} object with the information from the JSON.
     * @throws IOException If an IOException occurs with the JSON text
     */
    public List<Resto> readRestoInformation(JsonReader reader) throws IOException {

        // Start reading the text, by beginning the root object.
        reader.beginObject();

        HashMap<String, String> map = getRestoMap(reader);

        // Get required information from map
        Resto resto = getResto(map);

        // End root object
        reader.endObject();
        reader.close();

        // Create a list
        List<Resto> restos = new ArrayList<>();
        restos.add(resto);

        return restos;
    }

    /**
     * Reads the given JSON text of restaurants and returns a List of Resto objects
     *
     * @param reader   A {@code JsonReader} with the next object being the root
     * @param jsonName The string in the JSON to find restaurant objects
     * @return A list of {@code Resto}s. May return an empty list if there are no results found.
     * @throws IOException If an IOException occurs with the JSON text
     */
    public List<RestoItem> readResto(JsonReader reader, String jsonName) throws IOException {
        List<RestoItem> list = new ArrayList<>();

        // Start reading the text, by beginning the root object.
        reader.beginObject();
        while (reader.hasNext()) {

            // Get next token in the reader
            JsonToken token = reader.peek();

            // If token is a NAME, continue, otherwise, skip it
            if (token.name().equals(JsonToken.NAME.toString())) {
                String name = reader.nextName();

                // Find given name in JSON response
                if (name.equals(jsonName)) {
                    reader.beginArray();

                    // Read all restaurant objects
                    while (reader.hasNext()) {

                        // Get each restaurant from the response
                        reader.beginObject();
                        if (reader.nextName().equals("restaurant")) {
                            Log.i(TAG, "Found a restaurant!");
                            Log.i(TAG, "Getting its information...");

                            reader.beginObject();
                            HashMap<String, String> map = getRestoMap(reader);
                            reader.endObject();

                            // Get RestoItem information from map
                            list.add(getRestoItem(map));
                        } else {
                            // The object wasn't a "restaurant" object, so skip it
                            // This should not happen
                            reader.skipValue();
                        }
                        reader.endObject();
                    }

                    // End restaurants array
                    reader.endArray();
                } else {
                    Log.i(TAG, "Skipping " + name);
                    reader.skipValue();
                }
            } else {
                reader.skipValue();
            }
        }

        // End root object
        reader.endObject();
        reader.close();

        return list;
    }

    /**
     * Gets a single cuisine object from the JSON
     *
     * @param reader A {@code JsonReader} with the next object being a cuisine
     * @return A HashMap with two values, the id, and the cuisine name
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private Cuisine getCuisine(JsonReader reader) throws IOException {
        Cuisine cuisine = new Cuisine();

        reader.beginObject();

        // cuisine_id
        reader.nextName();
        int id = reader.nextInt();
        Log.i(TAG, "Found id: " + id);
        cuisine.setId(id);

        // cuisine_name
        reader.nextName();
        String name = reader.nextString();
        Log.i(TAG, "Found name: " + name);
        cuisine.setName(name);

        reader.endObject();

        return cuisine;
    }

    /**
     * Gets the restaurants information from the map and returns a data bean.
     *
     * @param map The map containing key-value pairs of fields found in a {@code Resto} object.
     * @return A {@code Resto} object with the information retrieved from the map.
     */
    private Resto getResto(HashMap<String, String> map) {
        Resto resto = new Resto();

        String id = map.get(RESTO_ID);
        String phone = map.get(RESTO_PHONE);
        String url = map.get(RESTO_URL);
        String latitude = map.get(RESTO_LOCATION_LATITUDE);
        String longitude = map.get(RESTO_LOCATION_LONGITUDE);
        String name = map.get(RESTO_NAME);
        String cuisines = map.get(RESTO_CUISINES);
        String priceRange = map.get(RESTO_PRICE);
        String city = map.get(RESTO_LOCATION_CITY);
        String province = map.get(RESTO_LOCATION_PROVINCE);
        String postal = map.get(RESTO_LOCATION_POSTAL);
        String addressString = map.get(RESTO_LOCATION_ADDRESS);
        String country = map.get(RESTO_LOCATION_COUNTRY);

        if (id != null) {
            resto.setZomatoId(Integer.parseInt(id));
        }
        if (phone != null) {
            resto.setPhone(Long.parseLong(phone));
        }
        resto.setName(name);

        if(cuisines.indexOf(",") > 0){
            cuisines = cuisines.split(",")[0];
        }

        resto.setGenre(cuisines);
        resto.setLink(url);
        resto.setPriceRange(priceRange);

        Address address = new Address();
        address.setCity(city);
        address.setPostal(postal);
        address.setProvince(province);
        address.setCountry(country);


        if (latitude != null && longitude != null) {
            address.setLatitude(Double.parseDouble(latitude));
            address.setLongitude(Double.parseDouble(longitude));
        }
        address.setAddress(addressString);

        Log.d(TAG, "adr contains: " + address);

        resto.setAddress(address);

        return resto;
    }

    /**
     * Gets the restaurants information from the map and returns a data bean.
     *
     * @param map The map containing key-value pairs of fields found in a {@code RestoItem} object.
     * @return A {@code RestoItem} object with the information retrieved from the map.
     */
    private RestoItem getRestoItem(HashMap<String, String> map) {
        RestoItem resto = new RestoItem();

        String id = map.get(RESTO_ID);
        String phone = map.get(RESTO_PHONE);
        String rating = map.get(RESTO_REVIEW_AVG);
        String latitude = map.get(RESTO_LOCATION_LATITUDE);
        String longitude = map.get(RESTO_LOCATION_LONGITUDE);
        String name = map.get(RESTO_NAME);
        String priceRange = map.get(RESTO_PRICE);
        String city = map.get(RESTO_LOCATION_CITY);
        String address = map.get(RESTO_LOCATION_ADDRESS);

        if (id != null) {
            resto.setZomatoId(Integer.parseInt(id));
        }
        if (phone != null) {
            resto.setPhone(Long.parseLong(phone));
        }
        if (rating != null) {
            resto.setRating(Double.parseDouble(rating));
        }
        if (latitude != null && longitude != null) {
            resto.setLatitude(Double.parseDouble(latitude));
            resto.setLongitude(Double.parseDouble(longitude));
        }

        resto.setName(name);
        resto.setPriceRange(priceRange);
        resto.setCity(city);
        resto.setAddress(address);

        return resto;
    }

    /**
     * Gets the location information of the restaurant
     *
     * @param reader A {@code JsonReader} with the next object being a location
     * @return An associative array which holds the values retrieved from the location object in the JSON
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private HashMap<String, String> getLocationMap(JsonReader reader) throws IOException {
        HashMap<String, String> values = new HashMap<>();
        reader.beginObject();
        for (int i = 0; i < 8; i++) {
            String name = reader.nextName();

            switch (name) {
                case "address":
                    String address = reader.nextString();
                    Log.d(TAG, "full adr is: " + address);
                    // ex.: 1 Place Ville Marie, Montreal, QC H3B3Y1
                    String[] addressPieces = address.split(", ");

                    // Get province
                    String province = addressPieces[2].split(" ", 2)[0];

                    Log.i(TAG, "Found province: " + province);
                    Log.i(TAG, "Found address: " + address);

                    values.put(RESTO_LOCATION_ADDRESS, addressPieces[0]);
                    values.put(RESTO_LOCATION_PROVINCE, province.isEmpty() ? "QC" : province);
                    break;
                case "city":
                    String city = reader.nextString();
                    Log.i(TAG, "Found city: " + city);

                    values.put(RESTO_LOCATION_CITY, city.isEmpty() ? "QC" : city);
                    break;
                case "latitude":
                    double latitude = reader.nextDouble();
                    Log.i(TAG, "Found latitude: " + latitude);

                    values.put(RESTO_LOCATION_LATITUDE, latitude + "");
                    break;
                case "longitude":
                    double longitude = reader.nextDouble();
                    Log.i(TAG, "Found longitude: " + longitude);

                    values.put(RESTO_LOCATION_LONGITUDE, longitude + "");
                    break;
                case "zipcode":
                    String postal = reader.nextString();
                    Log.i(TAG, "Found postal code: " + postal);

                    // sometimes Zomato chops half the postal code out which causes me to unable to sync to heroku.
                    values.put(RESTO_LOCATION_POSTAL, postal.length() >= 6 ? postal : "H3Z1A4");
                    break;
                default:
                    Log.i(TAG, "The " + name + " contains: " + reader.nextString());
                    //reader.skipValue();
            }

            values.put(RESTO_LOCATION_COUNTRY, "CA");
        }

        Log.d(TAG, "values contains: " + values);
        reader.endObject();

        return values;
    }

    /**
     * Gets the information of a restaurant from the {@code JsonReader}
     *
     * @param reader A {@code JsonReader} with the next object being a restaurant
     * @return A {@code RestoItem} with the data from the {@code JsonReader}
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private HashMap<String, String> getRestoMap(JsonReader reader) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        JsonToken jsonToken;

        // Default values for price range field
        int priceRange = 0;
        // Get user's currency by locale
        String currency = mContext.getString(R.string.currency);

        // Read all fields in "restaurant" object
        while (reader.hasNext()) {
            jsonToken = reader.peek();

            if (jsonToken == JsonToken.NAME) {
                // Get name of next token
                String name = reader.nextName();
                // Peek at next value and if it's not null, find out which of the following it is
                jsonToken = reader.peek();
                if (jsonToken != JsonToken.NULL) {
                    switch (name) {
                        case "id": // Zomato's id for the restaurant
                            int id = reader.nextInt();
                            Log.i(TAG, "Found id: " + id);

                            map.put(RESTO_ID, id + "");
                            break;
                        case "name": // Name of the restaurant
                            String restoName = reader.nextString();
                            Log.i(TAG, "Found name: " + restoName);

                            map.put(RESTO_NAME, restoName);
                            break;
                        case "url":
                            String url = reader.nextString();
                            Log.i(TAG, "Found URL: " + url);

                            map.put(RESTO_URL, url);
                            break;
                        case "location": // The location of the restaurant as an object
                            HashMap<String, String> locations = getLocationMap(reader);

                            map.put(RESTO_LOCATION_CITY, locations.get(RESTO_LOCATION_CITY));
                            map.put(RESTO_LOCATION_PROVINCE, locations.get(RESTO_LOCATION_PROVINCE));
                            map.put(RESTO_LOCATION_LATITUDE, locations.get(RESTO_LOCATION_LATITUDE));
                            map.put(RESTO_LOCATION_LONGITUDE, locations.get(RESTO_LOCATION_LONGITUDE));
                            map.put(RESTO_LOCATION_ADDRESS, locations.get(RESTO_LOCATION_ADDRESS));
                            map.put(RESTO_LOCATION_POSTAL, locations.get(RESTO_LOCATION_POSTAL));
                            map.put(RESTO_LOCATION_COUNTRY, locations.get(RESTO_LOCATION_COUNTRY));
                            break;
                        case "cuisines":
                            String cuisines = reader.nextString();
                            Log.i(TAG, "Found cuisines: " + cuisines);

                            map.put(RESTO_CUISINES, cuisines);
                            break;
                        case "price_range": // Price range of restaurant, as a number from 1-4, where 1 is pocket-friendly and 4 is expensive
                            priceRange = reader.nextInt();
                            Log.i(TAG, "Found price range: " + priceRange);
                            break;
                        case "user_rating": // The reviews from the restaurant, as an array of objects
                            HashMap<String, String> reviews = getReviewMap(reader);

                            map.put(RESTO_REVIEW_AVG, reviews.get(RESTO_REVIEW_AVG));
                            break;
                        case "phone_numbers": // The restaurant's phone number, as a long with the parentheses, spaces and dashes removed
                            String phone = reader.nextString();
                            Log.i(TAG, "Found phone number: " + phone);

                            // Replace all parentheses, spaces and dashes with empty string
                            phone = phone.replaceAll("\\(|\\)| |-", "");
                            Log.i(TAG, "Changed phone number to: " + phone);

                            map.put(RESTO_PHONE, phone);
                            break;
                        default: // Information we don't need
                            Log.i(TAG, "The " + name + " was ignored.");
                            reader.skipValue();
                    }
                }
            } else if (jsonToken == JsonToken.END_OBJECT) {
                // Change price range
                for (int i = 1; i < priceRange; i++) {
                    currency += currency;
                }
                Log.i(TAG, "Changed price range to " + currency);
                map.put(RESTO_PRICE, currency);

                return map;
            } else {
                Log.i(TAG, "Skipping " + jsonToken.name());
                reader.skipValue();
            }
        }

        // Change price range
        for (int i = 1; i < priceRange; i++) {
            currency += currency;
        }
        Log.i(TAG, "Changed price range to " + currency);
        map.put(RESTO_PRICE, currency);

        return map;
    }

    /**
     * Gets all the reviews from the JSON
     *
     * @param reader A {@code JsonReader} with the next object being an array of reviews
     * @return An list of all {@code Review} objects in the JSON, or an empty list if there are no
     * reviews in the JSON
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private HashMap<String, String> getReviewMap(JsonReader reader) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        reader.beginObject();

        for (int i = 0; i < 4; i++) {
            String name = reader.nextName();
            switch (name) {
                case "aggregate_rating":
                    double avg = reader.nextDouble();
                    Log.i(TAG, "Found average rating: " + avg);

                    map.put(RESTO_REVIEW_AVG, avg + "");
                    break;
                default:
                    reader.skipValue();
            }
        }

        reader.endObject();
        return map;
    }
}
