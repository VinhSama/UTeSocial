package com.utesocial.android.core.data.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateDeserializer implements JsonDeserializer<Date> {
    private static String TAG = DateDeserializer.class.getSimpleName();
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Date result = null;
        String date = json.getAsString();
        if (!date.isEmpty()) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            try {
                result = formatter.parse(date);
            } catch (ParseException e) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat anotherFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    result = anotherFormatter.parse(date);
                } catch (ParseException ex) {
                    Log.i(TAG, "deserialize:" + ex.getMessage());
                }
            }
        }
        if(result == null) {
            Log.i(TAG, "deserialize:" + "result == null");
            try {
                long timeInMillis = json.getAsLong();
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
                calendar.setTimeInMillis(timeInMillis);
                result = calendar.getTime();
            } catch (ClassCastException e) {
                Log.i(TAG, "deserialize: can't convert timeInMillis to Date.class");
                return null;
            }
        }
        return result;
    }
}
