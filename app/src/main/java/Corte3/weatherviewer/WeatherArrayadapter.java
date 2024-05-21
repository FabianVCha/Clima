package Corte3.weatherviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WeatherArrayadapter extends ArrayAdapter<Weather> {

  private static class Viewholder{
    ImageView conditionImageView;
    TextView dayTextView;
    TextView lowTextView;
    TextView hitextView;
    TextView humiditytextview;
  }

  private Map<String, Bitmap> bitmaps = new HashMap<>();

  public WeatherArrayadapter(Context context, List<Weather> weather) {
    super(context, -1, weather);
  }

  @SuppressLint("StringFormatInvalid")
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    Weather weather = getItem(position);
    Viewholder viewholder;

    if (convertView == null) {
      viewholder = new Viewholder();
      LayoutInflater inflater = LayoutInflater.from(getContext());
      convertView = inflater.inflate(R.layout.list_view, parent, false);
      viewholder.conditionImageView = (ImageView) convertView.findViewById((R.id.conditionImageView));
      viewholder.dayTextView = (TextView) convertView.findViewById(R.id.dayTextView);
      viewholder.lowTextView = (TextView) convertView.findViewById(R.id.lowTextView);
      viewholder.hitextView = (TextView) convertView.findViewById(R.id.hiTextView);
      viewholder.humiditytextview = (TextView) convertView.findViewById(R.id.humidityTextView);
      convertView.setTag(viewholder);

    }else{
      viewholder = (Viewholder) convertView.getTag();
    }



    if (bitmaps.containsKey(weather.iconUrl)) {
      viewholder.conditionImageView.setImageBitmap(bitmaps.get(weather.iconUrl));
    } else {
      new LoadImageTask(viewholder.conditionImageView).execute(weather.iconUrl);
    }

    Context context = getContext();
    viewholder.dayTextView.setText(context.getString(R.string.Descripcion_dia, weather.dayOfWeek, weather.description));
    viewholder.lowTextView.setText(context.getString(R.string.baja_temp, weather.minTemp));
    viewholder.hitextView.setText(context.getString(R.string.alta_temp, weather.maxTemp));
    viewholder.humiditytextview.setText(context.getString(R.string.Humedad, weather.humidity));

    return convertView;
  }

  private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView; // displays the thumbnail

    // store ImageView on which to set the downloaded Bitmap
    public LoadImageTask(ImageView imageView) {
      this.imageView = imageView;
    }

    // load image; params[0] is the String URL representing the image
    @Override
    protected Bitmap doInBackground(String... params) {
      Bitmap bitmap = null;
      HttpURLConnection connection = null;

      try {
        URL url = new URL(params[0]); // create URL for image

        // open an HttpURLConnection, get its InputStream
        // and download the image
        connection = (HttpURLConnection) url.openConnection();

        try (InputStream inputStream = connection.getInputStream()) {
          bitmap = BitmapFactory.decodeStream(inputStream);
          bitmaps.put(params[0], bitmap); // cache for later use
        } catch (Exception e) {
          e.printStackTrace();
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        connection.disconnect(); // close the HttpURLConnection
      }

      return bitmap;
    }
  }
}










