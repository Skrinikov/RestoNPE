package npe.com.restonpe.Zomato;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.RestoItem;
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
    public void findRestosNear(double latitude, double longitude) {
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

    public void findRestoInformation(int id) {
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

    /**
     * Reads the given JSON text and returns a List of Restaurant objects
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

    // TODO Not yet tested
    /**
     * Finds the restaurant with the given id's information
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

        // Begin root object
        reader.beginObject();

        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (token.name().equals(JsonToken.NAME.toString())) {
                Resto resto = new Resto();

                String name = reader.nextName();
                switch (name) {
                    case "id":
                        int id = reader.nextInt();
                        Log.i(TAG, "Found id " + id);

                        break;
                    case "name":
                        String restoName = reader.nextString();
                        Log.i(TAG, "Found name " + restoName);

                        break;
                    case "url":
                        String url = reader.nextString();
                        Log.i(TAG, "Found URL " + url);

                        break;
                    case "location":
                        reader.skipValue();
                        break;
                    case "price_range":
                        int priceRange = reader.nextInt();
                        Log.i(TAG, "Found price range" + priceRange);

                        break;
                    case "currency":
                        String currency = reader.nextString();
                        Log.i(TAG, "Found currency" + currency);

                        break;
                    case "user_rating":
                        reader.skipValue();
                        break;
                    case "cuisines":
                        String cuisines = reader.nextString();
                        Log.i(TAG, "Found cuisines" + cuisines);

                        break;
                    case "phone_numbers":
                        String phone = reader.nextString();
                        Log.i(TAG, "Found phone number" + phone);

                        break;
                    default:
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
                        handleRestoItemLocation(reader, restoItem);
                        break;
                    case "user_rating": // The restaurant's ratings
                        handleRestoItemRating(reader, restoItem);
                        break;
                    default:
                        Log.w(TAG, "The " + name + " was ignored.");
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
     * @param restoItem The {@code RestoItem} to which to add the location information
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private void handleRestoItemLocation(JsonReader reader, RestoItem restoItem) throws IOException {
        reader.beginObject();

        for (int i = 0; i < 8; i++) {
            String name = reader.nextName();

            switch (name) {
                case "address":
                    String address = reader.nextString();
                    Log.i(TAG, "Found address: " + address);

                    restoItem.setAddress(address);
                    break;
                case "city":
                    String city = reader.nextString();
                    Log.i(TAG, "Found city: " + city);

                    restoItem.setCity(city);
                    break;
                case "latitude":
                    double latitude = reader.nextDouble();
                    Log.i(TAG, "Found latitude: " + latitude);

                    restoItem.setLatitude(latitude);
                    break;
                case "longitude":
                    double longitude = reader.nextDouble();
                    Log.i(TAG, "Found longitude: " + longitude);

                    restoItem.setLongitude(longitude);
                    break;
                default:
                    Log.i(TAG, "Skipping " + name);
                    reader.skipValue();
            }
        }
        reader.endObject();
    }

    /**
     * Gets the rating information of the restaurant
     *
     * @param reader    A {@code JsonReader} with the next object being a restaurant
     * @param restoItem The {@code RestoItem} to which to add the rating information
     * @throws IOException If an IOException occurs while reading the JSON
     */
    private void handleRestoItemRating(JsonReader reader, RestoItem restoItem) throws IOException {
        reader.beginObject();

        for (int i = 0; i < 4; i++) {
            String name = reader.nextName();

            switch (name) {
                case "aggregate_rating":
                    double rating = reader.nextDouble();
                    Log.i(TAG, "Found rating: " + rating);

                    restoItem.setRating(rating);
                    break;
                default:
                    Log.i(TAG, "Skipping " + name);
                    reader.skipValue();
            }
        }
        reader.endObject();
    }
}
