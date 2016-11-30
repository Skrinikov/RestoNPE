package npe.com.restonpe.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RestoNetworkManager {

    private static final String TAG = RestoNetworkManager.class.getSimpleName();

    private final Context mContext;

    public RestoNetworkManager(Context context) {
        this.mContext = context;
    }

    public boolean netIsUp() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
