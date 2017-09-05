package com.dota.sohan.rubbicksforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailsActivity extends AppCompatActivity {

    ImageView im;
    TextView temp,main,desc,min,max,hum,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        temp=(TextView)findViewById(R.id.textView5);
        main=(TextView)findViewById(R.id.textView6);
        desc=(TextView)findViewById(R.id.textView7);
        max=(TextView)findViewById(R.id.textView8);
        min=(TextView)findViewById(R.id.textView9);
        hum=(TextView)findViewById(R.id.textView10);
        date=(TextView)findViewById(R.id.textView11);
        im=(ImageView)findViewById(R.id.imageView2);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null) {
            temp.setText(bundle.getString("TEMP"));
            main.setText(bundle.getString("MAIN"));
            desc.setText(bundle.getString("DESC"));
            max.setText("Minimum Temperature: "+bundle.getString("MAX"));
            min.setText("Maximum Temperature: "+bundle.getString("MIN"));
            hum.setText("Humidity: "+bundle.getString("HUM")+"%");
            date.setText(bundle.getString("DATE"));

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH");
            String formattedDate = df.format(c.getTime());
            int hr=Integer.parseInt(formattedDate);

            if (bundle.getString("MAIN").equals("Rain")) {
                if (hr>=6 && hr<=18)
                    im.setImageResource(R.drawable.rain_d);
                else
                    im.setImageResource(R.drawable.rain_n);
            }

            else if (bundle.getString("MAIN").equals("Clear")) {
                if (hr>=6 && hr<=18)
                    im.setImageResource(R.drawable.clear_sky_d);
                else
                    im.setImageResource(R.drawable.clear_sky_n);
            }

            else if (bundle.getString("MAIN").equals("Clouds")) {
                if (hr>=6 && hr<=18)
                    im.setImageResource(R.drawable.few_clouds_d);
                else
                    im.setImageResource(R.drawable.few_clouds_n);
            }
            else if (bundle.getString("MAIN").equals("Drizzle"))
                im.setImageResource(R.drawable.shower_rain);
            else if (bundle.getString("MAIN").equals("Thunderstorm"))
                im.setImageResource(R.drawable.thunderstorm);
            else if (bundle.getString("MAIN").equals("Snow"))
                im.setImageResource(R.drawable.snow);
            else if (bundle.getString("MAIN").equals("Mist"))
                im.setImageResource(R.drawable.mist);
            else
                im.setImageResource(R.drawable.clear_sky_d);
        }
    }
}