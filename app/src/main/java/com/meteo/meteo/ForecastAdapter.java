package com.meteo.meteo;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meteo.meteo.Activities.MainActivity;

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
        View view;
        int viewtype = getItemViewType(cursor.getPosition());

        //Check for the layout
        //-------------------
            switch (viewtype) {
                case TODAY_VIEW:
                    view = LayoutInflater.from(context).inflate(R.layout.list_item_meteo_today, parent, false);
                    break;
                default:
                    view = LayoutInflater.from(context).inflate(R.layout.list_item_meteo, parent, false);
                    break;
            }

        //View Holder
        //-----------
            view.setTag(new ViewHolder(view));

        return view;
    }
    //////////////////////////////////
    //                              //
    //           bindView           //
    //                              //
    //////////////////////////////////
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Restore references to the view
        //-------------------------------
            ViewHolder viewholder = (ViewHolder) view.getTag();

        //Icon
        //----
            switch (getItemViewType(cursor.getPosition())) {
                case TODAY_VIEW:
                        // Get weather icon
                        viewholder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(cursor.getInt(MainActivity.COL_WEATHER_CONDITION_ID)));
                    break;
                default:
                        // Get weather icon
                        viewholder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(cursor.getInt(MainActivity.COL_WEATHER_CONDITION_ID)));
                    break;
            }

        //Date & Description
        //------------------
            viewholder.dateView.setText(Utility.formatDate(cursor.getLong(MainActivity.COL_WEATHER_DATE)));
            viewholder.descriptionView.setText(cursor.getString(MainActivity.COL_WEATHER_DESC));

        //Températures
        //------------
            viewholder.highTempView.setText(Utility.formatTemperature(context,cursor.getDouble(MainActivity.COL_WEATHER_MAX_TEMP)));
            viewholder.lowTempView.setText(Utility.formatTemperature(context,cursor.getDouble(MainActivity.COL_WEATHER_MIN_TEMP)));
    }

    //////////////////////////////////
    //                              //
    //            ViewType          //
    //                              //
    //////////////////////////////////
    private static final int TODAY_VIEW = 0;
    private static final int STANDARD_VIEW = 1;

    @Override
    public int getViewTypeCount() {
        return 2;                           //We now have 2 possible layout
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TODAY_VIEW;
        } else {
            return STANDARD_VIEW;
        }
    }

    //////////////////////////////////
    //                              //
    //       ViewHolder Class       //
    //                              //
    //////////////////////////////////
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }

}
