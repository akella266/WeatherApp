package om.akella266.weatherapp.api;

import java.util.List;

import om.akella266.weatherapp.api.Models.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

interface OpenWeatherMapApi {

    @GET("data/2.5/forecast")
    Call<WeatherResponse> getWeatherByCityName(@Query("q") String cityName,
                                               @Query("units") String units,
                                               @Query("cnt") String countDays,
                                               @Query("APPID") String key);

    @GET("data/2.5/group")
    Call<WeatherResponse> getWeatherGroupCities(@Query("id") String id,
                                                @Query("units") String units,
                                                @Query("APPID") String key);
}
