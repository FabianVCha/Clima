package Corte3.weatherviewer;

import androidx.annotation.NonNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;

public class Weather {

  public final String dayOfWeek;
  public final String minTemp;
  public final String maxTemp;
  public final String iconUrl;
  public final String description;
  public final String humidity;


    public Weather(long timestamp, double minTemp, double maxTemp, String icon, String description, double humidity) {

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(0);

        this.dayOfWeek = convertTimeStampToDayOfWeek(timestamp);
        this.minTemp = numberFormat.format(minTemp)+ "\u00B0f";
        this.maxTemp = numberFormat.format(maxTemp)+ "\u00B0f";
        this.iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
        this.description = description;
        this.humidity = numberFormat.getPercentInstance().format(humidity/100.0);
    }

    @NonNull
    public static String convertTimeStampToDayOfWeek(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000);
        TimeZone tz = TimeZone.getDefault();

        calendar.add(Calendar.MILLISECOND,
          tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        return dateFormat.format(calendar.getTime());

    }

}

