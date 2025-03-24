package com.lauri.oliovko10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListInfoActivity extends AppCompatActivity {
    private TextView txtCity;
    private TextView txtYear;
    private TextView txtCarInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtCity = findViewById(R.id.CityText);
        txtYear = findViewById(R.id.YearText);
        txtCarInfo = findViewById(R.id.CarInfoText);

        String s = "";
        int carSum = 0;
        for (CarData carData : CarDataStorage.getInstance().getCarData()){
            s = s + carData.getType() + ": " + carData.getAmount() + "\n";
            carSum += carData.getAmount();
        }
        s = s + "Yhteensä: " + carSum;

        txtCity.setText(CarDataStorage.getInstance().getCity());
        txtYear.setText(String.valueOf(CarDataStorage.getInstance().getYear()));
        txtCarInfo.setText(s);

    }

    public void switchToMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}