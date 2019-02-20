package om.org.oco.androidocoforms;

import android.app.ProgressDialog;
import android.content.Context;
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

public class ForgetPasswordActivity extends AppCompatActivity {

    // widgets
    EditText etEmployeeNumber;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // initialize
        etEmployeeNumber = findViewById(R.id.et_employee_number);
    }

    public void send(View view) {
        // get user input
        String employeeNumber = etEmployeeNumber.getText().toString().trim();

        if(employeeNumber.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_employee_id, Toast.LENGTH_SHORT).show();
            return;
        }

        sendResetPasswordRequest(employeeNumber);
    }

    // method to send reset password request through email
    private void sendResetPasswordRequest(final String id) {
        // check for internet
        if(HttpHelper.isOnline(this)) {

            // show progress dialog
            final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.request_is_being_sent), getString(R.string.please_wait), false, false);

            // make http request
            StringRequest request = new StringRequest(Request.Method.POST, HttpHelper.RESET_PASSWORD_ROUTE, new Response.Listener<String>() {

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

                            Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();

                            if(status.equals("success")) {
                                finish();
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
                    Toast.makeText(ForgetPasswordActivity.this, R.string.failed_to_connect_to_server, Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    return params;
                }
            };

            // add request to request queue
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View view) {
        finish();
    }
}
