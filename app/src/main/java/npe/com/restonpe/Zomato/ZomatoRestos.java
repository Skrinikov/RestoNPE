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
import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.Beans.Review;
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

    private static final String RESTO_LOCATION_ADDRESS = "address";
    private static final String RESTO_LOCATION_CITY = "city";
    private static final String RESTO_LOCATION_LATITUDE = "latitude";
    private static final String RESTO_LOCATION_LONGITUDE = "longitude";
    private static final String RESTO_LOCATION_POSTAL = "postal";

    private static final String RESTO_RATING_AVG = "rating";

    private static final String RESTO_USER_NAME = "username";

    private Context mContext;

    public ZomatoRestos(Context context) {
        this.mContext = context;
    }

    /**
     * Finds the restaurants near the given latitude and longitude
     *
     * @param latitude The latitude to use to find the restaurants
     * @param longitude The longitude to use to find the restaurants
     */
    public void findNearbyRestos(double latitude, double longitude) {
        Log.i(TAG, "Finding restaurants near: " + latitude + ", " + longitude);

        RestoNetworkManager<RestoItem> restoNetworkManager = new RestoNetworkManager<RestoItem>(mContext) {
            @Override
            public void onPostExecute(List<RestoItem> list) {
                // TODO put list in AdpaterView
            }

            @Override
            protected List<RestoItem> readJson(JsonReader reader) {
                List<RestoItem> list = null;
                try {
                    list = readNearbyRestosJson(reader);
                } catch (IOException e) {
                    Log.e(TAG, "An IOException occurred while reading the JSON: " + e.getMessage());
                }

                return list;
            }
        };

        restoNetworkManager.findNearbyRestos(latitude, longitude);
    }

    /**
     * Finds all the information of the restaurant with the given id.
     *
     * @param id The id of the restaurant
     */
    public void findRestoInformation(int id) {
        Log.i(TAG, "Finding restaurant with id " + id);

        RestoNetworkManager<Resto> restoNetworkManager = new RestoNetworkManager<Resto>(mContext) {
            @Override
            public void onPostExecute(List<Resto> list) {
                // TODO put list in AdpaterView
            }

            @Override
            protected List<Resto> readJson(JsonReader reader) {
                List<Resto> list = null;
                try {
                    list = readRestoJson(reader);
                } catch (IOException e) {
                    Log.e(TAG, "An IOException occurred while reading the JSON: " + e.getMessage());
                }

                return list;
            }
        };

        restoNetworkManager.findRestoInformation(id);
    }

    public void findRestos(String name, String genre, String city) {
        Log.i(TAG, String.format("Finding restaurants matching name=%1$s, genre=%2$s, city=%3$s", name, genre, city));

        // TODO get lat and long of given city
        // TODO get id of genre somehow
        String latitude = "";
        String longitude = "";

        RestoNetworkManager<RestoItem> restoNetworkManager = new RestoNetworkManager<RestoItem>(mContext) {
            @Override
            public void onPostExecute(List<RestoItem> list) {
                // TODO put list in AdpaterView
            }

            @Override
            protected List<RestoItem> readJson(JsonReader reader) {
                List<RestoItem> list = null;
                try {
                    list = readRestosJson(reader);
                } catch (IOException e) {
                    Log.e(TAG, "An IOException occurred while reading the JSON: " + e.getMessage());
                }

                return list;
            }
        };

        restoNetworkManager.findRestos(name, latitude, longitude, genre);
    }

    /**
     * Reads the given JSON text and returns a List of nearby Restaurant objects
     *
     * @param reader The JSON reader
     *
     * @return A list of {@code RestoItem}s. May return an empty list if there are no results found.
     *
     * @throws IOException If an IOException occurs with the JSON text
     */
    private List<RestoItem> readNearbyRestosJson(JsonReader reader) throws IOException {
        List<RestoItem> list = new ArrayList<>();

        // Start reading the text, by beginning the root object.
        reader.beginObject();
        while (reader.hasNext()) {

            // Get next token in the reader
            JsonToken token = reader.peek();

            // If token is a NAME, continue, otherwise, skip it
            if (token.name().equals(JsonToken.NAME.toString())) {
                String name = reader.nextName();

                // Find "nearby_restaurants" in JSON response
                if (name.equals("nearby_restaurants")) {
                    reader.beginArray();

                    // Read all nearby restaurants
                    while (reader.hasNext()) {
                        Log.i(TAG, "Found a restaurant!");
                        Log.i(TAG, "Getting its information...");

                        // Get each restaurant from the response
                        reader.beginObject();
                        if (reader.nextName().equals("restaurant")) {
                            list.add(getRestoItem(reader));
                        } else {
                            // The object wasn't a "restaurant" object, so skip it
                            reader.skipValue();
                        }
                        reader.endObject();
                    }

                    // End "nearby_restaurants" array
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
     * Gets all the information of the restaurant object next in the JSON reader
     *
     * @param reader The JSON reader
     *
     * @return A {@code List} with 1 item that holds the bean with the information of the
     * restaurant. or an empty list if a restaurant with the given id does not exist.
     *
     * @throws IOException If an IOException occurs with the JSON text
     */
    private List<Resto> readRestoJson(JsonReader reader) throws IOException {
        List<Resto> list = new ArrayList<>();
        Resto resto = null;

        // Begin root object
        reader.beginObject();

        while (reader.hasNext()) {
            resto = new Resto();
            JsonToken token = reader.peek();

            if (token.name().equals(JsonToken.NAME.toString())) {

                String name = reader.nextName();
                switch (name) {
                    case "id":
                        int id = reader.nextInt();
                        Log.i(TAG, "Found id: " + id);

                        resto.setId(id);
                        break;
                    case "name":
                        String restoName = reader.nextString();
                        Log.i(TAG, "Found name: " + restoName);

                        resto.setName(name);
                        break;
                    case "url":
                        String url = reader.nextString();
                        Log.i(TAG, "Found URL: " + url);

                        resto.setLink(url);
                        break;
                    case "location":
                        HashMap<String, String> locations = handleLocation(reader);
                        Address addressItem = new Address();

                        addressItem.setAddress(locations.get(RESTO_LOCATION_ADDRESS));
                        addressItem.setCity(locations.get(RESTO_LOCATION_CITY));
                        addressItem.setLatitude(Double.parseDouble(locations.get(RESTO_LOCATION_LATITUDE)));
                        addressItem.setLongitude(Double.parseDouble(locations.get(RESTO_LOCATION_LONGITUDE)));
                        addressItem.setPostal(locations.get(RESTO_LOCATION_POSTAL));

                        resto.addAddress(addressItem);
                        break;
                    case "price_range":
                        int priceRange = reader.nextInt();
                        Log.i(TAG, "Found price range: " + priceRange);

                        resto.setPriceRange(priceRange + "");
                        break;
                    case "all_reviews":
                        // TODO
//                      Hashmap<String, String> values = handleReview(reader);
                        reader.skipValue();
                        break;
                    case "cuisines":
                        String cuisines = reader.nextString();
                        Log.i(TAG, "Found cuisines: " + cuisines);

                        resto.setGenre(cuisines);
                        break;
                    case "phone_numbers":
                        String phone = reader.nextString();
                        Log.i(TAG, "Found phone number: " + phone);

                        // Replace all parentheses, spaces and dashes with empty string
                        phone = phone.replaceAll("\\(|\\)| |-", "");
                        Log.i(TAG, "Changed phone number to: " + phone);

                        resto.setPhone(Long.parseLong(phone));
                        break;
                    default:
                        Log.i(TAG, "The " + name + " was ignored.");
                        reader.skipValue();
                }
            } else {
                reader.skipValue();
            }
        }

        list.add(resto);

        // End root object
        reader.endObject();
        reader.close();

        return list;
    }

    /**
     *
     * @param reader
     * @return
     * @throws IOException
     */
    private List<RestoItem> readRestosJson(JsonReader reader) throws IOException {
        List<RestoItem> list = null;

        // Begin root object
        reader.beginObject();

        // TODO

        // End root object
        reader.endObject();
        reader.close();

        return list;
    }

    /**
     * Gets the information of a restaurant from the {@code JsonReader}
     *
     * @param reader A {@code JsonReader} with the next object being a restaurant
     * @return A {@code RestoItem} with the data from the {@code JsonReader}
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private RestoItem getRestoItem(JsonReader reader) throws IOException {
        RestoItem restoItem = new RestoItem();

        // Begin the restaurant object
        reader.beginObject();

        // Read all fields
        for (int i = 0; i < 21; i++) {
            JsonToken token = reader.peek();

            if (token.name().equals(JsonToken.NAME.toString())) {

                String name = reader.nextName();

                switch (name) {
                    case "id":
                        int id = reader.nextInt();
                        Log.i(TAG, "Found id: " + id);

                        restoItem.setId(id);
                        break;
                    case "name": // Name of the restaurant
                        String restoName = reader.nextString();
                        Log.i(TAG, "Found name: " + restoName);

                        restoItem.setName(restoName);
                        break;
                    case "price_range": // Price range of restaurant
                        int priceRange = reader.nextInt();
                        Log.i(TAG, "Found price range: " + priceRange);

                        restoItem.setPriceRange(priceRange + "");
                        break;
                    case "location": // The restaurant's locations. A restaurant may have multiple locations
                        HashMap<String, String> location = handleLocation(reader);

                        restoItem.setAddress(location.get(RESTO_LOCATION_ADDRESS));
                        restoItem.setCity(location.get(RESTO_LOCATION_CITY));
                        restoItem.setLatitude(Double.parseDouble(location.get(RESTO_LOCATION_LATITUDE)));
                        restoItem.setLongitude(Double.parseDouble(location.get(RESTO_LOCATION_LONGITUDE)));
                        break;
                    case "user_rating": // The restaurant's average rating
                        HashMap<String, String> rating = handleRating(reader);

                        double avgRating = Double.parseDouble(rating.get(RESTO_RATING_AVG));
                        Log.i(TAG, "Found average rating: " + avgRating);

                        restoItem.setRating(avgRating);
                        break;
                    default:
                        Log.i(TAG, "The " + name + " was ignored.");
                        reader.skipValue();
                }
            } else {
                Log.i(TAG, "Skipping " + token.name());
                reader.skipValue();
            }
        }

        // End restaurant object
        reader.endObject();

        return restoItem;
    }

    /**
     * Gets the location information of the restaurant
     *
     * @param reader    A {@code JsonReader} with the next object being a restaurant
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
     * Gets the average rating information of the restaurant.
     *
     * @param reader    A {@code JsonReader} with the next object being a restaurant
     * @throws IOException If an IOException occurs while reading the JSON
     * @return
     */
    private HashMap<String, String> handleRating(JsonReader reader) throws IOException {
        HashMap<String, String> values = new HashMap<>();
        reader.beginObject();

        for (int i = 0; i < 4; i++) {
            String name = reader.nextName();

            switch (name) {
                case "aggregate_rating":
                    double rating = reader.nextDouble();
                    Log.i(TAG, "Found rating: " + rating);

                    values.put(RESTO_RATING_AVG, rating + "");
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
     *
     * @param reader
     * @return
     * @throws IOException
     */
    private HashMap<String, String> handleReview(JsonReader reader) throws IOException {
        HashMap<String, String> values = new HashMap<>();


        return values;
    }

    /**
     *
     * @param reader
     * @return
     * @throws IOException
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
                    HashMap<String, String> values = handleUser(reader);

                    review.setSubmitter(values.get(RESTO_USER_NAME));
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
     *
     * @param reader
     * @return
     * @throws IOException
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
