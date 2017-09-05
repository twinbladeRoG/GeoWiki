package com.dota.sohan.rubbicksforecast;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView forecastListView;
    String forecastJsonStr;
    String[][] weekForecast;
    NotificationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forecastListView=(ListView)findViewById(R.id.forecast_list);

        Intent i=new Intent(MainActivity.this,WeatherService.class);

        if (forecastJsonStr == null) {

            i.setData(getDataUrl());
            startService(i);

            Bundle bundle=getIntent().getExtras();
            if (bundle!=null) {
                forecastJsonStr=bundle.getString("DATA");
                Log.v(String.valueOf(MainActivity.class), "Forecast JSON String In Main: " + forecastJsonStr);

                try {
                    if (forecastJsonStr!=null) {
                        //Toast.makeText(getBaseContext(),"Forecast JSON is recieved!",Toast.LENGTH_SHORT).show();
                        weekForecast=getWeather(forecastJsonStr, 7);
                        ForecastListAdapter adapter = new ForecastListAdapter(weekForecast, getApplicationContext());
                        forecastListView.setAdapter(adapter);
                        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        Intent in=new Intent(MainActivity.this,MainActivity.class);
                        PendingIntent pi=PendingIntent.getActivity(getBaseContext(),0,in,0);
                        Notification n=new Notification.Builder(getBaseContext())
                                .setSmallIcon(android.R.drawable.stat_notify_missed_call)
                                .setContentIntent(pi)
                                .setContentTitle("Today's Rubbick Forecast!")
                                .setContentText(weekForecast[0][0])
                                .setSubText(weekForecast[1][0])
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .build();
                        manager.notify(0, n);
                    }
                    else
                        Toast.makeText(getBaseContext(),"No Json Data recieved!",Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                stopService(i);
            }
            else
                Toast.makeText(getBaseContext(),"No data recieved!",Toast.LENGTH_SHORT).show();
        }

        forecastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getBaseContext(),DetailsActivity.class);
                intent.putExtra("MAIN",weekForecast[0][position]);
                intent.putExtra("DESC",weekForecast[1][position]);
                intent.putExtra("TEMP",weekForecast[2][position]);
                intent.putExtra("MAX", weekForecast[3][position]);
                intent.putExtra("MIN", weekForecast[4][position]);
                intent.putExtra("HUM", weekForecast[5][position]);
                intent.putExtra("DATE",weekForecast[6][position]);
                startActivity(intent);
            }
        });
    }

    String location;String city_name="Kolkata";
    public Uri getDataUrl() {
        int numDays=7;
        final String APP_ID="29c2c5d6f02005ab9cc3e9d52988894e";

        final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
        final String ID_PARAM = "id";
        final String Q_PARAM = "q";
        final String DAYS_PARAM = "cnt";
        final String APPID_PARAM = "APPID";

        SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(this);
        city_name=settings.getString("location","Kolkata");

        //Toast.makeText(getBaseContext(),city_name,Toast.LENGTH_LONG).show();
        Uri uri=Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(Q_PARAM, city_name)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .appendQueryParameter(APPID_PARAM, APP_ID)
                .build();
        return uri;
    }

    private String[][] getWeather(String forecastJsonString, int numDays) throws JSONException {

        final String LIST = "list";
        final String WEATHER = "weather";
        final String TEMP = "temp";
        final String MIN = "min";
        final String MAX = "max";
        final String DES = "description";
        final String MAIN = "main";
        final String HUMID = "humidity";
        final String DATE = "dt";
        final String TDAY = "day";

        JSONObject forecastJson=new JSONObject(forecastJsonString);
        JSONArray weatherArray=forecastJson.getJSONArray(LIST);

        String[] result=new String[numDays];
        String[][] weatherForecastArray=new String[numDays][7];
        for (int i=0;i<weatherArray.length();i++) {

            String description;
            String main;
            String highAndLow;

            JSONObject dayForecast=weatherArray.getJSONObject(i);

            JSONObject weatherObject=dayForecast.getJSONArray(WEATHER)
                    .getJSONObject(0);
            description=weatherObject.getString(DES);
            main=weatherObject.getString(MAIN);

            JSONObject tempObject=dayForecast.getJSONObject(TEMP);
            double high=getCelcius(tempObject.getDouble(MAX));
            double low=getCelcius(tempObject.getDouble(MIN));
            double temp=getCelcius(tempObject.getDouble(TDAY));
            double humidity=dayForecast.getDouble(HUMID);
            long dateTime=dayForecast.getLong(DATE);

            String date=getReadableDate(dateTime);
            highAndLow = "\nMax. Temp. : "+high+" | Min. Temp : "+low;

            /*result[i]=  "Weather: " + main + ", " + description + "\n" +
                    "Avg. Temp: " + temp + " " + highAndLow + "\n" +
                    "Humidity: " + humidity + "\n" +
                    "Date: " + date;*/

            //Log.v(String.valueOf(MainActivity.class),"Forecast JSON String: " + result[i]);
            weatherForecastArray[0][i]=main;
            weatherForecastArray[1][i]=description;
            weatherForecastArray[2][i]=""+temp+"°";
            weatherForecastArray[3][i]=""+high+"°";
            weatherForecastArray[4][i]=""+low+"°";
            weatherForecastArray[5][i]=""+humidity;
            weatherForecastArray[6][i]=""+date;
        }
        /*for (int i=0;i<7;i++) {
            for (int j=0;j<7;j++) {
                Log.v(String.valueOf(MainActivity.class),"Forecast String Array: " + weatherForecastArray[i][j]);
            }
        }*/

        //return result;
        return weatherForecastArray;
    }

    private double getCelcius(double t) {
        Double truncatedDouble = new BigDecimal(t-273.15)
                .setScale(3, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        return truncatedDouble;
    }

    private String getReadableDate(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Intent intent1=new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.settings:
                Intent intent=new Intent(MainActivity.this,LocationPrefActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
