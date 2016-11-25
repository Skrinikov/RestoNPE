package npe.com.restonpe;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import npe.com.restonpe.Fragments.IndexFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFragments();
    }

    private void createFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        IndexFragment fragment = new IndexFragment();
        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
