package com.example.piety.weather;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piety.weather.Bean.WeatherBean;
import com.example.piety.weather.Utils.Data;
import com.example.piety.weather.Utils.WeatherUtils;

import org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private Context mContext;
    private Spinner sp_province,sp_city;
    private TextView tv_locweather,tv_loctemp,tv_weather,tv_temp,tv_ptime;
    private ArrayAdapter<String> p_adapter,c_adapter;
    private Handler mHandler;
    private ArrayList<String> p_list,c_list;
    private String province,city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;

        sp_province=(Spinner)findViewById(R.id.spinner1);
        sp_city=(Spinner)findViewById(R.id.spinner2);

        tv_locweather=(TextView)findViewById(R.id.locweather);
        tv_loctemp=(TextView)findViewById(R.id.loctemp);

        tv_weather=(TextView)findViewById(R.id.weather);
        tv_temp=(TextView)findViewById(R.id.temp);
        tv_ptime=(TextView)findViewById(R.id.ptime);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final WeatherBean wb = WeatherUtils.getWeatherInfo("101250801",mContext);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_locweather.setText(wb.weather);
                        tv_loctemp.setText(wb.temp1+"到"+wb.temp2);
                    }
                });
            }
        }).start();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                WeatherBean wb = (WeatherBean) msg.obj;
                tv_weather.setText(wb.weather);
                tv_temp.setText(wb.temp1+"到"+wb.temp2);
                tv_ptime.setText(wb.uptime);
            }
        };
        p_list = new ArrayList<>();
        p_list=WeatherUtils.getProvince();
        p_adapter = new ArrayAdapter(mContext,R.layout.support_simple_spinner_dropdown_item,p_list );
        sp_province.setAdapter(p_adapter);
        sp_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                province = parent.getItemAtPosition(position).toString();
                c_list=new ArrayList<>();
                c_list=WeatherUtils.getCity(province);
                c_adapter = new ArrayAdapter<>(mContext,R.layout.support_simple_spinner_dropdown_item,c_list);
                sp_city.setAdapter(c_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position).toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        msg.obj=WeatherUtils.getWeatherInfo(WeatherUtils.getCityCode(province,city),mContext);
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}