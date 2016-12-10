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
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.Beans.Review;

/**
 * Manages calls to the Heroku API.
 *
 * @author Jeegna Patel
 * @version 1.0
 * @since 06/12/2016
 */
public class HerokuRestos {

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
    private static final String RESTO_GENRE = "cuisine_id";
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

    public HerokuRestos (Context context) {
        this.mContext = context;
    }

    public List<RestoItem> readRestoJson(JsonReader reader) throws IOException {
        List<RestoItem> list = new ArrayList<>();

        // Start reading the text, by beginning the root object.
        reader.beginArray();
        while (reader.hasNext()) {
            // Begin restaurant object
            reader.beginObject();

            HashMap<String, String> map = getRestoMap(reader);
            list.add(getRestoItem(map));

            // End restaurant object
            reader.endObject();
        }

        // End root object
        reader.endArray();
        reader.close();

        return list;
    }

    /**
     * Reads the given JSON text of restaurants and returns a List of Resto objects
     *
     * @param reader A {@code JsonReader} with the next object being the root
     * @return A list of {@code RestoItem}s. May return an empty list if there are no results found.
     * @throws IOException If an IOException occurs with the JSON text
     */
    public List<Resto> readRestoInformation(JsonReader reader) throws IOException {
        List<Resto> list = new ArrayList<>();

        // Start reading the text, by beginning the root object.
        reader.beginObject();

        HashMap<String, String> map = getRestoMap(reader);
        list.add(getResto(map));

        // End root object
        reader.endObject();
        reader.close();

        return list;
    }

    /**
     * Gets the restaurants information from the map and returns a data bean.
     *
     * @param map The map containing key-value pairs of fields found in a {@code Resto} object.
     * @return A {@code RestoItem} object with the information retrieved from the map.
     */
    private RestoItem getRestoItem(HashMap<String, String> map) {
        RestoItem restoItem = new RestoItem();

        String id = map.get(RESTO_ID);
        String name = map.get(RESTO_NAME);
        String phone = map.get(RESTO_PHONE);
        String civicNum = map.get(RESTO_CIVIC_NUM);
        String suite = map.get(RESTO_SUITE);
        String street = map.get(RESTO_STREET);
        String city = map.get(RESTO_CITY);
        String postal = map.get(RESTO_POSTAL);
        String province = map.get(RESTO_PROVINCE);
        String country = map.get(RESTO_COUNTRY);
        String priceRange = map.get(RESTO_PRICE);

        if (id != null) {
            restoItem.setHerokuId(Integer.parseInt(id));
        }
        restoItem.setName(name);
        if (phone != null) {
            restoItem.setPhone(Long.parseLong(phone));
        }

        // Address format string
        // civic number suite Street Name, City, Province, Country, postal code
        String address = "$s $s $s, $s, $s, $s, $s";
        // Add address fields to string
        address = String.format(address, civicNum);
        address = String.format(address, suite);
        address = String.format(address, street, city, province, country, postal);
        restoItem.setAddress(address);

        restoItem.setPriceRange(priceRange);

        return restoItem;
    }

    /**
     * Gets the restaurants information from the map and returns a data bean. Does not get the
     * Resto's reviews.
     *
     * @param map The map containing key-value pairs of fields found in a {@code Resto} object.
     * @return A {@code Resto} object with the information retrieved from the map.
     */
    private Resto getResto(HashMap<String, String> map) {
        final Resto resto = new Resto();

        String id = map.get(RESTO_ID);
        String name = map.get(RESTO_NAME);
        String phone = map.get(RESTO_PHONE);
        String civicNum = map.get(RESTO_CIVIC_NUM);
        String suite = map.get(RESTO_SUITE);
        String street = map.get(RESTO_STREET);
        String city = map.get(RESTO_CITY);
        String postal = map.get(RESTO_POSTAL);
        String province = map.get(RESTO_PROVINCE);
        String country = map.get(RESTO_COUNTRY);
        String genre = map.get(RESTO_GENRE);
        String priceRange = map.get(RESTO_PRICE);
        String email = map.get(RESTO_EMAIL);
        String latitude = map.get(RESTO_LATITUDE);
        String longitude = map.get(RESTO_LONGITUDE);
        String link = map.get(RESTO_URL);

        if (id != null) {
            resto.setHerokuId(Integer.parseInt(id));
        }
        resto.setName(name);
        if (phone != null) {
            resto.setPhone(Long.parseLong(phone));
        }
        resto.setPriceRange(priceRange);
        resto.setEmail(email);
        resto.setLink(link);
        resto.setGenre(genre);

        // Address format string
        // civic number suite Street Name
        Address address = new Address();
        String addressString = "";
        // Add address fields to string
        if (civicNum != null) {
            addressString += Integer.parseInt(civicNum) + " ";
        }
        addressString += suite + " ";
        addressString += street + ", " + city + ", " + province + " " + postal + "\n" + country;

        address.setAddress(addressString);
        address.setPostal(postal);
        address.setCity(city);
        address.setProvince(province);
        address.setCountry(country);
        if (suite != null) {
            address.setSuite(Integer.parseInt(suite));
        }
        if (latitude != null) {
            address.setLatitude(Double.parseDouble(latitude));
        }
        if (longitude != null) {
            address.setLongitude(Double.parseDouble(longitude));
        }
        resto.setAddress(address);

        return resto;
    }

