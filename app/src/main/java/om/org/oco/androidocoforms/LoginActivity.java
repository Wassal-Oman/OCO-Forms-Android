package om.org.oco.androidocoforms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class LoginActivity extends AppCompatActivity {

    // widgets
    EditText etEmployeeId;
    EditText etPassword;

    // attributes
    int statusCode;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize
        etEmployeeId = findViewById(R.id.et_employee_number);
        etPassword = findViewById(R.id.et_phone);
    }

    public void signIn(View view) {
        String id = etEmployeeId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(id.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_employee_id, Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_password, Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length() < 6) {
            Toast.makeText(this, R.string.password_is_6_characters_long, Toast.LENGTH_SHORT).show();
            return;
        }

        signInUser(id, password);
    }

    private void signInUser(final String id, final String password) {
        // check for internet
        if(HttpHelper.isOnline(this)) {

            // show progress dialog
            final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.logging_in), getString(R.string.please_wait), false, false);

            // make http request
            StringRequest request = new StringRequest(Request.Method.POST, HttpHelper.LOGIN_ROUTE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    dialog.dismiss();
                    Log.d("RESPONSE", response);

                    if(response.length() > 0) {
                        try {
                            // get json response and parse it
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                            if(status.equals("success")) {
                                // store user data
                                String data = json.getString("data");
                                String token = json.getString("token");

                                storeUserData(data, token);
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }

                        } catch (JSONException e) {
                            Log.d("JSON EXCEPTION", "Cannot read JSON Object");
                        }
                    } else {
                        Log.d("RESPONSE", "No Response");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    Log.d("ERROR", String.valueOf(error));
                    Toast.makeText(LoginActivity.this, R.string.failed_to_connect_to_server, Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("password", password);
                    return params;
                }
            };

            // add request to request queue
            VolleySingleton.getInstance(this).addToRequestQueue(request);

        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    // method to store user data
    private void storeUserData(String data, String token) {
        // store data into a Shared Preferences
        SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user", data);
        editor.putString("token", token);
        editor.apply();
    }

    public void goToForgetPasswordScreen(View view) {
        startActivity(new Intent(this, ForgetPasswordActivity.class));
    }
}
