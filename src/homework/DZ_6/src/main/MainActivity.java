package homework.DZ_6.src.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText nameInput;
    private Button submitButton;
    private Switch emailSwitch;
    private CheckBox agreeCheckBox;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = findViewById(R.id.nameInput);
        submitButton = findViewById(R.id.submitButton);
        emailSwitch = findViewById(R.id.emailSwitch);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);
        resultTextView = findViewById(R.id.resultTextView);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                boolean notificationsEnabled = emailSwitch.isChecked();
                boolean accepted = agreeCheckBox.isChecked();

                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Введите имя", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!accepted) {
                    Toast.makeText(MainActivity.this, "Подтвердите соглашение", Toast.LENGTH_SHORT).show();
                    return;
                }

                String status = notificationsEnabled ? "включены" : "выключены";
                resultTextView.setText(
                        "Имя: " + name + "\n" +
                        "Уведомления: " + status + "\n" +
                        "Согласие: принято"
                );
            }
        });
    }
}
