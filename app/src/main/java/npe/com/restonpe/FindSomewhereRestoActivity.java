package npe.com.restonpe;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import npe.com.restonpe.Fragments.FavRestoFragment;
import npe.com.restonpe.Fragments.FindSomewhereRestoFragment;

public class FindSomewhereRestoActivity extends BaseActivity {
    private FindSomewhereRestoFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_activity_find_from_somewhere);
        }

        fragment = new FindSomewhereRestoFragment();
        super.createFragments(fragment);
    }
}
