package npe.com.restonpe.Zomato;

import android.content.Context;
import android.location.Location;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import npe.com.restonpe.Beans.Address;
import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.Review;
import npe.com.restonpe.Services.RestoLocationManager;
import npe.com.restonpe.Services.RestoNetworkManager;

/**
 * Manages calls to the Zomato API.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 30/11/2016
 */
public class ZomatoRestos {

    private static final String TAG = ZomatoRestos.class.getSimpleName();

    private static final String RESTO_CUISINE_ID = "id";
    private static final String RESTO_CUISINE_NAME = "name";

    private static final String RESTO_LOCATION_ADDRESS = "address";
    private static final String RESTO_LOCATION_CITY = "city";
    private static final String RESTO_LOCATION_LATITUDE = "latitude";
    private static final String RESTO_LOCATION_LONGITUDE = "longitude";
    private static final String RESTO_LOCATION_POSTAL = "postal";

    private static final String RESTO_USER_NAME = "username";

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
     * Finds all cuisines in the city in which the given latitude and longitude fall.
     *
     * @param latitude Any latitudinal point in a city
     * @param longitude Any longitudinal point in the same city
     */
    public void findCuisines(String latitude, String longitude) {
        Log.i(TAG, "Finding cuisines near: " + latitude + ", " + longitude);

        RestoNetworkManager<HashMap<String, String>> restoNetworkManager = new RestoNetworkManager<HashMap<String, String>>(mContext) {
            @Override
            public void onPostExecute(List<HashMap<String, String>> list) {

            }

            @Override
            protected List<HashMap<String, String>> readJson(JsonReader reader) {
                List<HashMap<String, String>> list = null;
                try {
                    list = readCuisines(reader);
                } catch (IOException e) {
                    Log.e(TAG, "An IOException occurred while reading the JSON: " + e.getMessage());
                }

                return list;
            }
        };

        restoNetworkManager.findCuisines(latitude, longitude);
    }

    /**
     * Finds the restaurants near the given latitude and longitude
     *
     * @param latitude The latitude to use to find the restaurants
     * @param longitude The longitude to use to find the restaurants
     */
    public void findNearbyRestos(String latitude, String longitude) {
        Log.i(TAG, "Finding restaurants near: " + latitude + ", " + longitude);

        RestoNetworkManager<Resto> restoNetworkManager = new RestoNetworkManager<Resto>(mContext) {
            @Override
            public void onPostExecute(List<Resto> list) {
                // TODO put list in AdpaterView
            }

            @Override
            protected List<Resto> readJson(JsonReader reader) {
                List<Resto> list = null;
                try {
                    list = readRestoJson(reader, "nearby_restaurants");
                } catch (IOException e) {
                    Log.e(TAG, "An IOException occurred while reading the JSON: " + e.getMessage());
                }

                return list;
            }
        };

        restoNetworkManager.findNearbyRestos(latitude, longitude);
    }

    /**
     * Finds the restaurants from the Zomato API which match the given search criteria
     *
     * @param name The name for which to search
     * @param city The city in which to search
     * @param cuisines The cuisines for which to search
     */
    public void findRestos(String name, String city, Integer[] cuisines) {
        Log.i(TAG, String.format("Finding restaurants matching name=%1$s, city=%2$s", name, city));

        // Get latitude and longitude of given city.
        RestoLocationManager restoLocationManager = new RestoLocationManager(mContext) {
            @Override
            public void onLocationChanged(Location location) {
                // Do nothing
            }
        };
        android.location.Address address = restoLocationManager.getLocationFromName(city);

        RestoNetworkManager<Resto> restoNetworkManager = new RestoNetworkManager<Resto>(mContext) {
            @Override
            public void onPostExecute(List<Resto> list) {
                // TODO put list in AdpaterView
            }

            @Override
            protected List<Resto> readJson(JsonReader reader) {
                List<Resto> list = null;
                try {
                    list = readRestoJson(reader, "restaurants");
                } catch (IOException e) {
                    Log.e(TAG, "An IOException occurred while reading the JSON: " + e.getMessage());
                }

                return list;
            }
        };

        // If an address was returned, it was not vague, so get the lat/long and continue.
        if (address != null) {
            String latitude = address.getLatitude() + "";
            String longitude = address.getLongitude() + "";

            restoNetworkManager.findRestos(name, latitude, longitude, cuisines);
        } else {
            // TODO No location was found
        }
    }

