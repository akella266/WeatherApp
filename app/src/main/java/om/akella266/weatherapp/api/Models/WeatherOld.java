package om.akella266.weatherapp.api.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class WeatherOld implements Parcelable {
    public final String cityName;
    public final String dayOfWeek;
    public final int day;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String iconURL;

    public WeatherOld(String cityName, long timeStamp, double minTemp, double maxTemp,
                      double humidity, String description, String iconName){

        //NumberFormat for format temperature in whole number
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);

        this.cityName = cityName;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp*1000);
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat dateFormater = new SimpleDateFormat();

        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.dayOfWeek = dateFormater.format(calendar.getTime());
        this.minTemp = nf.format(minTemp);
        this.maxTemp = nf.format(maxTemp);
        this.humidity = NumberFormat.getPercentInstance().format(humidity/100.0);
        this.description = description;
        this.iconURL = "http://openweathermap.org/img/w/" + iconName + ".png";
    }

    protected WeatherOld(Parcel in) {
        cityName = in.readString();
        dayOfWeek = in.readString();
        minTemp = in.readString();
        maxTemp = in.readString();
        humidity = in.readString();
        description = in.readString();
        iconURL = in.readString();
        day = in.readInt();
    }

    public static final Creator<WeatherOld> CREATOR = new Creator<WeatherOld>() {
        @Override
        public WeatherOld createFromParcel(Parcel in) {
            return new WeatherOld(in);
        }

        @Override
        public WeatherOld[] newArray(int size) {
            return new WeatherOld[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cityName);
        dest.writeString(dayOfWeek);
        dest.writeString(minTemp);
        dest.writeString(maxTemp);
        dest.writeString(humidity);
        dest.writeString(description);
        dest.writeString(iconURL);
        dest.writeInt(day);
    }
}
