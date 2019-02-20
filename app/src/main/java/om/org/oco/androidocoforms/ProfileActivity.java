package om.org.oco.androidocoforms;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    // widgets
    EditText etEmployeeNumber;
    EditText etName;
    EditText etEmail;
    EditText etPhone;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // set layout configuration
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("ar"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // initialize
        etEmployeeNumber = findViewById(R.id.et_employee_number);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);

        // get user data
        SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
        String data = sp.getString("user", "");

        // check of there is any available data
        if(!data.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(data);

                // set drawer views
                etEmployeeNumber.setText(jsonObject.getString("id"));
                etName.setText(jsonObject.getString("name"));
                etEmail.setText(jsonObject.getString("email"));
                etPhone.setText(jsonObject.getString("phone"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void back(View view) {
        finish();
    }
}
