package om.akella266.weatherapp.common;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import om.akella266.weatherapp.api.Models.WeatherDataResponse;
import om.akella266.weatherapp.api.Models.WeatherData;
import om.akella266.weatherapp.api.Models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Response;

public class GetWeather extends AsyncTask<Call<WeatherResponse>, Void, List<WeatherData>> {

    private AsyncTaskCompleteListener listener;

    public GetWeather(AsyncTaskCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<WeatherData> doInBackground(Call<WeatherResponse>... calls) {
        List<WeatherData> adapterList = new ArrayList<>();
        try {
            Call<WeatherResponse> call = calls[0];
            Response<WeatherResponse> response = call.execute();
            for (WeatherDataResponse resp : response.body().getList()) {
                adapterList.add(new WeatherData(
                        resp.getName() == null ? response.body().getCity().getName() : resp.getName(),
                        resp.getDt(),
                        resp.getMain().getTemp(),
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
    protected void onPostExecute(List<WeatherData> weathers) {
        listener.onTaskComplete(weathers);
    }
}