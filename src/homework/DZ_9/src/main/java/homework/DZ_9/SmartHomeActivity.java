package homework.DZ_9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import homework.DZ_9.databinding.ActivitySmartHomeBinding;

public class SmartHomeActivity extends AppCompatActivity {

    private static final String KEY_TEMPERATURE = "key_temperature";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_HOUSE_STATUS = "key_house_status";

    private static final int DEFAULT_TEMPERATURE = 21;
    private static final int MIN_TEMPERATURE = 15;
    private static final int MAX_TEMPERATURE = 30;

    private ActivitySmartHomeBinding binding;

    private int temperature = DEFAULT_TEMPERATURE;
    private String username = "";
    private String houseStatus = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySmartHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        // Setup Toolbar & Drawer Toggle
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.nav_home, R.string.nav_home
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Update Header in Navigation Drawer with ViewBinding / findViewById on header view
        View headerView = binding.navigationView.getHeaderView(0);
        if (headerView != null) {
            TextView tvHeaderUsername = headerView.findViewById(R.id.tvHeaderUsername);
            if (tvHeaderUsername != null) {
                String userDisplay = (username != null && !username.isEmpty()) ? username : "Пользователь";
                tvHeaderUsername.setText(getString(R.string.welcome_user, userDisplay));
            }
        }

        // Display username in main screen
        String userDisplay = (username != null && !username.isEmpty()) ? username : "Пользователь";
        binding.tvWelcomeUser.setText(getString(R.string.welcome_user, userDisplay));

        updateTemperatureDisplay();
        binding.tvHouseStatus.setText(houseStatus);

        // Click listener for temperature decrease (-) button
        binding.btnTempDecrease.setOnClickListener(v -> {
            if (temperature > MIN_TEMPERATURE) {
                temperature--;
                updateTemperatureDisplay();
                Toast.makeText(this, getString(R.string.temp_decreased, temperature), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.temp_min_reached), Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener for temperature increase (+) button
        binding.btnTempIncrease.setOnClickListener(v -> {
            if (temperature < MAX_TEMPERATURE) {
                temperature++;
                updateTemperatureDisplay();
                Toast.makeText(this, getString(R.string.temp_increased, temperature), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.temp_max_reached), Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener for start mode button
        binding.btnStartMode.setOnClickListener(v -> {
            if (houseStatus.equals(getString(R.string.house_status_ready))) {
                houseStatus = getString(R.string.house_status_active);
            } else {
                houseStatus = getString(R.string.house_status_ready);
            }
            binding.tvHouseStatus.setText(houseStatus);
            Toast.makeText(this, getString(R.string.toast_status_click) + ": " + houseStatus, Toast.LENGTH_SHORT).show();
        });

        // Click listener for lighting card
        binding.cardLighting.setOnClickListener(v -> {
            Toast.makeText(this, R.string.toast_lighting_click, Toast.LENGTH_SHORT).show();
        });

        // Click listener for security card
        binding.cardSecurity.setOnClickListener(v -> {
            Toast.makeText(this, R.string.toast_security_click, Toast.LENGTH_SHORT).show();
        });

        // Navigation Drawer Item Click Handler
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Toast.makeText(this, R.string.nav_home, Toast.LENGTH_SHORT).show();
            } else if (itemId == R.id.nav_about) {
                Intent aboutIntent = new Intent(SmartHomeActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
            } else if (itemId == R.id.nav_logout) {
                Toast.makeText(this, R.string.nav_logout, Toast.LENGTH_SHORT).show();
                finish();
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void updateTemperatureDisplay() {
        binding.tvTemperature.setText(getString(R.string.temp_unit, temperature));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TEMPERATURE, temperature);
        outState.putString(KEY_USERNAME, username);
        outState.putString(KEY_HOUSE_STATUS, houseStatus);
    }
}
