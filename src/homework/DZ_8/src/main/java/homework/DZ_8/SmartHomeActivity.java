package homework.DZ_8;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class SmartHomeActivity extends AppCompatActivity {

    private static final String KEY_TEMPERATURE = "key_temperature";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_HOUSE_STATUS = "key_house_status";

    private static final int DEFAULT_TEMPERATURE = 21;
    private static final int MIN_TEMPERATURE = 15;
    private static final int MAX_TEMPERATURE = 30;

    private int temperature = DEFAULT_TEMPERATURE;
    private String username = "";
    private String houseStatus = "";

    private TextView tvWelcomeUser;
    private TextView tvTemperature;
    private TextView tvHouseStatus;
    private Button btnTempDecrease;
    private Button btnTempIncrease;
    private Button btnStartMode;
    private MaterialCardView cardLighting;
    private MaterialCardView cardSecurity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_home);

        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvHouseStatus = findViewById(R.id.tvHouseStatus);
        btnTempDecrease = findViewById(R.id.btnTempDecrease);
        btnTempIncrease = findViewById(R.id.btnTempIncrease);
        btnStartMode = findViewById(R.id.btnStartMode);
        cardLighting = findViewById(R.id.cardLighting);
        cardSecurity = findViewById(R.id.cardSecurity);

        // Restore state or retrieve intent extra
        if (savedInstanceState != null) {
            temperature = savedInstanceState.getInt(KEY_TEMPERATURE, DEFAULT_TEMPERATURE);
            username = savedInstanceState.getString(KEY_USERNAME, "");
            houseStatus = savedInstanceState.getString(KEY_HOUSE_STATUS, getString(R.string.house_status_ready));
        } else {
            if (getIntent() != null && getIntent().hasExtra(MainActivity.EXTRA_USERNAME)) {
                username = getIntent().getStringExtra(MainActivity.EXTRA_USERNAME);
            }
            houseStatus = getString(R.string.house_status_ready);
        }

        // Display username
        if (username != null && !username.isEmpty()) {
            tvWelcomeUser.setText(getString(R.string.welcome_user, username));
        } else {
            tvWelcomeUser.setText(getString(R.string.welcome_user, "Пользователь"));
        }

        updateTemperatureDisplay();
        tvHouseStatus.setText(houseStatus);

        // Click listener for temperature decrease (-) button
        btnTempDecrease.setOnClickListener(v -> {
            if (temperature > MIN_TEMPERATURE) {
                temperature--;
                updateTemperatureDisplay();
                Toast.makeText(this, getString(R.string.temp_decreased, temperature), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.temp_min_reached), Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener for temperature increase (+) button
        btnTempIncrease.setOnClickListener(v -> {
            if (temperature < MAX_TEMPERATURE) {
                temperature++;
                updateTemperatureDisplay();
                Toast.makeText(this, getString(R.string.temp_increased, temperature), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.temp_max_reached), Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener for start mode button
        btnStartMode.setOnClickListener(v -> {
            if (houseStatus.equals(getString(R.string.house_status_ready))) {
                houseStatus = getString(R.string.house_status_active);
            } else {
                houseStatus = getString(R.string.house_status_ready);
            }
            tvHouseStatus.setText(houseStatus);
            Toast.makeText(this, getString(R.string.toast_status_click) + ": " + houseStatus, Toast.LENGTH_SHORT).show();
        });

        // Click listener for lighting card
        cardLighting.setOnClickListener(v -> {
            Toast.makeText(this, R.string.toast_lighting_click, Toast.LENGTH_SHORT).show();
        });

        // Click listener for security card
        cardSecurity.setOnClickListener(v -> {
            Toast.makeText(this, R.string.toast_security_click, Toast.LENGTH_SHORT).show();
        });
    }

    private void updateTemperatureDisplay() {
        tvTemperature.setText(getString(R.string.temp_unit, temperature));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TEMPERATURE, temperature);
        outState.putString(KEY_USERNAME, username);
        outState.putString(KEY_HOUSE_STATUS, houseStatus);
    }
}