    /**
     * Reads the given JSON text of restaurants and returns a List of Resto objects
     *
     * @param reader A {@code JsonReader} with the next object being the root
     * @param jsonName The string in the JSON to find restaurant objects
     *
     * @return A list of {@code Resto}s. May return an empty list if there are no results found.
     * @throws IOException If an IOException occurs with the JSON text
     */
    private List<Resto> readRestoJson(JsonReader reader, String jsonName) throws IOException {
        List<Resto> list = new ArrayList<>();

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

                    // Read all nearby restaurants
                    while (reader.hasNext()) {

                        // Get each restaurant from the response
                        reader.beginObject();
                        if (reader.nextName().equals("restaurant")) {
                            Log.i(TAG, "Found a restaurant!");
                            Log.i(TAG, "Getting its information...");
                            list.add(getResto(reader));
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
     * Gets the ID's of the given list of cuisines from the Zomato API.
     *
     * @param reader A {@code JsonReader} with the next object being a cuisine array
     *
     * @return An array of id's. The array could be of size zero or smaller than the given array if
     * some or all of the given cuisines were not found in the Zomato API.
     * @throws IOException If an IOException occurs with the JSON text
     */
    private List<HashMap<String, String>> readCuisines(JsonReader reader) throws  IOException{
        List<HashMap<String, String>> list = new ArrayList<>();

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
     * Gets a single cuisine object from the JSON
     *
     * @param reader A {@code JsonReader} with the next object being a cuisine
     *
     * @return A HashMap with two values, the id, and the cuisine name
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private HashMap<String, String> getCuisine(JsonReader reader) throws IOException {
        HashMap<String, String> cuisine = new HashMap<>();

        reader.beginObject();

        // cuisine_id
        reader.nextName();
        int id = reader.nextInt();
        Log.i(TAG, "Found id: " + id);
        cuisine.put(RESTO_CUISINE_ID, id + "");

        // cuisine_name
        reader.nextName();
        String name = reader.nextString();
        Log.i(TAG, "Found name: " + name);
        cuisine.put(RESTO_CUISINE_NAME, name);

        reader.endObject();

        return cuisine;
    }

    /**
     * Gets the information of a restaurant from the {@code JsonReader}
     *
     * @param reader A {@code JsonReader} with the next object being a restaurant
     *
     * @return A {@code RestoItem} with the data from the {@code JsonReader}
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private Resto getResto(JsonReader reader) throws IOException {
        Resto resto = new Resto();

        // Default values for price range field
        int priceRange = 0;
        String currency = "";

        // Begin the restaurant object
        reader.beginObject();

        // Read all fields in "restaurant" object
        for (int i = 0; i < 21; i++) {
            JsonToken token = reader.peek();

            if (token.name().equals(JsonToken.NAME.toString())) {

                String name = reader.nextName();

                switch (name) {
                    case "id": // Zomato's id for the restaurant
                        int id = reader.nextInt();
                        Log.i(TAG, "Found id: " + id);

                        resto.setId(id);
                        break;
                    case "name": // Name of the restaurant
                        String restoName = reader.nextString();
                        Log.i(TAG, "Found name: " + restoName);

                        resto.setName(restoName);
                        break;
                    case "url": // The link to the restaurant's page on Zomato
                        String url = reader.nextString();
                        Log.i(TAG, "Found URL: " + url);

                        resto.setLink(url);
                        break;
                    case "price_range": // Price range of restaurant, as a number from 1-4, where 1 is pocket-friendly and 4 is expensive
                        priceRange = reader.nextInt();
                        Log.i(TAG, "Found price range: " + priceRange);
                        break;
                    case "currency": // The currency used in the restaurant, ex. $, ₯, etc.
                        currency = reader.nextString();
                        Log.i(TAG, "Found currency: " + currency);
                        break;
                    case "location": // The location of the restaurant as an object
                        HashMap<String, String> locations = handleLocation(reader);
                        Address addressItem = new Address();

                        addressItem.setAddress(locations.get(RESTO_LOCATION_ADDRESS));
                        addressItem.setCity(locations.get(RESTO_LOCATION_CITY));
                        addressItem.setLatitude(Double.parseDouble(locations.get(RESTO_LOCATION_LATITUDE)));
                        addressItem.setLongitude(Double.parseDouble(locations.get(RESTO_LOCATION_LONGITUDE)));
                        addressItem.setPostal(locations.get(RESTO_LOCATION_POSTAL));

                        // Changed here because chains are no longer used.
                        resto.setAddress(addressItem);
                        break;
                    case "all_reviews": // The reviews from the restaurant, as an array of objects
                        List<Review> reviews = handleReview(reader);
                        resto.setReviews(reviews);
                        break;
                    case "cuisines": // The type of the food the restaurant serves in a comma-separated list
                        String cuisines = reader.nextString();
                        Log.i(TAG, "Found cuisines: " + cuisines);

                        resto.setGenre(cuisines);
                        break;
                    case "phone_numbers": // The restaurant's phone number, as a long with the parentheses, spaces and dashes removed
                        String phone = reader.nextString();
                        Log.i(TAG, "Found phone number: " + phone);

                        // Replace all parentheses, spaces and dashes with empty string
                        phone = phone.replaceAll("\\(|\\)| |-", "");
                        Log.i(TAG, "Changed phone number to: " + phone);

                        resto.setPhone(Long.parseLong(phone));
                        break;
                    default: // Information we don't need
                        Log.i(TAG, "The " + name + " was ignored.");
                        reader.skipValue();
                }
            } else {
                Log.i(TAG, "Skipping " + token.name());
                reader.skipValue();
            }
        }

        // Change price range
        for (int i = 1; i < priceRange; i++) {
            currency += currency;
        }
        Log.i(TAG, "Changed price range to " + currency);
        resto.setPriceRange(currency);

        // End restaurant object
        reader.endObject();

        return resto;
    }


    /**
     * Gets a single review's information from the JSON
     *
     * @param reader    A {@code JsonReader} with the next object being a review
     *
     * @return A {@code Review} which holds the values retrieved from the review object in the JSON
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private Review getReview(JsonReader reader) throws IOException {
        Review review = new Review();
        reader.beginObject();

        for (int i = 0; i < 10; i++) {
            String name = reader.nextName();

            switch (name) {
                case "rating":
                    double rating = reader.nextDouble();
                    Log.i(TAG, "Found rating: " + rating);

                    review.setRating(rating);
                    break;
                case "review_text":
                    String text = reader.nextString();
                    Log.i(TAG, "Found review text: " + text);

                    review.setContent(text);
                    break;
                case "likes":
                    int likes = reader.nextInt();
                    Log.i(TAG, "Found likes: " + likes);

                    review.setLikes(likes);
                    break;
                case "user":
                    HashMap<String, String> users = handleUser(reader);

                    review.setSubmitter(users.get(RESTO_USER_NAME));
                    break;
                default:
                    Log.i(TAG, "The " + name + " was ignored.");
                    reader.skipValue();
            }
        }
        reader.endObject();

        return review;
    }


    /**
     * Gets the location information of the restaurant
     *
     * @param reader    A {@code JsonReader} with the next object being a location
     *
     * @return An associative array which holds the values retrieved from the location object in the JSON
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private HashMap<String, String> handleLocation(JsonReader reader) throws IOException {
        HashMap<String, String> values = new HashMap<>();
        reader.beginObject();

        for (int i = 0; i < 8; i++) {
            String name = reader.nextName();

            switch (name) {
                case "address":
                    String address = reader.nextString();
                    Log.i(TAG, "Found address: " + address);

                    values.put("address", RESTO_LOCATION_ADDRESS);
                    break;
                case "city":
                    String city = reader.nextString();
                    Log.i(TAG, "Found city: " + city);

                    values.put("city", RESTO_LOCATION_CITY);
                    break;
                case "latitude":
                    double latitude = reader.nextDouble();
                    Log.i(TAG, "Found latitude: " + RESTO_LOCATION_LATITUDE);

                    values.put("latitude", latitude + "");
                    break;
                case "longitude":
                    double longitude = reader.nextDouble();
                    Log.i(TAG, "Found longitude: " + RESTO_LOCATION_LONGITUDE);

                    values.put("longitude", longitude + "");
                    break;
                case "zipcode":
                    String postal = reader.nextString();
                    Log.i(TAG, "Found postal code: " + RESTO_LOCATION_POSTAL);

                    values.put("postal", postal);
                    break;
                default:
                    Log.i(TAG, "The " + name + " was ignored.");
                    reader.skipValue();
            }
        }
        reader.endObject();

        return values;
    }

    /**
     * Gets all the reviews from the JSON
     *
     * @param reader    A {@code JsonReader} with the next object being an array of reviews
     *
     * @return An list of all {@code Review} objects in the JSON, or an empty list if there are no
     * reviews in the JSON
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private List<Review> handleReview(JsonReader reader) throws IOException {
        List<Review> reviews = new ArrayList<>();
        reader.beginArray();

        JsonToken token = reader.peek();
        // While all_reviews array is not over
        while (!token.name().equals(JsonToken.END_ARRAY.toString())) {
            Review review = getReview(reader);
            reviews.add(review);
            token = reader.peek();
        }

        reader.endArray();
        return reviews;
    }

    /**
     * Gets the information of the user object in the JSON
     *
     * @param reader    A {@code JsonReader} with the next object being a user
     *
     * @return An associative array which holds the values retrieved from the user object in the JSON
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private HashMap<String, String> handleUser(JsonReader reader) throws IOException {
        HashMap<String, String> values = new HashMap<>();
        reader.beginObject();

        for (int i = 0; i < 8; i++) {
            String name = reader.nextName();

            switch (name) {
                case "zomato_handle":
                    String username = reader.nextString();
                    Log.i(TAG, "Found username: " + username);

                    values.put(RESTO_USER_NAME, username);
                    break;
                default:
                    Log.i(TAG, "The " + name + " was ignored.");
                    reader.skipValue();
            }
        }
        reader.endObject();

        return values;
    }
}