    /**
     * Gets the information of a restaurant from the {@code JsonReader}
     *
     * @param reader A {@code JsonReader} with the next object being a restaurant
     * @return A {@code HashMap} of key-value pairs with the keys being fields in the JSON that
     * correspond to fields of a {@code Review}
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private HashMap<String, String> getRestoMap(JsonReader reader) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        JsonToken jsonToken;

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
                            String id = reader.nextString();
                            Log.i(TAG, "Found id: " + id);

                            map.put(RESTO_ID, id);
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
                            String civicNum = reader.nextString();
                            Log.i(TAG, "Found civic number: " + civicNum);

                            map.put(RESTO_CIVIC_NUM, civicNum);
                            break;
                        case "street":
                            String street = reader.nextString();
                            Log.i(TAG, "Found street: " + street);

                            map.put(RESTO_STREET, street);
                            break;
                        case "suite":
                            String suite = reader.nextString();
                            Log.i(TAG, "Found suite: " + suite);

                            map.put(RESTO_SUITE, suite);
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
                            String latitude = reader.nextString();
                            Log.i(TAG, "Found latitude: " + latitude);

                            map.put(RESTO_LATITUDE, latitude);
                            break;
                        case "longitude":
                            String longitude = reader.nextString();
                            Log.i(TAG, "Found longitude: " + longitude);

                            map.put(RESTO_LONGITUDE, longitude);
                            break;
                        case "price":
                            String price = reader.nextString();
                            Log.i(TAG, "Found price: " + price);

                            map.put(RESTO_PRICE, price);
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
                            String user = reader.nextString();
                            Log.i(TAG, "Found user id: " + user);

                            map.put(RESTO_USER_ID, user);
                            break;
                        case "genre":
                            String genre = reader.nextString();
                            Log.i(TAG, "Found genre: " + genre);

                            map.put(RESTO_GENRE, genre);
                            break;
                        case "description":
                            String description = reader.nextString();
                            Log.i(TAG, "Found description: " + description);

                            map.put(RESTO_DESCRIPTION, description);
                            break;
                        case "distance":
                            String distance = reader.nextString();
                            Log.i(TAG, "Found distance: " + distance);

                            map.put(RESTO_DISTANCE, distance);
                            break;
                        default: // Information we don't need. Should not happen here
                            Log.i(TAG, "The " + name + " was ignored.");
                            reader.skipValue();
                    }
                }
            } else {
                Log.i(TAG, "Skipping " + jsonToken.name());
                reader.skipValue();
            }
        }
        return map;
    }

    /**
     * Reads the given JSON text of review and returns a List of Review objects
     *
     * @param reader A {@code JsonReader} with the next object being the root
     * @return A list of {@code Review}s. May return an empty list if there are no results found.
     * @throws IOException If an IOException occurs with the JSON text
     */
    public List<Review> readReviewJson(JsonReader reader) throws IOException {
        List<Review> list = new ArrayList<>();

        // Start reading the text, by beginning the root object.
        reader.beginArray();
        while (reader.hasNext()) {
            // Begin restaurant object
            reader.beginObject();

            HashMap<String, String> map = getReviewMap(reader);
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
            review.setId(Integer.parseInt(id));
        }
        review.setTitle(title);
        review.setContent(content);
        if (rating != null) {
            review.setRating(Double.parseDouble(rating));
        }
        if (restoId != null) {
            review.setRestoId(Long.parseLong(restoId));
        }

        return review;
    }

    /**
     * Gets the information of a review from the {@code JsonReader}
     *
     * @param reader A {@code JsonReader} with the next object being a review
     * @return A {@code HasMap} of key-value pairs with the keys being fields in the JSON that
     * correspond to fields of a {@code Review}
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private HashMap<String, String> getReviewMap(JsonReader reader) throws IOException {
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
}
