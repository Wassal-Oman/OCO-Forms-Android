package om.org.oco.androidocoforms.Mazyoona;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shuhart.stepview.StepView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import om.org.oco.androidocoforms.HttpHelper;
import om.org.oco.androidocoforms.R;
import om.org.oco.androidocoforms.VolleySingleton;
import om.org.oco.androidocoforms.models.Beneficiary;
import om.org.oco.androidocoforms.models.Category;

public class BeneficiaryAddActivity extends AppCompatActivity {

    // widgets
    private StepView stepView;
    private Toolbar toolbar;
    private Spinner spRegions;
    private Spinner spSocialStatus;
    private EditText etFileNumber, etBeneficiaryName, etBeneficiaryDOB, etBeneficiaryNationality, etBeneficiaryPhone;

    // attributes
    private String[] regions;
    private List<Category> social_statuses;
    private ArrayAdapter adapter;
    private Beneficiary beneficiary;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_add);

        // set layout configuration
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("ar"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // initialize
        stepView = findViewById(R.id.step_view);
        toolbar = findViewById(R.id.toolbar);
        etFileNumber = findViewById(R.id.et_file_id);
        etBeneficiaryName = findViewById(R.id.et_beneficiary_name);
        etBeneficiaryDOB = findViewById(R.id.et_beneficiary_dob);
        etBeneficiaryNationality = findViewById(R.id.et_beneficiary_nationality);
        etBeneficiaryPhone = findViewById(R.id.et_beneficiary_phone);
        spRegions = findViewById(R.id.sp_regions);
        spSocialStatus = findViewById(R.id.sp_social_status);
        social_statuses = new ArrayList<>();

        // set default toolbar
        setSupportActionBar(toolbar);

        // check if toolbar is available
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow);
            getSupportActionBar().setTitle(R.string.beneficiary_data);
        }

        // configure StepView
        stepView.getState()
                .stepsNumber(5)
                .stepLineWidth(5)
                .textSize(getResources().getDimensionPixelSize(R.dimen.step_view))
                .stepNumberTextSize(getResources().getDimensionPixelSize(R.dimen.step_view))
                .commit();

        // get regions
        regions = getResources().getStringArray(R.array.regions);

        // feed region spinner
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, regions);
        spRegions.setAdapter(adapter);

        // load social status from database
        loadSocialStatuses();

    }

    // method to load social statuses
    private void loadSocialStatuses() {
        // check for internet connection
        if(HttpHelper.isOnline(this)) {

            // show progress dialog
            final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.load_data), getString(R.string.please_wait), false, false);

            // create hhtp request
            StringRequest request = new StringRequest(Request.Method.GET, HttpHelper.CATEGORY_ROUTE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    dialog.dismiss();
                    Log.d("RESPONSE", response);

                    if(response.length() > 0) {
                        // get json response and parse it
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");
                            String data = json.getString("data");

                            if(status.equals("success")) {
                                // view data in spinner
                                viewData(data);
                            } else {
                                Toast.makeText(BeneficiaryAddActivity.this, getString(R.string.cannot_load_social_statuses), Toast.LENGTH_SHORT).show();
                            }

                            Toast.makeText(BeneficiaryAddActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(BeneficiaryAddActivity.this, getString(R.string.no_data), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            // add request to request queue
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    // method to view drop down list
    private void viewData(String data) {
        try {
            JSONArray array = new JSONArray(data);

            for(int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);

                Category category = new Category();
                category.setId(object.getInt("id"));
                category.setName(object.getString("name"));

                social_statuses.add(category);
            }

            // view spinner with social statuses
            ArrayAdapter adapter = new ArrayAdapter(BeneficiaryAddActivity.this, android.R.layout.simple_list_item_1, social_statuses);
            spSocialStatus.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // method to collect user input data
    public void goToNextPage(View view) {
        // get user inputs
        String fileNumber = etFileNumber.getText().toString().trim();
        String beneficiaryName = etBeneficiaryName.getText().toString().trim();
        String beneficiaryDOB = etBeneficiaryDOB.getText().toString().trim();
        String beneficiaryNationality = etBeneficiaryNationality.getText().toString().trim();
        String beneficiaryPhone = etBeneficiaryPhone.getText().toString().trim();

        // validation
        if(fileNumber.isEmpty()) {
            Toast.makeText(this, "الرجاء ادخال رقم الملف", Toast.LENGTH_SHORT).show();
            return;
        }

        if(beneficiaryName.isEmpty()) {
            Toast.makeText(this, "الرجاء ادخال اسم المستفيد", Toast.LENGTH_SHORT).show();
            return;
        }

        if(beneficiaryDOB.isEmpty()) {
            Toast.makeText(this, "الرجاء ادخال تاريخ الميلاد", Toast.LENGTH_SHORT).show();
            return;
        }

        if(beneficiaryNationality.isEmpty()) {
            Toast.makeText(this, "الرجاء ادخال الجنسية", Toast.LENGTH_SHORT).show();
            return;
        }

        if(beneficiaryPhone.isEmpty()) {
            Toast.makeText(this, "الرجاء ادخال رقم الهاتف", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(BeneficiaryAddActivity.this, BeneficiaryIncomeActivity.class);
        intent.putExtra("file_number", fileNumber);
        startActivity(intent);
    }

    public void createBeneficiary(View view) {
        // get user inputs
        String fileNumber = etFileNumber.getText().toString().trim();
        String beneficiaryName = etBeneficiaryName.getText().toString().trim();
        String beneficiaryDOB = etBeneficiaryDOB.getText().toString().trim();
        String beneficiaryNationality = etBeneficiaryNationality.getText().toString().trim();
        String beneficiaryPhone = etBeneficiaryPhone.getText().toString().trim();
        Category category = (Category) spSocialStatus.getSelectedItem();
        int socialStatus = category.getId();
        String address = spRegions.getSelectedItem().toString();

        // validation
        if(fileNumber.isEmpty()) {
            Toast.makeText(this, "الرجاء ادخال رقم الملف", Toast.LENGTH_SHORT).show();
            return;
        }

        if(beneficiaryName.isEmpty()) {
            Toast.makeText(this, "الرجاء ادخال اسم المستفيد", Toast.LENGTH_SHORT).show();
            return;
        }

        if(beneficiaryDOB.isEmpty()) {
            Toast.makeText(this, "الرجاء ادخال تاريخ الميلاد", Toast.LENGTH_SHORT).show();
            return;
        }

        if(beneficiaryNationality.isEmpty()) {
            Toast.makeText(this, "الرجاء ادخال الجنسية", Toast.LENGTH_SHORT).show();
            return;
        }

        if(beneficiaryPhone.isEmpty()) {
            Toast.makeText(this, "الرجاء ادخال رقم الهاتف", Toast.LENGTH_SHORT).show();
            return;
        }

        // create new beneficiary
        createNewBeneficiary(fileNumber, beneficiaryName, beneficiaryDOB, beneficiaryNationality, beneficiaryPhone, socialStatus, address);
    }

    // method to create beneficiary
    private void createNewBeneficiary(final String fileNumber, final String beneficiaryName, final String beneficiaryDOB, final String beneficiaryNationality, final String beneficiaryPhone, final int socialStatus, final String address) {

        if (HttpHelper.isOnline(this)) {
            // get token
            final String accessToken = HttpHelper.getAccessToken(this);

            // dialog
            final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.request_being_processed), getString(R.string.please_wait), false);

            StringRequest request = new StringRequest(Request.Method.POST, HttpHelper.ADD_BENEFICIARY_ROUTE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Log.d("RESPONSE", response);

                    if (response.length() > 0) {
                        try {
                            // get json response and parse it
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            Toast.makeText(BeneficiaryAddActivity.this, message, Toast.LENGTH_LONG).show();

                            if (status.equals("success")) {
                                viewAlertDialog(message, "حسنا");
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
                    Toast.makeText(BeneficiaryAddActivity.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Authorization", "Bearer " + accessToken);
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("file_number", fileNumber);
                    params.put("name", beneficiaryName);
                    params.put("dob", beneficiaryDOB);
                    params.put("nationality", beneficiaryNationality);
                    params.put("phone", beneficiaryPhone);
                    params.put("category", String.valueOf(socialStatus));
                    params.put("address", address);
                    return params;
                }
            };

            // send request
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        } else{
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    // method to search for beneficiary
    public void searchForFileNumber(View view) {
        // get user input
        final String fileNumber = etFileNumber.getText().toString().trim();

        // validation
        if(fileNumber.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_file_number), Toast.LENGTH_SHORT).show();
            return;
        }

        // check for internet connection
        if(HttpHelper.isOnline(this)) {
            // get token
            final String accessToken = HttpHelper.getAccessToken(this);
            Log.d("TOKEN", accessToken);

            // dialog
            final ProgressDialog dialog = ProgressDialog.show(this, getString(R.string.data_is_being_fetched), getString(R.string.please_wait), false);

            // http request
            StringRequest request = new StringRequest(Request.Method.POST, HttpHelper.FILE_NUMBER_SEARCH_ROUTE, new Response.Listener<String>() {
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

                            Toast.makeText(BeneficiaryAddActivity.this, message, Toast.LENGTH_LONG).show();

                            if(status.equals("success")) {
                                // store user data
                                String data = json.getString("data");
                                storeBeneficiaryData(data);
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
                    Toast.makeText(BeneficiaryAddActivity.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Authorization", "Bearer " + accessToken);
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("file_number", fileNumber);
                    return params;
                }
            };

            // add request to request queue
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    // method to retrieve all beneficiary data
    private void storeBeneficiaryData(String data) {
        try {
            JSONObject json = new JSONObject(data);
            beneficiary = new Beneficiary();

            beneficiary.setId(json.getInt("id"));
            beneficiary.setFileNumber(json.getInt("file_id"));
            beneficiary.setName(json.getString("name"));
            beneficiary.setDate_of_birth(json.getString("date_of_birth").split("T")[0]);
            beneficiary.setNationality(json.getString("nationality"));
            beneficiary.setMobile(json.getString("mobile"));
            beneficiary.setAddress(json.getString("address"));
            beneficiary.setCategoryId(json.getInt("categoryId"));
            beneficiary.setCreatedAt(json.getString("createdAt"));
            beneficiary.setUpdatedAt(json.getString("updatedAt"));

            // set all fields
            etBeneficiaryName.setText(beneficiary.getName());
            etBeneficiaryDOB.setText(beneficiary.getDate_of_birth());
            etBeneficiaryNationality.setText(beneficiary.getNationality());
            etBeneficiaryPhone.setText(beneficiary.getMobile());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // method to view alert
    public void viewAlertDialog(String message, String okButton) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(okButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}