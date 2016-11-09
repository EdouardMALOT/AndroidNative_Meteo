package com.meteo.meteo;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.meteo.meteo.data.WeatherContract.WeatherEntry;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String mMeteoText;
    private ShareActionProvider mShareActionProvider;


    //For the Loader
    //-------------
        private static final int DETAIL_LOADER = 1;

        private static final String[] FORECAST_COLUMNS = {
                WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
                WeatherEntry.COLUMN_DATE,
                WeatherEntry.COLUMN_SHORT_DESC,
                WeatherEntry.COLUMN_MAX_TEMP,
                WeatherEntry.COLUMN_MIN_TEMP,
        };

        private static final int COL_WEATHER_ID = 0;
        private static final int COL_WEATHER_DATE = 1;
        private static final int COL_WEATHER_DESC = 2;
        private static final int COL_WEATHER_MAX_TEMP = 3;
        private static final int COL_WEATHER_MIN_TEMP = 4;


    //////////////////////////////////
    //                              //
    //             onCreate         //
    //                              //
    //////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

    }

    //////////////////////////////////
    //                              //
    //             Loader           //
    //                              //
    //////////////////////////////////
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(MainActivity.LOG_TAG, "In onCreateLoader");

        Intent intent = getIntent();
            if (intent == null) {
                return null;
            }

        return new CursorLoader(this, intent.getData(),FORECAST_COLUMNS,null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v(MainActivity.LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) { return; }

        boolean isMetric = Utility.isMetric(this);

        String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
        String weatherDescription =data.getString(COL_WEATHER_DESC);
        String high = Utility.formatTemperature(getApplicationContext(), data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        String low = Utility.formatTemperature(getApplicationContext(), data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        mMeteoText = String.format("%s - %s - %s/%s", dateString, weatherDescription, low, high);

        TextView detailTextView = (TextView) findViewById(R.id.text_meteo);
        detailTextView.setText(mMeteoText);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {


    }

    //////////////////////////////////
    //                              //
    //             MENU             //
    //                              //
    //////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.Menu_share :
                if(mMeteoText != null) {
                    OpenShare();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //////////////////////////////////
    //                              //
    //           OpenShare          //
    //                              //
    //////////////////////////////////
    public void OpenShare(){

        //Intent
            Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mMeteoText);
                sendIntent.setType("text/plain");

        //Start inttent
            if(sendIntent.resolveActivity(getPackageManager()) != null){     //Check if there is an App to display map
                startActivity(sendIntent);                                   //Send Action
            }else{
                Toast.makeText(this, getString(R.string.no_share_app_msp), Toast.LENGTH_LONG).show();
                Log.v(MainActivity.LOG_TAG, "No app to share this msg!");
            }
    }

}
