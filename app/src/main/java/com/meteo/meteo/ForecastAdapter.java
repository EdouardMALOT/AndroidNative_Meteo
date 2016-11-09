package com.meteo.meteo;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meteo.meteo.data.WeatherContract;

/**
 * Created by edouard on 08/11/16.
 */


public class ForecastAdapter extends CursorAdapter {

    //////////////////////////////////
    //                              //
    //          Constructor         //
    //                              //
    //////////////////////////////////
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    //////////////////////////////////
    //                              //
    //            newView           //
    //                              //
    //////////////////////////////////
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_meteo, parent, false);
        return view;
    }

    //////////////////////////////////
    //                              //
    //           bindView           //
    //                              //
    //////////////////////////////////
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView)view;
        tv.setText(convertCursorRowToUXFormat(cursor));
    }

    //////////////////////////////////
    //                              //
    //  convertCursorRowToUXFormat  //
    //                              //
    //////////////////////////////////
    private String convertCursorRowToUXFormat(Cursor cursor) {

        String highAndLow = formatHighLows(cursor.getDouble(MainActivity.COL_WEATHER_MAX_TEMP), cursor.getDouble(MainActivity.COL_WEATHER_MIN_TEMP));
        return Utility.formatDate(cursor.getLong(MainActivity.COL_WEATHER_DATE)) + " - " + cursor.getString(MainActivity.COL_WEATHER_DESC) + " - " + highAndLow;

    }

    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        //String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric)+"°C";
        String highLowStr = Utility.formatTemperature(low, isMetric) + "/" + Utility.formatTemperature(high, isMetric)+"°C";
        return highLowStr;
    }

}