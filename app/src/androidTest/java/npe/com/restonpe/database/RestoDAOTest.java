package npe.com.restonpe.database;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by Danieil on 2016-11-26.
 */
@RunWith(AndroidJUnit4.class)
public class RestoDAOTest {

    // Testing variables
    private RestoDAO db;

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
}