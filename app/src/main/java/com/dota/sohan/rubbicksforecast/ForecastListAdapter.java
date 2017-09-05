package com.dota.sohan.rubbicksforecast;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ForecastListAdapter extends BaseAdapter {

    String[][] weekForecast;
    String[] mainWeather,shortDesc,dayTemp,maxTemp,minTemp,humidity,date;
    Context context;
    LayoutInflater inflater;

    public ForecastListAdapter(String[][] weekForecast, Context context) {
        this.weekForecast=weekForecast;
        /*for (int i=0;i<7;i++) {
            for (int j=0;j<7;j++) {
                Log.v(String.valueOf(MainActivity.class), "Forecast String Array: " + weekForecast[i][j]);
            }
        }*/
        mainWeather=this.weekForecast[0];
        for (int i=0;i<7;i++)
            Log.v(String.valueOf(MainActivity.class), "Forecast String Array: " + mainWeather[i]);
        shortDesc=this.weekForecast[1];
        dayTemp=this.weekForecast[2];
        maxTemp=this.weekForecast[3];
        minTemp=this.weekForecast[4];
        humidity=this.weekForecast[5];
        date=this.weekForecast[6];
        this.context=context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inflater.inflate(R.layout.list_hybrid_layout,null);
        TextView tv_main=(TextView)convertView.findViewById(R.id.textView);
        TextView tv_desc=(TextView)convertView.findViewById(R.id.textView2);
        TextView tv_temp=(TextView)convertView.findViewById(R.id.textView4);
        TextView tv_minm= (TextView)convertView.findViewById(R.id.textView3);
        ImageView im=(ImageView)convertView.findViewById(R.id.imageView);


        tv_main.setText(date[position]);
        tv_desc.setText(mainWeather[position]+", "+shortDesc[position]);
        tv_temp.setText(dayTemp[position]);
        tv_minm.setText(minTemp[position] + "\n" + maxTemp[position]);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String formattedDate = df.format(c.getTime());
        int hr=Integer.parseInt(formattedDate);

        if (mainWeather[position].equals("Rain")) {
            if (hr>=6 && hr<=18)
                im.setImageResource(R.drawable.rain_d);
            else
                im.setImageResource(R.drawable.rain_n);
        }

        else if (mainWeather[position].equals("Clear")) {
            if (hr>=6 && hr<=18)
                im.setImageResource(R.drawable.clear_sky_d);
            else
                im.setImageResource(R.drawable.clear_sky_n);
        }

        else if (mainWeather[position].equals("Clouds")) {
            if (hr>=6 && hr<=18)
                im.setImageResource(R.drawable.few_clouds_d);
            else
                im.setImageResource(R.drawable.few_clouds_n);
        }
        else if (mainWeather[position].equals("Drizzle"))
            im.setImageResource(R.drawable.shower_rain);
        else if (mainWeather[position].equals("Thunderstorm"))
            im.setImageResource(R.drawable.thunderstorm);
        else if (mainWeather[position].equals("Snow"))
            im.setImageResource(R.drawable.snow);
        else if (mainWeather[position].equals("Mist"))
            im.setImageResource(R.drawable.mist);
        else
            im.setImageResource(R.drawable.clear_sky_d);

        return convertView;
    }
}
