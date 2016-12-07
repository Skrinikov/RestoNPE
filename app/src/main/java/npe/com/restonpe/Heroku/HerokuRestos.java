package npe.com.restonpe.Heroku;

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
import npe.com.restonpe.Beans.Review;
import npe.com.restonpe.Services.RestoNetworkManager;

/**
 * Manages calls to the Heroku app.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 06/12/2016
 */
public abstract class HerokuRestos {

    private static final String TAG = HerokuRestos.class.getSimpleName();

    // Resto fields
    private static final String RESTO_ID = "resto_id";
    private static final String RESTO_NAME = "name";
    private static final String RESTO_PHONE = "phone";
    private static final String RESTO_EMAIL = "email";
    private static final String RESTO_CIVIC_NUM = "civic_num";
    private static final String RESTO_SUITE = "suite";
    private static final String RESTO_STREET = "street";
    private static final String RESTO_CITY = "city";
    private static final String RESTO_POSTAL = "postal";
    private static final String RESTO_PROVINCE = "province";
    private static final String RESTO_COUNTRY = "country";
    private static final String RESTO_DESCRIPTION = "description";
    private static final String RESTO_PRICE = "price";
    private static final String RESTO_URL = "url";
    private static final String RESTO_GENRE_ID = "cuisine_id";
    private static final String RESTO_USER_ID = "rating_user_id";
    private static final String RESTO_LATITUDE = "latitude";
    private static final String RESTO_LONGITUDE = "longitude";
    private static final String RESTO_DISTANCE = "distance";

    // Review fields
    private static final String REVIEW_ID = "review_id";
    private static final String REVIEW_TITLE = "title";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_RATING = "rating";
    private static final String REVIEW_USER_ID = "review_user_id";
    private static final String REVIEW_RESTO_ID = "resto_id";

    private Context mContext;

    public HerokuRestos(Context context) {
        this.mContext = context;
    }

    /**
     * Finds the restaurants near the given latitude and longitude
     *
     * @param latitude  The latitude to use to find the restaurants
     * @param longitude The longitude to use to find the restaurants
     */
    public void findNearbyRestos(String latitude, String longitude) {
        Log.i(TAG, "Finding restaurants near: " + latitude + ", " + longitude);

        RestoNetworkManager<Review> restoNetworkManager = new RestoNetworkManager<Review>(mContext) {
            @Override
            public void onPostExecute(List<Review> list) {
                handleResults(list);
            }

            @Override
            protected List<Review> readJson(JsonReader reader) {
                List<Review> list = null;
                try {
                    list = readReviewJson(reader);
                } catch (IOException e) {
                    Log.e(TAG, "An IOException occurred while reading the JSON: " + e.getMessage());
                }

                return list;
            }
        };

        restoNetworkManager.findNearbyRestosFromHeroku(latitude, longitude);
    }

