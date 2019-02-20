package om.org.oco.androidocoforms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import om.org.oco.androidocoforms.Mazyoona.BeneficiaryAddActivity;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // widgets
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView drawerName;
    TextView drawerEmail;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // set layout configuration
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("ar"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // initialize
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav);

        // set default toolbar
        setSupportActionBar(toolbar);

        // check if toolbar is available
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        // configure drawer
        View headerView = navigationView.getHeaderView(0);
        drawerName = headerView.findViewById(R.id.tv_drawer_name);
        drawerEmail = headerView.findViewById(R.id.tv_drawer_email);
        navigationView.setNavigationItemSelectedListener(this);

        // get user data
        SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
        String data = sp.getString("user", "");

        // check of there is any available data
        if(!data.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(data);

                // set drawer views
                drawerName.setText(jsonObject.getString("name"));
                drawerEmail.setText(jsonObject.getString("email"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_logout) {
            // remove user
            SharedPreferences sp = getSharedPreferences("USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("user");
            editor.apply();

            // go to login page
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToMazyoonaForm(View view) {
        startActivity(new Intent(this, BeneficiaryAddActivity.class));
    }

    public void goToDisastersForm(View view) {

    }

    public void goToPublicForm(View view) {

    }

    public void goToRescueForm(View view) {

    }
}
