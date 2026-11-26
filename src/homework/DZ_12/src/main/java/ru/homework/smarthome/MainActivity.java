package ru.homework.smarthome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String TEMPERATURE_DIALOG_TAG = "temperature_dialog";
    private static final int[] TEMPERATURES = {19, 20, 21, 22, 23, 24, 25};

    private int temperature = 22;
    private boolean securityArmed = true;
    private TextView temperatureValue;
    private TextView securityStatus;
    private Button securityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureValue = findViewById(R.id.temperature_value);
        securityStatus = findViewById(R.id.security_status);
        securityButton = findViewById(R.id.security_button);

        if (savedInstanceState != null) {
            temperature = savedInstanceState.getInt("temperature", 22);
            securityArmed = savedInstanceState.getBoolean("security_armed", true);
        }
        updateTemperature();
        updateSecurityStatus();

        findViewById(R.id.temperature_button).setOnClickListener(view ->
                new TemperatureDialogFragment().show(getFragmentManager(), TEMPERATURE_DIALOG_TAG));
        securityButton.setOnClickListener(view -> showSecurityConfirmation());
    }

    private void showSecurityConfirmation() {
        if (securityArmed) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.disarm_title)
                    .setMessage(R.string.disarm_message)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.disarm, (dialog, which) -> {
                        securityArmed = false;
                        updateSecurityStatus();
                    })
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.arm_title)
                    .setMessage(R.string.arm_message)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.arm, (dialog, which) -> {
                        securityArmed = true;
                        updateSecurityStatus();
                    })
                    .show();
        }
    }

    void setTemperature(int value) {
        temperature = value;
        updateTemperature();
    }

    private void updateTemperature() {
        temperatureValue.setText(getString(R.string.temperature_value, temperature));
    }

    private void updateSecurityStatus() {
        securityStatus.setText(securityArmed ? R.string.security_armed : R.string.security_disarmed);
        securityStatus.setTextColor(getColor(
                securityArmed ? R.color.status_green : R.color.status_orange));
        securityButton.setText(securityArmed ? R.string.disarm_button : R.string.arm_button);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("temperature", temperature);
        outState.putBoolean("security_armed", securityArmed);
        super.onSaveInstanceState(outState);
    }

    public static class TemperatureDialogFragment extends DialogFragment {

        private int selectedTemperature = 22;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                selectedTemperature = savedInstanceState.getInt("selected_temperature", 22);
            } else if (getActivity() instanceof MainActivity) {
                selectedTemperature = ((MainActivity) getActivity()).temperature;
            }

            int checkedItem = 3;
            for (int i = 0; i < TEMPERATURES.length; i++) {
                if (TEMPERATURES[i] == selectedTemperature) {
                    checkedItem = i;
                    break;
                }
            }

            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.temperature_dialog_title)
                    .setSingleChoiceItems(R.array.temperature_options, checkedItem,
                            (dialog, which) -> selectedTemperature = TEMPERATURES[which])
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.apply, (dialog, which) -> {
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).setTemperature(selectedTemperature);
                        }
                    })
                    .create();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putInt("selected_temperature", selectedTemperature);
            super.onSaveInstanceState(outState);
        }
    }
}
