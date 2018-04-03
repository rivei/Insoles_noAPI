package com.polimi.insolesNoAPI.logic.log;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SmartphoneLogReader {
    private static final String TAG = SmartphoneLogReader.class.getSimpleName();

    private Context context;
    private static final int MIN_NUMBER_LENGTH = 10;

    public SmartphoneLogReader(Context context) {
        this.context = context;
    }

    public List<String[]> readCallByTimestamp(long ts_start) {

        Log.v(TAG, "Method readCallByTimestamp: start");

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Method readCallByTimestamp: Call Permission Required");
            return null;
        }

        String[] projection = new String[]{
                CallLog.Calls._ID,
                CallLog.Calls.DATE,   // millis
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE,
                CallLog.Calls.NUMBER
        };

        // Defines a string to contain the selection clause
        String selection = CallLog.Calls.DATE + " BETWEEN ? AND ? ";

        // Initializes an array to contain selection arguments
        String[] args = {String.valueOf(ts_start), createRightNow()};

        List<String[]> timestampSet = new ArrayList<>();

        try (
                Cursor cursor = context.getContentResolver().query(
                        CallLog.Calls.CONTENT_URI,              // The call URi to query
                        projection,                             // The column to return (projection)
                        selection,                              // The values for the WHERE clause (selection)
                        args,                                   // Selection args
                        CallLog.Calls.DATE + " DESC"            // The sort order
                )
        ) {
            if (cursor == null || cursor.getCount() <= 0) {
                Log.i(TAG, "Method readCallByTimestamp: No call found");
                return null;
            }

            while (cursor.moveToNext()) {
                // Check duration
                if(cursor.getInt(2) == 0){
                    Log.e(TAG, "Method readCallByTimestamp: call duration is 0");
                    continue;
                }

                String[] s = new String[2];
                long s0 = cursor.getLong(1);        // ts_start
                long duration = cursor.getInt(2);   // duration
                long s1 = s0 + duration;                      // ts_end
                s[0] = String.valueOf(s0);
                s[1] = String.valueOf(s1);

                timestampSet.add(s);
            }
        }

        Log.v(TAG, "Method readCallByTimestamp: end");
        return timestampSet;
    }


    // if number is valid (no advertise, phone operator), returns the last 4 digits, otherwise -1
    private int getValidNumber(String number){

        if(number.length()<MIN_NUMBER_LENGTH){
            Log.i(TAG, "Method numberValidFormat: number too short");
            return -1;
        }

        if(number.charAt(0) == '+') {
            number = number.substring(1, number.length());
        }

        int returnNumber;
        try{
            long l = Long.parseLong(number) % 10000;
            returnNumber = (int) l;

        } catch (NumberFormatException e){
            Log.i(TAG, "Method numberValidFormat: number not numerical");
            returnNumber = -1;
        }
        return returnNumber;

    }

    private boolean contactExists(String number) {

        if(number.length()<MIN_NUMBER_LENGTH){
            Log.e(TAG, "Method contactExists: number too short");
            return false;
        }

        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = {
                ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur != null && cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }


    private String createRightNow() {
        Calendar rightNow = Calendar.getInstance();

        return String.valueOf(rightNow.getTimeInMillis());
    }
}
