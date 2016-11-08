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

        // get row indices for our cursor
        int idx_max_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
        int idx_min_temp = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);
        int idx_date = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
        int idx_short_desc = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);

        String highAndLow = formatHighLows(cursor.getDouble(idx_max_temp), cursor.getDouble(idx_min_temp));

        return Utility.formatDate(cursor.getLong(idx_date)) + " - " + cursor.getString(idx_short_desc) + " - " + highAndLow;
    }


    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric)+"°C";
        return highLowStr;
    }

}