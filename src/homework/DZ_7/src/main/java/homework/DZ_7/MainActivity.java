package homework.DZ_7;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String SERVER_URL = "http://10.0.2.2:8080/alice";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    private TextView tempTextView;
    private Switch mainSwitch;
    private Button buttonCooling;
    private Button buttonTimer;
    private Button buttonTurbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = findViewById(R.id.tempTextView);
        mainSwitch = findViewById(R.id.mainSwitch);
        buttonCooling = findViewById(R.id.buttonCooling);
        buttonTimer = findViewById(R.id.buttonTimer);
        buttonTurbo = findViewById(R.id.buttonTurbo);

        mainSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String command = isChecked ? "включи умный дом" : "выключи умный дом";
            sendCommand(command);
        });

        buttonCooling.setOnClickListener(v -> sendCommand("включи кондиционер и установи температуру 22"));
        buttonTimer.setOnClickListener(v -> sendCommand("установи таймер на 20 минут"));
        buttonTurbo.setOnClickListener(v -> sendCommand("включи свет в гостиной"));
    }

    private void sendCommand(String command) {
        final String payload = "{\"request\":{\"command\":\"" + escapeJson(command) + "\"}}";

        executor.execute(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(payload.getBytes(StandardCharsets.UTF_8));
                }

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP " + responseCode);
                }

                StringBuilder response = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                final String responseText = extractText(response.toString());
                uiHandler.post(() -> {
                    tempTextView.setText(responseText);
                    Toast.makeText(this, responseText, Toast.LENGTH_SHORT).show();
                });

            } catch (Exception e) {
                final String error = "Ошибка связи с сервером";
                uiHandler.post(() -> {
                    tempTextView.setText(error);
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                });
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignored) {
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    private String extractText(String json) {
        Pattern pattern = Pattern.compile("\\\"text\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1).replace("\\n", "\n");
        }
        return "Команда обработана";
    }

    private String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
