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
                    return getDayName(dateInMillis);
            }
    }

    //////////////////////////////////
    //                              //
    //          getDayName          //
    //                              //
    //////////////////////////////////
    static String getDayName(long dateInMillis){
        //Get The Day
            Date date = new Date(dateInMillis);
            String Day=  new SimpleDateFormat("EEEE", Locale.FRANCE).format(date);
            String DayCapitalized = Day.substring(0,1).toUpperCase() + Day.substring(1);
            return DayCapitalized;
    }

    //////////////////////////////////
    //                              //
    //     getFormattedMonthDay     //
    //                              //
    //////////////////////////////////
    public static String getFormattedMonthDay(long dateInMillis ) {
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("dd MMMM");
        String monthDayString = monthDayFormat.format(dateInMillis);
        return monthDayString;
    }

    //////////////////////////////////
    //                              //
    //     getFormattedWind         //
    //                              //
    //////////////////////////////////
    public static String getFormattedWind(Context context, float windSpeed, float degrees) {

        int windFormat = R.string.format_wind_kmh;

        final String[] directionsText = { "N", "NE", "E", "SE", "S", "SW", "W", "NW" };
        final int DEGREES_TOTAL = 360;
        final int DIR_TOTAL = 8;

        String direction = directionsText[Math.round(degrees / (DEGREES_TOTAL / DIR_TOTAL)) % DIR_TOTAL];
        return String.format(context.getString(windFormat), windSpeed, direction);
    }

    //////////////////////////////////////////////
    //                                          //
    //      getArtResourceForWeatherCondition   //
    //                                          //
    //////////////////////////////////////////////
    public static int getArtResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }
    //////////////////////////////////////////////
    //                                          //
    //      getIconResourceForWeatherCondition  //
    //                                          //
    //////////////////////////////////////////////
    public static int getIconResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }

}