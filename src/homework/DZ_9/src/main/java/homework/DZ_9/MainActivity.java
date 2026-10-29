package homework.DZ_9;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView status = findViewById(R.id.houseStatus);
        Button startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(v -> {
            status.setText("Все системы готовы");
        });
    }
}
