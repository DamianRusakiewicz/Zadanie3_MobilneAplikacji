package com.example.zadanie3;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zadanie3.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.CDATASection;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText cityEd;
    TextView resultTv;
    Button btnFetch;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityEd = findViewById(R.id.city_et);
        resultTv = findViewById(R.id.result_tv);
        btnFetch = findViewById(R.id.btn_fet);

        btnFetch.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_fet){
            cityName = cityEd.getText().toString();
            getData();
        }

    }
    public void getData(){
        Uri uri = Uri.parse("https://datausa.io/api/data?drilldowns=State&measures=Population&year=latest")
                .buildUpon().build();

    }
    class DOTask extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls [0];
            String data=null;
            try {
                data = NetworkUtils.makeHTTPRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s){
            try {
                parseJson(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void parseJson(String data){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
            }catch (JSONException e){
                e.printStackTrace();
            }
            JSONArray cityArray = jsonObject.getJSONArray("data");

            for (int i=0; i<cityArray.length();i++){
                JSONObject cityo = cityArray.getJSONObject(i);
                String cityn = cityo.get("State").toString();
                if(cityn.equals(cityName)){
                    String population = cityo.get("Population").toString();
                    resultTv.setText(population);
                    break;
                }
                else {
                    resultTv.setText("Not found");
                }

            }
        }
    }

}