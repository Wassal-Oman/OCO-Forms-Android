package om.org.oco.androidocoforms;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class HttpHelper {

    // routes
    public static final String LOGIN_ROUTE = "http://10.0.2.2:3000/api/login";
    public static final String RESET_PASSWORD_ROUTE = "http://10.0.2.2:3000/api/reset-password";
    public static final String CATEGORY_ROUTE = "http://10.0.2.2:3000/api/categories";
    public static final String FILE_NUMBER_SEARCH_ROUTE = "http://10.0.2.2:3000/api/beneficiary";
    public static final String ADD_BENEFICIARY_ROUTE = "http://10.0.2.2:3000/api/beneficiary/add";

    // method to check internet state
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // method to get access token
    public static String getAccessToken(Context context) {
        // get token
        SharedPreferences sp = context.getSharedPreferences("USER", MODE_PRIVATE);
        return sp.getString("token", "");
    }
}
