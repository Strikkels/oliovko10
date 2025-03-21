package com.lauri.oliovko10;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchActivity extends AppCompatActivity {
    private EditText cityInput;
    private EditText yearInput;
    private TextView txtStatusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cityInput = findViewById(R.id.CityNameEdit);
        yearInput = findViewById(R.id.YearEdit);
        txtStatusMessage = findViewById(R.id.StatusText);
    }
    public void switchToMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void getData(Context context, String city, int year){
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode areas = null;
    }
    public void searchData(View view){
        String city = cityInput.getText().toString();
        String yearStr = yearInput.getText().toString();
        try {
            int year = Integer.parseInt(yearStr);
            if(!city.isEmpty()){
                getData(this, city, year);
                txtStatusMessage.setText("Kaikki ok");
            } else{
                txtStatusMessage.setText("Virheellinen kaupunni");
            }
        }
        catch (NumberFormatException e) {
            txtStatusMessage.setText("Virheellinen vuosiluku");
        }

    }
}