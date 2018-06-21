package om.akella266.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import om.akella266.weatherapp.adapters.WeatherAdapter;
import om.akella266.weatherapp.api.Models.Weather;
import om.akella266.weatherapp.api.Models.WeatherDataResponse;
import om.akella266.weatherapp.api.Models.WeatherOld;
import om.akella266.weatherapp.api.Models.WeatherResponse;
import om.akella266.weatherapp.api.RestApi;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<WeatherOld> weatherOldList = new ArrayList<>();
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private String units;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        units = getString(R.string.units);
        key = getString(R.string.api_key);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText locationEditText = (EditText) findViewById(R.id.locationEditText);
                String city = locationEditText.getText().toString();

                RestApi restApi = RestApi.getRestApi();
                final Call<WeatherResponse> call = restApi.getWeatherByCityName(city, units,
                                                                    "3", key);
                new GetWeather().execute(call);
                dismissKeyboard(locationEditText);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.weatherRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        String ids = "";
        for(String id : getResources().getStringArray(R.array.cities)){
            ids += id + ",";
        }
        ids = ids.substring(0, ids.length()-1);
        RestApi restApi = RestApi.getRestApi();
        final Call<WeatherResponse> call = restApi.getWeatherGroupCities(ids, units, key);
        new GetWeather().execute(call);
    }

    private void dismissKeyboard(EditText locationEditText) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(locationEditText.getWindowToken(), 0);
    }

    private void updateUI() {

        if (adapter != null){
            adapter.setWeather(weatherOldList);
            adapter.notifyDataSetChanged();
        }
        else{
            adapter = new WeatherAdapter(weatherOldList);
            recyclerView.setAdapter(adapter);
        }
    }

    private class GetWeather extends AsyncTask<Call<WeatherResponse>, Void, List<WeatherOld>>{

        @Override
        protected List<WeatherOld> doInBackground(Call<WeatherResponse>... calls) {
            List<WeatherOld> adapterList = new ArrayList<>();
            try {
                Call<WeatherResponse> call = calls[0];
                Response<WeatherResponse> response = call.execute();
                for(WeatherDataResponse resp : response.body().getList()){
                    adapterList.add( new WeatherOld(
                            resp.getName(),
                            resp.getDt(),
                            resp.getMain().getTempMin(),
                            resp.getMain().getTempMax(),
                            resp.getMain().getHumidity(),
                            resp.getWeather().get(0).getDescription(),
                            resp.getWeather().get(0).getIcon()
                            ));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return adapterList;
        }

        @Override
        protected void onPostExecute(List<WeatherOld> weatherOlds) {
            weatherOldList.clear();
            weatherOldList.addAll(weatherOlds);
            updateUI();
        }
    }
}
