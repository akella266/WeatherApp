package om.akella266.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import om.akella266.weatherapp.adapters.WeatherAdapter;
import om.akella266.weatherapp.adapters.listeners.ItemClickListener;
import om.akella266.weatherapp.api.Models.WeatherOld;
import om.akella266.weatherapp.api.RestApi;
import om.akella266.weatherapp.common.AsyncTaskCompleteListener;
import om.akella266.weatherapp.common.GetWeather;

public class InfoActivity extends AppCompatActivity
        implements AsyncTaskCompleteListener<List<WeatherOld>> {

    private ImageView conditionImageView;
    private TextView dayTextView;
    private TextView lowTextView;
    private TextView highTextView;
    private TextView humidityTextView;
    private RadioGroup rbCountDays;
    private int countDays;
    private RecyclerView rvForecast;
    private List<WeatherOld> weatherList;
    private WeatherAdapter adapter;

    public static final String EXTRA_WEATHER_DATA = "on.akella266.infointent.weather_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        final WeatherOld weather = intent.getParcelableExtra(EXTRA_WEATHER_DATA);
        toolbar.setTitle(weather.cityName);
        initDetails(weather);
        launchTask(weather.cityName);
        rvForecast = findViewById(R.id.rvForecast);
        rvForecast.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        weatherList = new ArrayList<>();

        rbCountDays = findViewById(R.id.countDays);
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

    private void initDetails(WeatherOld w){
        conditionImageView = findViewById(R.id.conditionImageViewDetails);
        dayTextView = findViewById(R.id.dayTextViewDetails);
        lowTextView = findViewById(R.id.lowTextViewDetails);
        highTextView = findViewById(R.id.highTextViewDetails);
        humidityTextView = findViewById(R.id.humidityTextViewDetails);

        dayTextView.setText(getString(R.string.day_description,w.dayOfWeek, w.description));
        highTextView.setText(getString(R.string.high_temp,w.maxTemp));
        lowTextView.setText(getString(R.string.low_temp, w.minTemp));
        humidityTextView.setText(getString(R.string.humidity, w.humidity));

        Picasso.with(this).load(w.iconURL).into(conditionImageView);

    }

    private void updateUI(){
        if (adapter == null){
            final Context context = this;
            adapter = new WeatherAdapter(context, weatherList, new ItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = InfoActivity.newIntent(context, weatherList.get(position));
                    startActivity(intent);
                }
            });
            rvForecast.setAdapter(adapter);
        }
        if (weatherList.size() != 0) {
            adapter.setWeather(countDays == 3 ? weatherList.subList(0, 3) : weatherList);
            adapter.notifyDataSetChanged();
        }
    }

    public static Intent newIntent(Context context, WeatherOld data){
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra(EXTRA_WEATHER_DATA, data);
        return intent;
    }

    @Override
    public void onTaskComplete(List<WeatherOld> result) {
        weatherList.add(result.get(0));
        for(int i = 1; i < result.size(); i++){
            if (result.get(i).day != result.get(i-1).day){
                weatherList.add(result.get(i));
            }
        }
        updateUI();
    }
}
