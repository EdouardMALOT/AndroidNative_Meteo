package com.meteo.meteo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.meteo.meteo.data.WeatherContract;

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = MainActivity.class.getSimpleName();
    private ForecastAdapter mForecastAdapteur;

    private ProgressDialog DiagWaitMsg;

    //////////////////////////////////
    //                              //
    //             onCreate         //
    //                              //
    //////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            //Cursor Adaptor Methode
            //-----------------------
                String locationSetting = Utility.getPreferredLocation(this);

                // Sort order:  Ascending, by date.
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());

                Cursor cur = getContentResolver().query(weatherForLocationUri, null, null, null, sortOrder);

                mForecastAdapteur = new ForecastAdapter(this, cur, 0);

                 ListView listmeteo = (ListView) this.findViewById(R.id.listview_meteo);
                listmeteo.setAdapter(mForecastAdapteur);


//        // Wait Dialogue box
//            DiagWaitMsg = new ProgressDialog(this);
//                DiagWaitMsg.setIndeterminate(true);
//                DiagWaitMsg.setCancelable(false);
//                DiagWaitMsg.setMessage(getString(R.string.LoadingText));
    }


    //////////////////////////////////
    //                              //
    //             onStart          //
    //                              //
    //////////////////////////////////
    @Override
    protected void onStart() {
        super.onStart();

        //DiagWaitMsg.show();
        Refresh_weather();  //Update meteo
    }

    //////////////////////////////////
    //                              //
    //        Refresh_weather       //
    //                              //
    //////////////////////////////////
    public void Refresh_weather(){
        //METHODE 2
        //---------
            FetchWeatherTask weatherTask = new FetchWeatherTask(this);
            String location = Utility.getPreferredLocation(this);
            weatherTask.execute(location);
    }

    //////////////////////////////////
    //                              //
    //             MENU             //
    //                              //
    //////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.Menu_Params:
                    startActivity(new Intent(this, SettingsActivity.class));        //Launched settings
                return true;

            case R.id.Menu_Map :
                    OpenMap();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //////////////////////////////////
    //                              //
    //            OpenMap           //
    //                              //
    //////////////////////////////////
    public void OpenMap(){
        //Get location
            String location = Utility.getPreferredLocation(this);

        //Compute Url
            Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                                                    .appendQueryParameter("q", location)
                                                    .build();
        //Intent
            Intent GeoIntent = new Intent(Intent.ACTION_VIEW);
                GeoIntent.setData(geoLocation);

            if(GeoIntent.resolveActivity(getPackageManager()) != null){     //Check if there is an App to display map
                startActivity(GeoIntent);                                   //Send Action
            }else{
                Toast.makeText(this, getString(R.string.no_map_app_msp), Toast.LENGTH_LONG).show();
                Log.v(LOG_TAG, "No Map editor !!");
            }
    }

}