    /**
     * Finds the reviews of the restaurant with the given id
     *
     * @param id The id of the restaurant whose reviews are to be found
     */
    public void findRestoReviews(int id) {
        Log.i(TAG, "Finding reviews of restaurant with id " + id);

        RestoNetworkManager<Resto> restoNetworkManager = new RestoNetworkManager<Resto>(mContext) {
            @Override
            public void onPostExecute(List<Resto> list) {
                handleResults(list);
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

        restoNetworkManager.findRestoReviewsFromHeroku(id);
    }

    /**
     * This method will be called to handle the list that is returned as a response from the HTTP request.
     *
     * @param list The response from the HTTP request.
     */
    public abstract void handleResults(List<?> list);

    /**
     * Reads the given JSON text of restaurants and returns a List of Resto objects
     *
     * @param reader A {@code JsonReader} with the next object being the root
     * @return A list of {@code Resto}s. May return an empty list if there are no results found.
     * @throws IOException If an IOException occurs with the JSON text
     */
    private List<Resto> readRestoJson(JsonReader reader) throws IOException {
        List<Resto> list = new ArrayList<>();

        // Start reading the text, by beginning the root object.
        reader.beginArray();
        while (reader.hasNext()) {
            // Begin restaurant object
            reader.beginObject();

            HashMap<String, String> map = getResto(reader);
            list.add(getResto(map));

            // End restaurant object
            reader.endObject();
        }

        // End root object
        reader.endArray();
        reader.close();

        return list;
    }

    /**
     * Reads the given JSON text of review and returns a List of Review objects
     *
     * @param reader A {@code JsonReader} with the next object being the root
     * @return A list of {@code Review}s. May return an empty list if there are no results found.
     * @throws IOException If an IOException occurs with the JSON text
     */
    private List<Review> readReviewJson(JsonReader reader) throws IOException {
        List<Review> list = new ArrayList<>();

        // Start reading the text, by beginning the root object.
        reader.beginArray();
        while (reader.hasNext()) {
            // Begin restaurant object
            reader.beginObject();

            HashMap<String, String> map = getReview(reader);
            list.add(getReview(map));

            // End restaurant object
            reader.endObject();
        }

        // End root object
        reader.endArray();
        reader.close();

        return list;
    }

    /**
     * Gets the information of a restaurant from the {@code JsonReader}
     *
     * @param reader A {@code JsonReader} with the next object being a restaurant
     * @return A {@code HasMap} of key-value pairs with the keys being fields in the JSON that
     * correspond to fields of a {@code Review}
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private HashMap<String, String> getResto(JsonReader reader) throws IOException {
        HashMap<String, String> map = new HashMap<>();

        // Read all fields in "restaurant" object
        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (token.name().equals(JsonToken.NAME.toString())) {

                String name = reader.nextName();

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
                    case "link":
                        String url = reader.nextString();
                        Log.i(TAG, "Found URL: " + url);

                        map.put(RESTO_URL, url);
                        break;
                    case "civic_num":
                        int civicNum = reader.nextInt();
                        Log.i(TAG, "Found civic number: " + civicNum);

                        map.put(RESTO_CIVIC_NUM, civicNum + "");
                        break;
                    case "street":
                        String street = reader.nextString();
                        Log.i(TAG, "Found street: " + street);

                        map.put(RESTO_STREET, street);
                        break;
                    case "suite":
                        int suite = reader.nextInt();
                        Log.i(TAG, "Found suite: " + suite);

                        map.put(RESTO_SUITE, suite + "");
                        break;
                    case "city":
                        String city = reader.nextString();
                        Log.i(TAG, "Found city: " + city);

                        map.put(RESTO_CITY, city);
                        break;
                    case "province":
                        String province = reader.nextString();
                        Log.i(TAG, "Found province: " + province);

                        map.put(RESTO_PROVINCE, province);
                        break;
                    case "country":
                        String country = reader.nextString();
                        Log.i(TAG, "Found country: " + country);

                        map.put(RESTO_COUNTRY, country);
                        break;
                    case "postal_code":
                        String postal = reader.nextString();
                        Log.i(TAG, "Found postal code: " + postal);

                        map.put(RESTO_POSTAL, postal);
                        break;
                    case "latitude":
                        double latitude = reader.nextDouble();
                        Log.i(TAG, "Found latitude: " + latitude);

                        map.put(RESTO_LATITUDE, latitude + "");
                        break;
                    case "longitude":
                        double longitude = reader.nextDouble();
                        Log.i(TAG, "Found longitude: " + longitude);

                        map.put(RESTO_LONGITUDE, longitude + "");
                        break;
                    case "price":
                        double price = reader.nextDouble();
                        Log.i(TAG, "Found price: " + price);

                        map.put(RESTO_PRICE, price + "");
                        break;
                    case "phone":
                        String phone = reader.nextString();
                        Log.i(TAG, "Found phone number: " + phone);

                        // Replace all parentheses, spaces and dashes with empty string
                        phone = phone.replaceAll("\\(|\\)| |-", "");
                        Log.i(TAG, "Changed phone number to: " + phone);

                        map.put(RESTO_PHONE, phone);
                        break;
                    case "email":
                        String email = reader.nextString();
                        Log.i(TAG, "Found email: " + email);

                        map.put(RESTO_EMAIL, email);
                        break;
                    case "user_id":
                        int user = reader.nextInt();
                        Log.i(TAG, "Found user id: " + user);

                        map.put(RESTO_USER_ID, user + "");
                        break;
                    case "genre_id":
                        int genre = reader.nextInt();
                        Log.i(TAG, "Found genre id: " + genre);

                        map.put(RESTO_GENRE_ID, genre + "");
                        break;
                    case "description":
                        String description = reader.nextString();
                        Log.i(TAG, "Found description: " + description);

                        map.put(RESTO_DESCRIPTION, description);
                        break;
                    case "distance":
                        double distance = reader.nextDouble();
                        Log.i(TAG, "Found distance: " + distance);

                        map.put(RESTO_DISTANCE, distance + "");
                        break;
                    default: // Information we don't need. Should not happen here
                        Log.i(TAG, "The " + name + " was ignored.");
                        reader.skipValue();
                }
            } else {
                Log.i(TAG, "Skipping " + token.name());
                reader.skipValue();
            }
        }

        return map;
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
        String name = map.get(RESTO_NAME);
        String phone = map.get(RESTO_PHONE);
        String email = map.get(RESTO_EMAIL);
        String civicNum = map.get(RESTO_CIVIC_NUM);
        String suite = map.get(RESTO_SUITE);
        String street = map.get(RESTO_STREET);
        String city = map.get(RESTO_CITY);
        String postal = map.get(RESTO_POSTAL);
        String province = map.get(RESTO_PROVINCE);
        String country = map.get(RESTO_COUNTRY);
        String priceRange = map.get(RESTO_PRICE);
        String url = map.get(RESTO_URL);
        String cuisineId = map.get(RESTO_GENRE_ID);
        String userId = map.get(RESTO_USER_ID);
        String latitude = map.get(RESTO_LATITUDE);
        String longitude = map.get(RESTO_LONGITUDE);

        if (id != null) {
            resto.setId(Integer.parseInt(id));
        }
        resto.setName(name);
        if (phone != null) {
            resto.setPhone(Long.parseLong(phone));
        }
        resto.setEmail(email);

        // Get address fields
        Address address = new Address();
        if (civicNum != null) {
//            address.setCivicNum(Integer.parseInt(civicNum));
        }
        if (suite != null) {
            address.setSuite(Integer.parseInt(suite));
        }
//        address.setStreet(street);
        address.setCity(city);
        address.setPostal(postal);
        address.setProvince(province);
        address.setCountry(country);
        if (latitude != null && longitude != null) {
            address.setLatitude(Double.parseDouble(latitude));
            address.setLongitude(Double.parseDouble(longitude));
        }
        resto.setAddress(address);

        resto.setPriceRange(priceRange);
        resto.setLink(url);
        // TODO get genre from id
//        resto.setGenre(cuisines);
        // TODO get submitter from id

        return resto;
    }


    /**
     * Gets the information of a review from the {@code JsonReader}
     *
     * @param reader A {@code JsonReader} with the next object being a review
     * @return A {@code HasMap} of key-value pairs with the keys being fields in the JSON that
     * correspond to fields of a {@code Review}
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private HashMap<String, String> getReview(JsonReader reader) throws IOException {
        HashMap<String, String> map = new HashMap<>();

        // Read all fields in review object
        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (token.name().equals(JsonToken.NAME.toString())) {

                String name = reader.nextName();

                switch (name) {
                    case "id":
                        int id = reader.nextInt();
                        Log.i(TAG, "Found id: " + id);

                        map.put(REVIEW_ID, id + "");
                        break;
                    case "title":
                        String title = reader.nextString();
                        Log.i(TAG, "Found title: " + title);

                        map.put(REVIEW_TITLE, title);
                        break;
                    case "content":
                        String content = reader.nextString();
                        Log.i(TAG, "Found content: " + content);

                        map.put(REVIEW_CONTENT, content);
                        break;
                    case "rating": // Name of the restaurant
                        double rating = reader.nextDouble();
                        Log.i(TAG, "Found rating: " + rating);

                        map.put(REVIEW_RATING, rating + "");
                        break;
                    case "user_id":
                        int user = reader.nextInt();
                        Log.i(TAG, "Found user id: " + user);

                        map.put(REVIEW_USER_ID, user + "");
                        break;
                    case "resto_id":
                        int resto = reader.nextInt();
                        Log.i(TAG, "Found resto id: " + resto);

                        map.put(REVIEW_RESTO_ID, resto + "");
                        break;
                    default: // Information we don't need. Should not happen here
                        Log.i(TAG, "The " + name + " was ignored.");
                        reader.skipValue();
                }
            } else {
                Log.i(TAG, "Skipping " + token.name());
                reader.skipValue();
            }
        }

        return map;
    }

    /**
     * Gets the review information from the map and returns a data bean.
     *
     * @param map The map containing key-value pairs of fields found in a {@code Review} object.
     * @return A {@code Review} object with the information retrieved from the map.
     */
    private Review getReview(HashMap<String, String> map) {
        Review review = new Review();

        String id = map.get(REVIEW_ID);
        String title = map.get(REVIEW_TITLE);
        String content = map.get(REVIEW_CONTENT);
        String rating = map.get(REVIEW_RATING);
        String userId = map.get(REVIEW_USER_ID);
        String restoId = map.get(REVIEW_RESTO_ID);

        if (id != null) {
//            review.setId(Integer.parseInt(id));
        }
        review.setTitle(title);

        return review;
    }
}
