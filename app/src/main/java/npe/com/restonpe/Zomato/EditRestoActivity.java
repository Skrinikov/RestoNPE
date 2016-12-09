package npe.com.restonpe.Zomato;

import android.support.v7.app.ActionBar;
import android.os.Bundle;

import npe.com.restonpe.AddRestoActivity;
import npe.com.restonpe.R;

public class EditRestoActivity extends AddRestoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_edit_resto);
        }
    }
}
