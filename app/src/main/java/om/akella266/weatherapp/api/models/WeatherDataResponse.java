package om.akella266.weatherapp.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;
import java.util.List;

public class WeatherDataResponse implements Parcelable{
    @Json(name = "dt")
    private Integer dt;
    @Json(name = "name")
    private String name;
    @Json(name = "main")
    private Main main;
    @Json(name = "weather")
    private List<Weather> weather = null;

    private WeatherDataResponse(Parcel in) {
        if (in.readByte() == 0) {
            dt = null;
        } else {
            dt = in.readInt();
        }
        name = in.readString();
        main = (Main) in.readValue(Main.class.getClassLoader());
        in.readList(weather, Weather.class.getClassLoader());
    }

    public static final Creator<WeatherDataResponse> CREATOR = new Creator<WeatherDataResponse>() {
        @Override
        public WeatherDataResponse createFromParcel(Parcel in) {
            return new WeatherDataResponse(in);
        }

        @Override
        public WeatherDataResponse[] newArray(int size) {
            return new WeatherDataResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dt);
        dest.writeString(name);
        dest.writeValue(main);
        dest.writeList(weather);
    }

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
