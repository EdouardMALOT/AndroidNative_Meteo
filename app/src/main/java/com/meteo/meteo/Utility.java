package com.meteo.meteo;

/**
 * Created by edouard on 08/11/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    //////////////////////////////////
    //                              //
    //     getPreferredLocation     //
    //                              //
    //////////////////////////////////
    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_location_key), context.getString(R.string.pref_location_default));
    }

    //////////////////////////////////
    //                              //
    //           isMetric           //
    //                              //
    //////////////////////////////////
    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_units_key), context.getString(R.string.pref_units_metric)).equals(context.getString(R.string.pref_units_metric));
    }

    //////////////////////////////////
    //                              //
    //      formatTemperature       //
    //                              //
    //////////////////////////////////
    static String formatTemperature(Context context,double temperature, boolean... isMetric) {

        double temp;
        if(isMetric.length > 0)
        {
            if ( !isMetric[0] ) {
                temp = 9*temperature/5+32;
            } else {
                temp = temperature;
            }
        }else{
            temp = temperature;
        }

        return context.getString(R.string.format_temperature, temp);
    }

    //////////////////////////////////
    //                              //
    //          formatDate          //
    //                              //
    //////////////////////////////////
    static String formatDate(long dateInMillis) {

        //Check if the Day is in the next week
        //------------------------------------
            if( (dateInMillis - System.currentTimeMillis()) > 6*24*3600*1000 )
            {
                //Date including mounth and year
                    Date date = new Date(dateInMillis);
                    return DateFormat.getDateInstance().format(date);
            }else{
                //Get The Day
                    Date date = new Date(dateInMillis);
                    String Day=  new SimpleDateFormat("EEEE", Locale.FRANCE).format(date);
                    String DayCapitalized = Day.substring(0,1).toUpperCase() + Day.substring(1);
                    return DayCapitalized;
            }
    }
}