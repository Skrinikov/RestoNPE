package npe.com.restonpe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Creates an instance of the FindRestos Activity. This {@code Activity} will allow the user to
 * find restaurants from the Zomato API.
 *
 * @author Jeegna Patel
 * @since 21/11/2016
 * @version 1.0
 */
public class FindRestosActivity extends AppCompatActivity {

    /**
     * Creates the {@code Activity}.
     *
     * @param savedInstanceState The {@code Bundle} from which to retrieve the saved values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_restos);
    }
}
