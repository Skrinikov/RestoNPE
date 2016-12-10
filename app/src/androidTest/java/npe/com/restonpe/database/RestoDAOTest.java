package npe.com.restonpe.database;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import npe.com.restonpe.Beans.Address;
import npe.com.restonpe.Beans.Resto;
import npe.com.restonpe.Beans.RestoItem;
import npe.com.restonpe.Beans.Review;

import static org.junit.Assert.*;

/**
 * Created by Danieil on 2016-11-26.
 */
@RunWith(AndroidJUnit4.class)
public class RestoDAOTest {

    // Testing variables
    private RestoDAO db;
    private static String TAG = "RestoDAO Test";

    /**
     * Sets ups the testing
     */
    @Before
    public void setUp(){
        Context c = InstrumentationRegistry.getTargetContext();

        db = RestoDAO.getDatabase(c);
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("npe.com.restonpe", appContext.getPackageName());
    }

    @Test
    public void addRestoTest() throws Exception{
        Resto r = new Resto();
        r.setEmail("email");
        r.setGenre("genre");
        r.setLink("link");
        r.setName("name");
        r.setPhone(123);
        r.setPriceRange("pricerange");
        r.setSubmitterEmail("submitter");
        r.setSubmitterName("submitter name");
        Address a = new Address();
        a.setCity("city");
        a.setCountry("country");
        a.setLatitude(1);
        a.setLongitude(1);
        a.setPostal("h4h4h4");
        a.setSuite("0");

        r.setAddress(a);

        db.addRestaurant(r);

        //Did not crash this far.
        assertTrue(true);
    }

    @Test
    public void addRestoTest2() throws Exception{
        Resto r = new Resto();
        r.setEmail("email");
        r.setGenre("genre");
        r.setLink("link");
        r.setName("name");
        r.setPhone(123);
        r.setPriceRange("pricerange");
        r.setSubmitterEmail("submitter");
        r.setSubmitterName("submitter name");

        Address a = new Address();
        a.setCity("city");
        a.setCountry("country");
        a.setLatitude(1);
        a.setLongitude(1);
        a.setPostal("h4h4h4");
        a.setAddress("3040 Sherbrooke");
        a.setSuite("0");

        r.setAddress(a);

        Review rev = new Review();
        rev.setContent("Worst Restaurant Ever will not recommend");
        rev.setLikes(873);
        rev.setRating(5.0);
        rev.setSubmitter("Bob");
        rev.setSubmitterEmail("Bob@me.com");
        rev.setTitle("I PUKED !!!!!");

        List<Review> revList = new ArrayList<>();
        revList.add(rev);
        r.setReviews(revList);

        db.addRestaurant(r);

        //Did not crash this far.
        assertTrue(true);
    }

    @Test
    public void getRestosSmallTest() throws Exception{
        Resto r = createNewResto();
        db.addRestaurant(r);

        List<RestoItem> result = db.getAllRestaurantsSmall();

        assertTrue(validateResult(r,result));
    }

    @Test
    public void getSingleRestoTest() throws Exception{
        Resto r = createNewResto();

        long restoId = db.addRestaurant(r);

        Resto result = db.getSingleRestaurant(restoId);
        Log.d(TAG,result.toString());
    }

    @Test
    public void deleteTest() throws Exception{
        Resto r = createNewResto();

        long id = db.addRestaurant(r);

        int result = db.deleteRestaurant(id);

        Log.d(TAG, "Result is: "+result);
        assertEquals(result,1);
    }

    private boolean validateResult(Resto r, List<RestoItem> result) {
        RestoItem item = result.get(3);
        Log.d(TAG,r.getName()+"-"+item.getName());
        Log.d(TAG, "Size: "+result.size());

        return true;
    }


    private Resto createNewResto() {
        Resto r = new Resto();

        r.setGenre("French 3");
        r.setName("Baguette Fromage");
        r.setPriceRange("323.99-873.32");
        r.setSubmitterEmail("baguette");
        r.setSubmitterName("French Dude 107");
        r.setPhone(5142244918L);
        r.setLink("TestTest");
        r.setEmail("Hello");

        Address addr = new Address();
        addr.setCity("Paris");
        addr.setLongitude(23.54);
        addr.setLatitude(45.98);
        addr.setPostal("h0h0h0");
        addr.setCountry("France");
        addr.setAddress("3040 Sherbrooke");
        addr.setSuite("2");
        r.setAddress(addr);


        Review rev = new Review();
        rev.setRating(5.0);
        rev.setContent("Best");
        rev.setLikes(2);
        rev.setSubmitter("Me");
        rev.setSubmitterEmail("me@mew");
        rev.setTitle("MUAHAHA");
        List<Review> revL = new ArrayList<>();
        revL.add(rev);
        r.setReviews(revL);

        return r;
    }
}
