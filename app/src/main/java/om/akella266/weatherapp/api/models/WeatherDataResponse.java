package om.akella266.weatherapp.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherDataResponse implements Parcelable{
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("coord")
    @Expose
    private Coord coord;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("sys")
    @Expose
    private Sys sys;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("weather")
    @Expose
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
        id = in.readLong();
        coord = (Coord) in.readValue(Coord.class.getClassLoader());
        sys = (Sys) in.readValue(Sys.class.getClassLoader());
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
        dest.writeLong(id);
        dest.writeValue(coord);
        dest.writeValue(sys);
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }
}
