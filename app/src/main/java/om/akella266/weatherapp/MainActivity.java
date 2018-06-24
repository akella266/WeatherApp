package om.akella266.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import om.akella266.weatherapp.adapters.WeatherAdapter;
import om.akella266.weatherapp.adapters.listeners.ItemClickListener;
import om.akella266.weatherapp.api.Models.WeatherData;
import om.akella266.weatherapp.api.RestApi;
import om.akella266.weatherapp.common.AsyncTaskCompleteListener;
import om.akella266.weatherapp.common.GetWeather;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity
        implements AsyncTaskCompleteListener<List<WeatherData>> {

    private ArrayList<WeatherData> weatherList = new ArrayList<>();
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
                launchTask(restApi.getWeatherByCityName(city, units,
                        "1", key));
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
        launchTask(restApi.getWeatherGroupCities(ids, units, key));
    }

    private void dismissKeyboard(EditText locationEditText) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(locationEditText.getWindowToken(), 0);
    }

    private void launchTask(Call call){
        new GetWeather(this).execute(call);
    }

    private void updateUI() {
        if (adapter == null){
            final Context context = this;
            adapter = new WeatherAdapter(context, weatherList, new ItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = InfoActivity.newIntent(context, weatherList.get(position));
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        }
        adapter.setWeather(weatherList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskComplete(List<WeatherData> result) {
        weatherList.clear();
        weatherList.addAll(result);
        updateUI();
    }
}
