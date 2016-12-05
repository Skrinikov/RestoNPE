package npe.com.restonpe.util;

/**
 * This class provides basic methods to perform distance calculations.
 *
 * @author Danieil Skrinikov
 * @version 1.0.0
 * @since 11/28/2016
 */
public class DistanceCalculator {

    public static final int EARTH_RADIUS = 6371; //In km

    /**
     * Make the class not instantiable.
     */
    private DistanceCalculator() {
    }

    /**
     * Calculates the distance between two latitude-longitude points.
     * <p>
     * Used the Haversine formula to find the distance.
     * It can be found here: http://www.movable-type.co.uk/scripts/latlong.html
     *
     * @param lat1  Latitude of the first point.
     * @param long1 Longitude of the first point.
     * @param lat2  Latitude of the second point.
     * @param long2 Longitude of the second point.
     * @return Distance between the two location in km.
     */
    public static double calculateDistance(Double lat1, Double long1, Double lat2, Double long2) {

        // I used the formula from http://andrew.hedges.name/experiments/haversine/
        double lat1r = Math.abs(lat1 * 2 * Math.PI / 360);
        double lat2r = Math.abs(lat2 * 2 * Math.PI / 360);
        double long1r = Math.abs(long1 * 2 * Math.PI / 360);
        double long2r = Math.abs(long1 * 2 * Math.PI / 360);
        double dlat = Math.abs(lat1 - lat2) * 2 * Math.PI / 360;
        double dlong = Math.abs(long1 - long2) * 2 * Math.PI / 360;

        //a = (sin(dlat/2))^2 + cos(lat2) * cos(lat1) * (sin(dlon/2))^2
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1r) * Math.cos(lat2r) * Math.pow(Math.sin(dlong / 2), 2);
        //c = 2 * atan2( sqrt(a), sqrt(1-a) )
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        //d = R * c
        return Math.round(EARTH_RADIUS * c * 10) / 10.0;
    }//calculateDistance
}
