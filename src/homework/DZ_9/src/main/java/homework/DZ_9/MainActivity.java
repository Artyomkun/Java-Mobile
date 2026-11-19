package homework.DZ_9;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import homework.DZ_9.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "homework.DZ_9.EXTRA_USERNAME";
    private static final String KEY_LOGIN = "key_login";
    private static final String KEY_PASSWORD = "key_password";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            binding.loginInput.setText(savedInstanceState.getString(KEY_LOGIN, ""));
            binding.passwordInput.setText(savedInstanceState.getString(KEY_PASSWORD, ""));
        }

        binding.loginButton.setOnClickListener(v -> {
            String login = binding.loginInput.getText().toString().trim();
            String password = binding.passwordInput.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                binding.statusText.setText(R.string.login_empty_error);
                Toast.makeText(this, R.string.login_empty_error, Toast.LENGTH_SHORT).show();
                return;
            }

            binding.statusText.setText("Авторизация выполнена успешно");
            Toast.makeText(this, getString(R.string.welcome_user, login), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, SmartHomeActivity.class);
            intent.putExtra(EXTRA_USERNAME, login);
            startActivity(intent);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_LOGIN, binding.loginInput.getText().toString());
        outState.putString(KEY_PASSWORD, binding.passwordInput.getText().toString());
    }
}
