package com.meteo.meteo;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by edouard on 04/11/16.
 */

public class DownloadClass extends AsyncTask<String, Void , List<String>> {

    private  DownloadClassCallback CallbackTask = null;


    final String BASE_ADR = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private String CityName;


    //////////////////////////////////
    //                              //
    //          Constructor         //
    //                              //
    //////////////////////////////////
    public DownloadClass(String zipcode, DownloadClassCallback callbackTask) {

        CallbackTask = callbackTask;
        execute(zipcode);
    }

    //////////////////////////////////
    //                              //
    //          doInBackground      //
    //                              //
    //////////////////////////////////
    @Override
    protected List<String> doInBackground(String... zipcode) {

        try {

            //Compute URL
            //-----------
                String zip_code = "75001";
                if(zipcode.length > 0){
                    zip_code = zipcode[0];
                }

                //"http://api.openweathermap.org/data/2.5/forecast/daily?appid=3d1f6baa785b265b02c7df7784a625e5&q=Marseille,fr&units=metric&cnt=15"
                Uri builturi = Uri.parse(BASE_ADR).buildUpon().appendQueryParameter("appid","3d1f6baa785b265b02c7df7784a625e5" )
                                                                .appendQueryParameter("units", "metric")
                                                                .appendQueryParameter("cnt","15")
                                                                .appendQueryParameter("zip", zip_code+",FR")
                                    .build();

                Log.v(MainActivity.LOG_TAG, "Start download meteo file !");

            //Http request
            //------------
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                                                .url(builturi.toString())
                                                .build();

                Response response = client.newCall(request).execute();

                Log.v(MainActivity.LOG_TAG, "Meteo file downloaded !");

            //Decode if OK
            //-------------
                if(!response.isSuccessful()) {
                    return null;
                }else {
                    //JSON Conversion
                    //---------------
                        try {
                            List<String> list = new ArrayList<String>();

                            JSONObject json_datas = new JSONObject(response.body().string());
                            JSONArray json_meteo_tab = json_datas.getJSONArray("list");

                            CityName =  json_datas.getJSONObject("city").getString("name");

                            for (int i = 0; i < json_meteo_tab.length(); i++) {
                                Double TemperatureMoy = json_meteo_tab.getJSONObject(i).getJSONObject("temp").getDouble("day");
                                Double TemperatureMin = json_meteo_tab.getJSONObject(i).getJSONObject("temp").getDouble("min");
                                Double TemperatureMax = json_meteo_tab.getJSONObject(i).getJSONObject("temp").getDouble("max");

                                String MainWeather = json_meteo_tab.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");

                                //Convert to String
                                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                                    String StringMeteo = getDay(i) + " : " + decimalFormat.format(TemperatureMoy) + "°C - " + MainWeather +"   (" + decimalFormat.format(TemperatureMin) + "/" + decimalFormat.format(TemperatureMax)+"°C)";

                                list.add(StringMeteo);
                                Log.v(MainActivity.LOG_TAG, StringMeteo);
                            }


                            return list;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //////////////////////////////////
    //                              //
    //          onPostExecute       //
    //                              //
    //////////////////////////////////
    protected void onPostExecute(List<String> result) {
        if(result != null){
          //  CallbackTask.DownloadClassCallback(result, CityName);
        }
    }


    //////////////////////////////////
    //                              //
    //             GetDay           //
    //                              //
    //////////////////////////////////
    public String getDay(int TodayPlus){
        String Day=  new SimpleDateFormat("EEEE", Locale.FRANCE).format(System.currentTimeMillis() + ((long)(TodayPlus))*1000*3600*24);
            String DayCapitalized = Day.substring(0,1).toUpperCase() + Day.substring(1);
        return DayCapitalized;

    }

}
