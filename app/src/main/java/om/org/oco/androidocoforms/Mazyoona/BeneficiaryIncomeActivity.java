package om.org.oco.androidocoforms.Mazyoona;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.shuhart.stepview.StepView;

import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import om.org.oco.androidocoforms.R;

public class BeneficiaryIncomeActivity extends AppCompatActivity {

    // widgets
    private StepView stepView;
    private Toolbar toolbar;
    private Spinner spIncomeType;

    // attributes
    private String[] incomeTypes;
    private ArrayAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_income);

        // set layout configuration
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("ar"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // initialize
        stepView = findViewById(R.id.step_view);
        toolbar = findViewById(R.id.toolbar);
        spIncomeType = findViewById(R.id.sp_income_type);

        // set default toolbar
        setSupportActionBar(toolbar);

        // check if toolbar is available
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.income_data);
        }

        // configure StepView
        stepView.getState()
                .stepsNumber(5)
                .stepLineWidth(5)
                .textSize(getResources().getDimensionPixelSize(R.dimen.step_view))
                .stepNumberTextSize(getResources().getDimensionPixelSize(R.dimen.step_view))
                .commit();

        // load income types
        incomeTypes = getResources().getStringArray(R.array.income_type);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, incomeTypes);

        // set spinner adapter
        spIncomeType.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        stepView.go(1, false);
    }

    public void goToNextPage(View view) {
        startActivity(new Intent(this, BeneficiaryAccommodationActivity.class));
        finish();
    }

    public void enterIncomeData(View view) {

    }
}
