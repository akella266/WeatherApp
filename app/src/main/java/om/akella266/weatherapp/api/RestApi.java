package om.akella266.weatherapp.api;

import om.akella266.weatherapp.api.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi implements OpenWeatherMapApi {

    private OpenWeatherMapApi api;
    private static RestApi restApi;

    private RestApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(OpenWeatherMapApi.class);
    }

    public static RestApi getRestApi(){
        if (restApi == null){
            restApi = new RestApi();
        }
        return restApi;
    }

    @Override
    public Call<WeatherResponse> getWeatherByCityName(String cityName, String units,
                                                      String countDays, String key) {
        return api.getWeatherByCityName(cityName, units, countDays, key);
    }

    @Override
    public Call<WeatherResponse> getWeatherGroupCities(String citiesID, String units, String key) {
        return api.getWeatherGroupCities(citiesID, units, key);
    }

    @Override
    public Call<WeatherResponse> findCities(String nameOfCity, String key) {
        return api.findCities(nameOfCity, key);
    }
}
