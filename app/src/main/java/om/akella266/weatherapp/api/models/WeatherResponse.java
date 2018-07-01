package om.akella266.weatherapp.api.models;

import com.squareup.moshi.Json;
import java.util.List;

public class WeatherResponse {
    @Json(name = "cod")
    private String cod;
    @Json(name = "message")
    private Double message;
    @Json(name = "cnt")
    private Integer cnt;
    @Json(name = "list")
    private List<WeatherDataResponse> list = null;
    @Json(name = "city")
    private City city;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public List<WeatherDataResponse> getList() {
        return list;
    }

    public void setList(List<WeatherDataResponse> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}

