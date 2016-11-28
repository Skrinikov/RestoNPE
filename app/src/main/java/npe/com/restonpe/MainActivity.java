package npe.com.restonpe;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import npe.com.restonpe.services.RestoLocationManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request permission
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, RestoLocationManager.LOCATION_MANAGER_REQUEST_CODE );
    }

    public void buttonClick(View v) {
        Intent intent = new Intent(this, NearRestosActivity.class);
        startActivity(intent);
    }
}
