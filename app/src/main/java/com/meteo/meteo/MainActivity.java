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

    //private ArrayAdapter MeteoAdapteur;
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


        //METHODE 1
        //---------
                //Create the datas
                //----------------
                    //List<String> meteo_list = new ArrayList<String>();

                //Create adapteur
                    //MeteoAdapteur = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_meteo, R.id.list_item_meteo_text_view, meteo_list);


                //Link the listview with the adapter
                    //ListView listmeteo = (ListView) this.findViewById(R.id.listview_meteo);
                    //listmeteo.setAdapter(MeteoAdapteur);

                //Click listener
                    //listmeteo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    //    @Override
                    //    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //        //Toast.makeText(getApplicationContext(),"You click on row n°"+i+1, Toast.LENGTH_SHORT).show();
                    //        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    //            intent.putExtra(intent.EXTRA_TEXT,MeteoAdapteur.getItem(i).toString());
                    //        startActivity(intent);
                    //    }
                    //});

        //Methode 2
        //---------

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

        //METHODE 1
        //---------
            // Get Value
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            //String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

            // Download Weather info
            //    new DownloadClass(location, this);              //It will call  DownloadClassCallback when download is finished

        //METHODE 2
        //---------
            FetchWeatherTask weatherTask = new FetchWeatherTask(this);
            String location = Utility.getPreferredLocation(this);
            weatherTask.execute(location);
    }

    //////////////////////////////////
    //                              //
    //     DownloadClassCallback    //
    //                              //
    //////////////////////////////////
//    @Override
//    public void DownloadClassCallback(List<String> list, String CityName) {
//
//        //Update list
//            MeteoAdapteur.clear();
//            for(int i=0; i < list.size(); i++)
//            {
//                MeteoAdapteur.add(list.get(i));
//            }
//
//        //Update City name
//            setTitle(getString(R.string.app_name) + " : " +CityName);
//
//        DiagWaitMsg.dismiss();
//    }



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
        //Get Value
            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            //String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

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
