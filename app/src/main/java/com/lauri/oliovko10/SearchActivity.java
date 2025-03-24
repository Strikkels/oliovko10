package com.lauri.oliovko10;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        try {
            areas = objectMapper.readTree(new URL("https://pxdata.stat.fi/PxWeb/api/v1/fi/StatFin/mkan/statfin_mkan_pxt_11ic.px"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        for (JsonNode node : areas.get("variables").get(0).get("values")){
            values.add(node.asText());
        }
        for (JsonNode node : areas.get("variables").get(0).get("valueTexts")){
            keys.add(node.asText());
        }
        HashMap<String,String> carDataCodes = new HashMap<>();

        for(int i = 0;i<keys.size(); i++) {
            carDataCodes.put(keys.get(i),values.get(i));
        }

        String code = null;

        code = carDataCodes.get(city);


        try {
            URL url = new URL("https://pxdata.stat.fi/PxWeb/api/v1/fi/StatFin/mkan/statfin_mkan_pxt_11ic.px");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            JsonNode jsonInputString = objectMapper.readTree(context.getResources().openRawResource(R.raw.query));

            ((ObjectNode) jsonInputString.get("query").get(0).get("selection")).putArray("values").add(code);
            ((ObjectNode) jsonInputString.get("query").get(3).get("selection")).putArray("values").add(year);

            byte[] input = objectMapper.writeValueAsBytes(jsonInputString);
            OutputStream os = con.getOutputStream();
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }

            JsonNode carData = objectMapper.readTree(response.toString());

            ArrayList<String> types = new ArrayList<>();
            ArrayList<String> amounts = new ArrayList<>();

            for (JsonNode node : carData.get("dimension").get("Ajoneuvoluokka").get("category").get("label")){
                types.add(node.asText());
            }
            for (JsonNode node : carData.get("value")){
                amounts.add(node.asText());
            }
            for (int i = 0;i < types.size();i++){
                CarDataStorage.getInstance().addCarData(new CarData(types.get(i),Integer.parseInt(amounts.get(i))));
            }
            //CarDataStorage.getInstance().log(carData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void searchData(View view){
        String city = cityInput.getText().toString();
        String yearStr = yearInput.getText().toString();
        Context context = this;

        txtStatusMessage.setText("Haetaan");

        if(city.isEmpty()){
            txtStatusMessage.setText("Haku epäonnistui. Tyhjä hakukenttä");
            return;
        }
        try{
            int year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e){
            txtStatusMessage.setText("Haku epäonnistui");
            return;
        }
        int year = Integer.parseInt(yearStr);
        CarDataStorage.getInstance().setCity(city);
        CarDataStorage.getInstance().setYear(year);
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                getData(context, city, year);
            }
        });
        txtStatusMessage.setText("Haku onnistui");
    }
}