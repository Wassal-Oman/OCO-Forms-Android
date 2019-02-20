package om.org.oco.androidocoforms.Mazyoona;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.shuhart.stepview.StepView;

import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import om.org.oco.androidocoforms.R;

public class BeneficiaryBankActivity extends AppCompatActivity {

    // widgets
    private StepView stepView;
    private Toolbar toolbar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_bank);

        // set layout configuration
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("ar"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // initialize
        stepView = findViewById(R.id.step_view);
        toolbar = findViewById(R.id.toolbar);

        // set default toolbar
        setSupportActionBar(toolbar);

        // check if toolbar is available
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.bank_data);
        }

        // configure StepView
        stepView.getState()
                .stepsNumber(5)
                .stepLineWidth(5)
                .textSize(getResources().getDimensionPixelSize(R.dimen.step_view))
                .stepNumberTextSize(getResources().getDimensionPixelSize(R.dimen.step_view))
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        stepView.go(4, false);
    }

    public void goToNextPage(View view) {
        stepView.done(true);
        Toast.makeText(this, "تم الانتهاء من التسجيل بنجاح", Toast.LENGTH_SHORT).show();
        finish();
    }
}
