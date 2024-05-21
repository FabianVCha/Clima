package Corte3.weatherviewer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Weather> weatherList = new ArrayList<>();

    private WeatherArrayadapter weatherArrayadapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        listView = (android.widget.ListView) findViewById(R.id.CLIMA);
        weatherArrayadapter = new WeatherArrayadapter(this, weatherList);
        listView.setAdapter(weatherArrayadapter);

      Button busqueda = (Button) findViewById(R.id.BUSQUEDA);
      busqueda.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          EditText Ciudad = (EditText) findViewById(R.id.ciudad);
          URL url = createUrl(Ciudad.getText().toString());
          if (url == null) {
            dismissKeyboard(Ciudad);
          }

        }
      });

    }
  private void dismissKeyboard(View view) {
    InputMethodManager imm = (InputMethodManager) getSystemService(
      Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  private URL createUrl(String city) {
      String apiKey = getString(R.string.api_key);
      String baseUrl = getString(R.string.web_service_url);

      try {
        // create URL for specified city and imperial units (Fahrenheit)
        String urlString = baseUrl + URLEncoder.encode(city, "UTF-8") +
          "&units=imperial&cnt=16&APPID=" + apiKey;
        return new URL(urlString);
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      return null; // URL was malformed
  }

  private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject>{
    @Override
    protected JSONObject doInBackground(URL... params) {
      HttpURLConnection connection = null;
      try{
        connection = (HttpURLConnection) params[0].openConnection();
        int response = connection.getResponseCode();

        if(response == HttpURLConnection.HTTP_OK){
          StringBuilder builder = new StringBuilder();
          try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            String line;
            while((line = reader.readLine()) != null){
              builder.append(line);
            }

          }catch (IOException e){

          }
        }
      } catch (IOException e) {
          throw new RuntimeException(e);
      }

        return null;
    }
  }
}

