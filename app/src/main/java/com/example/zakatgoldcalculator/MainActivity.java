package com.example.zakatgoldcalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    // Declare UI elements
    EditText editWeight, editValue;
    RadioGroup radioGroupType;
    RadioButton radioKeep, radioWear;
    Button btnCalculate, btnReset;
    TextView textTotalValue, textZakatPayable, textTotalZakat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Initialize widgets
        editWeight = findViewById(R.id.editWeight);
        editValue = findViewById(R.id.editValue);
        radioGroupType = findViewById(R.id.radioGroupType);
        radioKeep = findViewById(R.id.radioKeep);
        radioWear = findViewById(R.id.radioWear);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        textTotalValue = findViewById(R.id.textTotalValue);
        textZakatPayable = findViewById(R.id.textZakatPayable);
        textTotalZakat = findViewById(R.id.textTotalZakat);

        // Create number formatter
        DecimalFormat df = new DecimalFormat("#,##0.00");

        // Button click listener
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Step 1: Validate input fields
                String weightStr = editWeight.getText().toString().trim();
                String valueStr = editValue.getText().toString().trim();

                if (weightStr.isEmpty() || valueStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both weight and gold value", Toast.LENGTH_SHORT).show();
                    return;
                }

                double weight;
                double value;

                try {
                    weight = Double.parseDouble(weightStr);
                    value = Double.parseDouble(valueStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid number format.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Step 2: Check gold type (keep/wear)
                double uruf;
                if (radioKeep.isChecked()) {
                    uruf = 85; // for gold kept
                } else if (radioWear.isChecked()) {
                    uruf = 200; // for gold worn
                } else {
                    Toast.makeText(MainActivity.this, "Please select type of gold.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Step 3: Calculate total value
                double totalValue = weight * value;

                // Step 4: Determine gold payable (grams)
                double goldValuePayable = (weight - uruf) * value;

                if (goldValuePayable < 0) goldValuePayable = 0;

                // Step 5: Calculate total zakat
                double totalZakat = goldValuePayable * 0.025; // 2.5%

                // Step 6: Display results with proper formatting
                textTotalValue.setText("Total Gold Value: RM" + df.format(totalValue));
                textZakatPayable.setText("Gold Value Zakat Payable: RM" + df.format(goldValuePayable));
                textTotalZakat.setText("Total Zakat (2.5%): RM" + df.format(totalZakat));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWeight.setText("");
                editValue.setText("");
                radioGroupType.clearCheck();
                textTotalValue.setText("Total Gold Value: RM0");
                textZakatPayable.setText("Gold Value Zakat Payable: RM0");
                textTotalZakat.setText("Total Zakat: RM0");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;

        } else if (selected == R.id.menuSettings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (selected == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Please use my Zakat Gold application - https://github.com/hidayahrozaidi19/ZakatGoldCalculator.git");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return super.onOptionsItemSelected(item);
        }

        return false;
    }
}