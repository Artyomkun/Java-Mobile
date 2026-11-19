package homework.DZ_8;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "homework.DZ_8.EXTRA_USERNAME";
    private static final String KEY_LOGIN = "key_login";
    private static final String KEY_PASSWORD = "key_password";

    private EditText loginInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView statusText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginInput = findViewById(R.id.loginInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        statusText = findViewById(R.id.statusText);

        if (savedInstanceState != null) {
            loginInput.setText(savedInstanceState.getString(KEY_LOGIN, ""));
            passwordInput.setText(savedInstanceState.getString(KEY_PASSWORD, ""));
        }

        loginButton.setOnClickListener(v -> {
            String login = loginInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                statusText.setText("Введите логин и пароль");
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            statusText.setText("Авторизация выполнена успешно");
            Toast.makeText(this, "Добро пожаловать, " + login, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, SmartHomeActivity.class);
            intent.putExtra(EXTRA_USERNAME, login);
            startActivity(intent);
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_LOGIN, loginInput.getText().toString());
        outState.putString(KEY_PASSWORD, passwordInput.getText().toString());
    }
}
