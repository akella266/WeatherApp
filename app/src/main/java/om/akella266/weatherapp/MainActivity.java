package om.akella266.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import om.akella266.weatherapp.adapters.WeatherAdapter;
import om.akella266.weatherapp.adapters.listeners.ItemClickListener;
import om.akella266.weatherapp.api.models.WeatherData;
import om.akella266.weatherapp.api.RestApi;
import om.akella266.weatherapp.common.AsyncTaskCompleteListener;
import om.akella266.weatherapp.common.AsyncTaskResult;
import om.akella266.weatherapp.common.GetWeather;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity
        implements AsyncTaskCompleteListener<AsyncTaskResult<List<WeatherData>>> {

    private EditText locationEditText;
    private ArrayList<WeatherData> weatherList = new ArrayList<>();
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private String units;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationEditText = findViewById(R.id.locationEditText);

        units = getString(R.string.units);
        key = getString(R.string.api_key);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = locationEditText.getText().toString();

                if (isConnected()) {
                    RestApi restApi = RestApi.getRestApi();
                    launchTask(restApi.getWeatherByCityName(city, units,
                            "1", key));
                    dismissKeyboard(locationEditText);
                }
                else{
                    Snackbar.make(recyclerView, getString(R.string.connect_error), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        recyclerView = findViewById(R.id.weatherRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        setDefaultForecast();
    }

    private void dismissKeyboard(EditText locationEditText) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(locationEditText.getWindowToken(), 0);
        }
    }

    private void launchTask(Call call){
        new GetWeather(this).execute(call);
    }

    private void setDefaultForecast(){
        if (isConnected()) {
            String ids = "";
            for (String id : getResources().getStringArray(R.array.cities)) {
                ids += id + ",";
            }
            ids = ids.substring(0, ids.length() - 1);
            RestApi restApi = RestApi.getRestApi();
            launchTask(restApi.getWeatherGroupCities(ids, units, key));
        }
        else{
            Snackbar.make(recyclerView, getString(R.string.connect_error), Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null)
                return activeNetwork.isConnectedOrConnecting();
            else
                return false;
        }
        return false;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.reset:{
                locationEditText.setText("");
                setDefaultForecast();
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onTaskComplete(AsyncTaskResult<List<WeatherData>> result) {
        weatherList.clear();
        if (result != null) {
            if (result.getError() == null) {
                weatherList.addAll(result.getResult());
                updateUI();
            } else {
                Snackbar.make(recyclerView, getString(R.string.read_error), Snackbar.LENGTH_LONG).show();
            }
        }
        else{
            Snackbar.make(recyclerView, getString(R.string.connect_error), Snackbar.LENGTH_LONG).show();
        }
    }
}
