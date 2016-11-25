package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import npe.com.restonpe.Fragments.AboutFragment;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFragments();
    }

    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AboutFragment fragment = new AboutFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
