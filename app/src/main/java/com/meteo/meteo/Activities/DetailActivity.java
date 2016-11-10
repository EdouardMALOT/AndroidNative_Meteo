package com.meteo.meteo.Activities;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meteo.meteo.R;
import com.meteo.meteo.Utility;
import com.meteo.meteo.data.WeatherContract;
import com.meteo.meteo.data.WeatherContract.WeatherEntry;
import com.meteo.meteo.service.ForecastService;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String mMeteoText;
    private ShareActionProvider mShareActionProvider;


    //For the Loader
    //-------------
        private static final int DETAIL_LOADER = 1;

        private static final String[] DETAIL_COLUMNS = {
                WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
                WeatherEntry.COLUMN_DATE,
                WeatherEntry.COLUMN_SHORT_DESC,
                WeatherEntry.COLUMN_MAX_TEMP,
                WeatherEntry.COLUMN_MIN_TEMP,
                WeatherEntry.COLUMN_HUMIDITY,
                WeatherEntry.COLUMN_PRESSURE,
                WeatherEntry.COLUMN_WIND_SPEED,
                WeatherEntry.COLUMN_DEGREES,
                WeatherEntry.COLUMN_WEATHER_ID,
                // This works because the WeatherProvider returns location data joined with
                // weather data, even though they're stored in two different tables.
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
        };

        // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
        // must change.
                public static final int COL_WEATHER_ID = 0;
                public static final int COL_WEATHER_DATE = 1;
                public static final int COL_WEATHER_DESC = 2;
                public static final int COL_WEATHER_MAX_TEMP = 3;
                public static final int COL_WEATHER_MIN_TEMP = 4;
                public static final int COL_WEATHER_HUMIDITY = 5;
                public static final int COL_WEATHER_PRESSURE = 6;
                public static final int COL_WEATHER_WIND_SPEED = 7;
                public static final int COL_WEATHER_DEGREES = 8;
                public static final int COL_WEATHER_CONDITION_ID = 9;

    //View references
    //---------------
            private ImageView mIconView;
            private TextView mFriendlyDateView;
            private TextView mDateView;
            private TextView mDescriptionView;
            private TextView mHighTempView;
            private TextView mLowTempView;
            private TextView mHumidityView;
            private TextView mWindView;
            private TextView mPressureView;


    //////////////////////////////////
    //                              //
    //             onCreate         //
    //                              //
    //////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        mIconView           = (ImageView)findViewById(R.id.detail_icon);
        mDateView           = (TextView) findViewById(R.id.detail_date_textview);
        mFriendlyDateView   = (TextView) findViewById(R.id.detail_day_textview);
        mDescriptionView    = (TextView) findViewById(R.id.detail_forecast_textview);
        mHighTempView       = (TextView) findViewById(R.id.detail_high_textview);
        mLowTempView        = (TextView) findViewById(R.id.detail_low_textview);
        mHumidityView       = (TextView) findViewById(R.id.detail_humidity_textview);
        mWindView           = (TextView) findViewById(R.id.detail_wind_textview);
        mPressureView       = (TextView) findViewById(R.id.detail_pressure_textview);

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

        return new CursorLoader(this, intent.getData(),DETAIL_COLUMNS,null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v(MainActivity.LOG_TAG, "In onLoadFinished");


        if (data != null && data.moveToFirst()) {

            int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);
            mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

            // Read date from cursor and update views for day of week and date
            long date = data.getLong(COL_WEATHER_DATE);
            String friendlyDateText = Utility.getDayName(date);
            String dateText = Utility.getFormattedMonthDay(date);
            mFriendlyDateView.setText(friendlyDateText);
            mDateView.setText(dateText);

            // Read description from cursor and update view
            String description = data.getString(COL_WEATHER_DESC);
            mDescriptionView.setText(description);

            // Read high temperature from cursor and update view
            boolean isMetric = Utility.isMetric(getApplicationContext());

            double high = data.getDouble(COL_WEATHER_MAX_TEMP);
            String highString = Utility.formatTemperature(getApplicationContext(), high, isMetric);
            mHighTempView.setText(highString);

            // Read low temperature from cursor and update view
            double low = data.getDouble(COL_WEATHER_MIN_TEMP);
            String lowString = Utility.formatTemperature(getApplicationContext(), low, isMetric);
            mLowTempView.setText(lowString);

            // Read humidity from cursor and update view
            float humidity = data.getFloat(COL_WEATHER_HUMIDITY);
            mHumidityView.setText(getApplicationContext().getString(R.string.format_humidity, humidity));

            // Read wind speed and direction from cursor and update view
            float windSpeedStr = data.getFloat(COL_WEATHER_WIND_SPEED);
            float windDirStr = data.getFloat(COL_WEATHER_DEGREES);
            mWindView.setText(Utility.getFormattedWind(getApplicationContext(), windSpeedStr, windDirStr));

            // Read pressure from cursor and update view
            float pressure = data.getFloat(COL_WEATHER_PRESSURE);
            mPressureView.setText(getApplicationContext().getString(R.string.format_pressure, pressure));

            // We still need this for the share intent
            mMeteoText = String.format("%s - %s - %s/%s", dateText, description, high, low);

            //Title
            //-----
                if(ForecastService.cityName != null) {
                    setTitle(ForecastService.cityName);
                }
        }

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
