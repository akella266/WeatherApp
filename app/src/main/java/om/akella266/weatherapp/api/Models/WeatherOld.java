package om.akella266.weatherapp.api.Models;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class WeatherOld {
    public final String cityName;
    public final String dayOfWeek;
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
        this.dayOfWeek = convertTimeStamptoDay(timeStamp);
        this.minTemp = nf.format(minTemp) + "°F";
        this.maxTemp = nf.format(maxTemp) + "°F";
        this.humidity = NumberFormat.getPercentInstance().format(humidity/100.0);
        this.description = description;
        this.iconURL = "http://openweathermap.org/img/w/" + iconName + ".png";
    }

    private String convertTimeStamptoDay(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp*1000);
        TimeZone tz = TimeZone.getDefault();

        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));

        SimpleDateFormat dateFormater = new SimpleDateFormat();
        return dateFormater.format(calendar.getTime());
    }
}
