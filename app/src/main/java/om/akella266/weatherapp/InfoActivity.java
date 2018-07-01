package om.akella266.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import om.akella266.weatherapp.adapters.WeatherAdapter;
import om.akella266.weatherapp.api.Models.WeatherData;
import om.akella266.weatherapp.api.RestApi;
import om.akella266.weatherapp.common.AsyncTaskCompleteListener;
import om.akella266.weatherapp.common.AsyncTaskResult;
import om.akella266.weatherapp.common.GetWeather;

public class InfoActivity extends AppCompatActivity
        implements AsyncTaskCompleteListener<AsyncTaskResult<List<WeatherData>>> {

    private int countDays;
    private RecyclerView rvForecast;
    private List<WeatherData> weatherList;
    private WeatherAdapter adapter;

    public static final String EXTRA_WEATHER_DATA = "on.akella266.infointent.weather_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        final WeatherData weather = intent.getParcelableExtra(EXTRA_WEATHER_DATA);
        setTitle(weather.getCityName());
        initDetails(weather);

        if (isConnected())
            launchTask(weather.getCityName());
        else
            Snackbar.make(rvForecast, getString(R.string.connect_error), Snackbar.LENGTH_LONG).show();

        rvForecast = findViewById(R.id.rvForecast);
        rvForecast.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        weatherList = new ArrayList<>();

        RadioGroup rbCountDays = findViewById(R.id.countDays);
        rbCountDays.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setCountDays(checkedId);
            }
        });
        setCountDays(rbCountDays.getCheckedRadioButtonId());
    }

    private void setCountDays(int checkedId){
        switch (checkedId){
            case R.id.rbThree:{countDays = 3;} break;
            case R.id.rbFive:{countDays = 5;} break;
        }
        updateUI();
    }

    private void launchTask(String cityName) {
        String units = getString(R.string.units);
        String key = getString(R.string.api_key);

        RestApi api = RestApi.getRestApi();
        new GetWeather(this)
                .execute(api.getWeatherByCityName(cityName, units, null, key));
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

    private void initDetails(WeatherData w){
        ImageView conditionImageView = findViewById(R.id.conditionImageViewDetails);
        TextView dayTextView = findViewById(R.id.dayTextViewDetails);
        TextView lowTextView = findViewById(R.id.lowTextViewDetails);
        TextView highTextView = findViewById(R.id.highTextViewDetails);
        TextView humidityTextView = findViewById(R.id.humidityTextViewDetails);
        TextView temperatureTextView = findViewById(R.id.temperatureViewDetails);

        dayTextView.setText(getString(R.string.day_description, "", w.getDayOfWeek(), w.getDescription()));
        temperatureTextView.setText(getString(R.string.temperature, w.getTemp()));
        highTextView.setText(getString(R.string.high_temp,w.getMaxTemp()));
        lowTextView.setText(getString(R.string.low_temp, w.getMinTemp()));
        humidityTextView.setText(getString(R.string.humidity, w.getHumidity()));

        Picasso.with(this).load(w.getIconURL())
                .error(R.drawable.ic_no_content_black_24dp)
                .resizeDimen(R.dimen.image_side_length_big, R.dimen.image_side_length_big)
                .into(conditionImageView);
    }

    private void updateUI(){
        if (adapter == null){
            final Context context = this;
            adapter = new WeatherAdapter(context, weatherList, null);
            rvForecast.setAdapter(adapter);
        }
        if (weatherList.size() != 0) {
            adapter.setWeather(countDays == 3 ? weatherList.subList(0, 3) : weatherList);
            adapter.notifyDataSetChanged();
        }
    }

    public static Intent newIntent(Context context, WeatherData data){
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra(EXTRA_WEATHER_DATA, data);
        return intent;
    }

    @Override
    public void onTaskComplete(AsyncTaskResult<List<WeatherData>> result) {
        if (result != null) {
            if (result.getError() == null) {
                List<WeatherData> weatherResult = result.getResult();
                weatherList.add(weatherResult.get(0));
                for (int i = 1; i < weatherResult.size(); i++) {
                    if (weatherResult.get(i).getDay() != weatherResult.get(i - 1).getDay()) {
                        //set middle value for temperature
                        int lastIndex = weatherList.size() - 1;
                        weatherList.get(lastIndex).setTemp((weatherList.get(lastIndex).getMaxTemp()
                                + weatherList.get(lastIndex).getMinTemp()) / 2);
                        weatherList.add(weatherResult.get(i));
                    } else {
                        //set min and max temp from all list to every day
                        if (weatherResult.get(i).getMinTemp() < weatherResult.get(i - 1).getMinTemp()) {
                            weatherList.get(weatherList.size() - 1).setMinTemp(weatherResult.get(i).getMinTemp());
                        }
                        if (weatherResult.get(i).getMaxTemp() > weatherResult.get(i - 1).getMaxTemp()) {
                            weatherList.get(weatherList.size() - 1).setMaxTemp(weatherResult.get(i).getMaxTemp());
                        }
                    }
                }
                weatherList.remove(0);
                updateUI();
            }
            else{
                Snackbar.make(rvForecast, getString(R.string.read_error), Snackbar.LENGTH_LONG).show();
            }
        }
        else{
            Snackbar.make(rvForecast, getString(R.string.connect_error), Snackbar.LENGTH_LONG).show();
        }
    }
}
