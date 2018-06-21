package om.akella266.weatherapp.api.Models;

import com.squareup.moshi.Json;
import java.util.List;

public class WeatherDataResponse {
    @Json(name = "dt")
    private Integer dt;
    @Json(name = "name")
    private String name;
    @Json(name = "main")
    private Main main;
    @Json(name = "weather")
    private List<Weather> weather = null;

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
