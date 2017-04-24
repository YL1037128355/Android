package com.example.piety.weather.Utils;

import android.content.Context;
import android.widget.Toast;

import com.example.piety.weather.Bean.WeatherBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by PIETY on 2017/4/23.
 */

public class WeatherUtils {
    public static WeatherBean getWeatherInfo(String citycode, Context context){
        WeatherBean wb = new WeatherBean();
        try{
            URL url = new URL("http://www.weather.com.cn/data/cityinfo/"+citycode+".html");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10*1000);
            if(conn.getResponseCode()==200){
                BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String result = buffer.readLine();
                JSONObject object = new JSONObject(result);
                JSONObject obj = object.getJSONObject("weatherinfo");
                wb.weather=obj.getString("weather");
                wb.temp1=obj.getString("temp1");
                wb.temp2=obj.getString("temp2");
                wb.uptime=obj.getString("ptime");
            }
        }
        catch (Exception e){
            Toast.makeText(context,"网络连接不可用！",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return wb;
    }
    public static ArrayList<String> getProvince(){
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(new Data().strCityCode);
            JSONArray array = object.getJSONArray("城市代码");
            for (int i = 0; i< array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                list.add(obj.getString("省"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public static ArrayList<String> getCity(String province){
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(new Data().strCityCode);
            JSONArray array = object.getJSONArray("城市代码");
            for(int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                if(province.equals(obj.getString("省"))){
                    JSONArray arr = obj.getJSONArray("市");
                    for(int j =0;j<arr.length();j++){
                        list.add(arr.getJSONObject(j).getString("市名"));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public static String getCityCode(String province,String city){
        String citycode = "";
        try {
            JSONObject object = new JSONObject(new Data().strCityCode);
            JSONArray array = object.getJSONArray("城市代码");
            for(int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                if(province.equals(obj.getString("省"))){
                    JSONArray arr = obj.getJSONArray("市");
                    for(int j =0;j<arr.length();j++){
                        JSONObject ob = arr.getJSONObject(j);
                        if(city.equals(ob.getString("市名"))){
                            citycode = ob.getString("编码");
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return citycode;
    }
}
