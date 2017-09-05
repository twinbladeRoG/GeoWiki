package com.dota.sohan.rubbicksforecast;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Sohan on 7/25/2016.
 */
public class WeatherService extends IntentService {

    public WeatherService() {
        super("WeatherService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String weatherD=fetchWeather(intent.getDataString());

        if (weatherD!=null) {
            /*for (String s : weatherD) {
                Log.v(String.valueOf(MainActivity.class), "Forecast Entry : " + s);
            }*/
            Intent i = new Intent(WeatherService.this, MainActivity.class);
            i.putExtra("DATA", weatherD);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            stopSelf();
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    //String location="1275004";
    public String fetchWeather(String dataUrl) {
        HttpURLConnection urlConnection=null;
        BufferedReader reader=null;
        String forecastJsonString=null;

        int numDays=7;
        final String APP_ID="29c2c5d6f02005ab9cc3e9d52988894e";

        try {
            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String ID_PARAM = "id";
            final String DAYS_PARAM = "cnt";
            final String APPID_PARAM = "APPID";

            /*Uri uri=Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(ID_PARAM, location)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                    .appendQueryParameter(APPID_PARAM, APP_ID)
                    .build();*/
            Log.v(String.valueOf(MainActivity.class), "RETURNED 0");
            Log.v(String.valueOf(MainActivity.class), "URL "+dataUrl);

            //dataUrl="http://api.openweathermap.org/data/2.5/forecast/daily?id=6058560&cnt=7&APPID=29c2c5d6f02005ab9cc3e9d52988894e";
            URL url=new URL(dataUrl);
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream=urlConnection.getInputStream();
            StringBuffer buffer=new StringBuffer();
            if (inputStream ==null) {
                Log.v(String.valueOf(MainActivity.class),"RETURNED 1");
                return null;
            }
            reader=new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
            }

            if (buffer.length() ==0) {
                Log.v(String.valueOf(MainActivity.class),"RETURNED 2");
                return null;
            }
            forecastJsonString=buffer.toString();
            //Log.v(String.valueOf(MainActivity.class),"RETURNED 3");
            Log.v(String.valueOf(MainActivity.class),"Forecast JSON String: " + forecastJsonString);
            return forecastJsonString;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (urlConnection!=null)
                urlConnection.disconnect();
            if (reader!=null) {
                try {
                    reader.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
