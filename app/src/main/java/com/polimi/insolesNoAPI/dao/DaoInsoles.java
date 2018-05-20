package com.polimi.insolesNoAPI.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.polimi.insolesNoAPI.model.insoles.Insoles;
import com.polimi.insolesNoAPI.model.insoles.InsolesRawData;
import com.polimi.insolesNoAPI.model.insoles.InsolesRawHeader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DaoInsoles extends Dao {
    private static final String TAG = DaoInsoles.class.getSimpleName();

    //private static final int ALL_DATA_BYTE = 101777407;
    private static final int LAST_BIT_POSITION = 26;
    private static final String ALL_NULL = ",,,,,,,,,,,,,,,,,,";

    public DaoInsoles(Context context) {
        super(context);
    }

    /* RAW DATA*/

    // WRITE FUNCTIONS
    public long storeRawHeader(InsolesRawHeader rawHeader) throws Exception {
        Log.v(TAG, "Method storeRawHeader: start");

        if (rawHeader == null) {
            Log.d(TAG, "Method storeRawHeader: null Object received");
            return -1;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.InsolesRawHeaderTable.DATE_COL1, rawHeader.getSessionDate());
        values.put(DatabaseContract.InsolesRawHeaderTable.SAMPLE_RATE_COL2, rawHeader.getSampleRate());
        values.put(DatabaseContract.InsolesRawHeaderTable.INS_LEFT_ID_COL3, rawHeader.getLeftID());
        values.put(DatabaseContract.InsolesRawHeaderTable.INS_LEFT_SENS_COL4, rawHeader.getLeftSensor());
        values.put(DatabaseContract.InsolesRawHeaderTable.INS_RIGHT_ID_COL5, rawHeader.getRightID());
        values.put(DatabaseContract.InsolesRawHeaderTable.INS_RIGHT_SENS_COL6, rawHeader.getRightSensor());

        long newRowId = db.insert(
                DatabaseContract.InsolesRawHeaderTable.TABLE_NAME,
                null,
                values);

        if (newRowId == -1) {
            throw new Exception("DBInsertException");
        }

        Log.v(TAG, "Method storeRawHeader: end");

        return newRowId;
    }

    public void storeRawData(InsolesRawData raw) throws Exception {
        Log.v(TAG, "Method storeRawData: start");

        if (raw == null) {
            Log.d(TAG, "Method storeRawData: null Object received");
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.InsolesRawDataTable.TIMESTAMP_COL1, raw.getTimestamp());
        values.put(DatabaseContract.InsolesRawDataTable.MSG_DEF_L_COL2, raw.getMsgDefinitionLeft());
        values.put(DatabaseContract.InsolesRawDataTable.MSG_DEF_R_COL3, raw.getMsgDefinitionRight());
        values.put(DatabaseContract.InsolesRawDataTable.DATA_L_COL4, raw.getDataLeft());
        values.put(DatabaseContract.InsolesRawDataTable.DATA_R_COL5, raw.getDataRight());
        values.put(DatabaseContract.InsolesRawDataTable.SESSION_COL6, raw.getSessionID());

        long newRowId = db.insert(
                DatabaseContract.InsolesRawDataTable.TABLE_NAME,
                null,
                values);

        if (newRowId == -1) {
            throw new Exception("DBInsertException");
        }

        Log.v(TAG, "Method storeRawData: end");

    }

    public void storeInsolesData(Insoles ins) throws Exception {
        Log.v(TAG, "Method storeInsolesData: start");

        if (ins == null) {
            Log.d(TAG, "Method storeInsolesData: null Object received");
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.InsolesTable.DATE_COL1, ins.getTimestamp());

        if(ins.getDataLeft() != null){
            values.put(DatabaseContract.InsolesTable.ACCEL_X_L_COL2, (Double) ins.getDataLeft().get(0));
            values.put(DatabaseContract.InsolesTable.ACCEL_Y_L_COL3, (Double) ins.getDataLeft().get(1));
            values.put(DatabaseContract.InsolesTable.ACCEL_Z_L_COL4, (Double) ins.getDataLeft().get(2));
            values.put(DatabaseContract.InsolesTable.PRESS_0_L_COL5, (Double) ins.getDataLeft().get(3));
            values.put(DatabaseContract.InsolesTable.PRESS_1_L_COL6, (Double) ins.getDataLeft().get(4));
            values.put(DatabaseContract.InsolesTable.PRESS_2_L_COL7, (Double) ins.getDataLeft().get(5));
            values.put(DatabaseContract.InsolesTable.PRESS_3_L_COL8, (Double) ins.getDataLeft().get(6));
            values.put(DatabaseContract.InsolesTable.PRESS_4_L_COL9, (Double) ins.getDataLeft().get(7));
            values.put(DatabaseContract.InsolesTable.PRESS_5_L_COL10, (Double) ins.getDataLeft().get(8));
            values.put(DatabaseContract.InsolesTable.PRESS_6_L_COL11, (Double) ins.getDataLeft().get(9));
            values.put(DatabaseContract.InsolesTable.PRESS_7_L_COL12, (Double) ins.getDataLeft().get(10));
            values.put(DatabaseContract.InsolesTable.PRESS_8_L_COL13, (Double) ins.getDataLeft().get(11));
            values.put(DatabaseContract.InsolesTable.PRESS_9_L_COL14, (Double) ins.getDataLeft().get(12));
            values.put(DatabaseContract.InsolesTable.PRESS_10_L_COL15, (Double) ins.getDataLeft().get(13));
            values.put(DatabaseContract.InsolesTable.PRESS_11_L_COL16, (Double) ins.getDataLeft().get(14));
            values.put(DatabaseContract.InsolesTable.PRESS_12_L_COL17, (Double) ins.getDataLeft().get(15));
            values.put(DatabaseContract.InsolesTable.TOT_FORCE_L_COL18, (Double) ins.getDataLeft().get(16));
            values.put(DatabaseContract.InsolesTable.COP_X_L_COL19, (Double) ins.getDataLeft().get(17));
            values.put(DatabaseContract.InsolesTable.COP_Y_L_COL20, (Double) ins.getDataLeft().get(18));
        }

        if(ins.getDataRight() != null){
            values.put(DatabaseContract.InsolesTable.ACCEL_X_R_COL21, (Double) ins.getDataRight().get(0));
            values.put(DatabaseContract.InsolesTable.ACCEL_Y_R_COL22, (Double) ins.getDataRight().get(1));
            values.put(DatabaseContract.InsolesTable.ACCEL_Z_R_COL23, (Double) ins.getDataRight().get(2));
            values.put(DatabaseContract.InsolesTable.PRESS_0_R_COL24, (Double) ins.getDataRight().get(3));
            values.put(DatabaseContract.InsolesTable.PRESS_1_R_COL25, (Double) ins.getDataRight().get(4));
            values.put(DatabaseContract.InsolesTable.PRESS_2_R_COL26, (Double) ins.getDataRight().get(5));
            values.put(DatabaseContract.InsolesTable.PRESS_3_R_COL27, (Double) ins.getDataRight().get(6));
            values.put(DatabaseContract.InsolesTable.PRESS_4_R_COL28, (Double) ins.getDataRight().get(7));
            values.put(DatabaseContract.InsolesTable.PRESS_5_R_COL29, (Double) ins.getDataRight().get(8));
            values.put(DatabaseContract.InsolesTable.PRESS_6_R_COL30, (Double) ins.getDataRight().get(9));
            values.put(DatabaseContract.InsolesTable.PRESS_7_R_COL31, (Double) ins.getDataRight().get(10));
            values.put(DatabaseContract.InsolesTable.PRESS_8_R_COL32, (Double) ins.getDataRight().get(11));
            values.put(DatabaseContract.InsolesTable.PRESS_9_R_COL33, (Double) ins.getDataRight().get(12));
            values.put(DatabaseContract.InsolesTable.PRESS_10_R_COL34, (Double) ins.getDataRight().get(13));
            values.put(DatabaseContract.InsolesTable.PRESS_11_R_COL35, (Double) ins.getDataRight().get(14));
            values.put(DatabaseContract.InsolesTable.PRESS_12_R_COL36, (Double) ins.getDataRight().get(15));
            values.put(DatabaseContract.InsolesTable.TOT_FORCE_R_COL37, (Double) ins.getDataRight().get(16));
            values.put(DatabaseContract.InsolesTable.COP_X_R_COL38, (Double) ins.getDataRight().get(17));
            values.put(DatabaseContract.InsolesTable.COP_Y_R_COL39, (Double) ins.getDataRight().get(18));
        }

        long newRowId = db.insert(
                DatabaseContract.InsolesTable.TABLE_NAME,
                null,
                values);

        if (newRowId == -1) {
            throw new Exception("DBInsertException");
        }

        //Log.e("****", "Insoles STORED");
        Log.v(TAG, "Method storeInsolesData: end");

    }


    // READ FUNCTIONS

    public String retrieveRawHeader(long sessionID){
        Log.v(TAG, "Method retrieveRawHeader: start");

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "_id = ? ";
        String[] selectionArgs = {String.valueOf(sessionID)};

        InsolesRawHeader header;

        try (
                Cursor cursor = db.query(
                        DatabaseContract.InsolesRawHeaderTable.TABLE_NAME,      // The table to query
                        null,                                                   // The columns to return
                        selection,                                              // The columns for the WHERE clause
                        selectionArgs,                                          // The values for the WHERE clause
                        null,                                                   // don't group the rows
                        null,                                                   // don't filter by row groups
                        null                                                    // The sort order
                )
        ) {
            if (cursor == null || cursor.getCount() <= 0) {
                Log.d(TAG, "Method retrieveRawHeader: No insoles data found by query");
                return null;
            }

            cursor.moveToNext();
            header = new InsolesRawHeader(
                    cursor.getLong(1),      // session data
                    cursor.getDouble(2),    // sample rate
                    cursor.getInt(3),       // left ID
                    cursor.getInt(4),       // left sensor
                    cursor.getInt(5),       // right ID
                    cursor.getInt(6)        // right sensor
            );
        }

        Log.v(TAG, "Method retrieveRawHeader: end");

        return header.toString();
    }

    public List<String> retrieveRawData(long sessionID){
        Log.e(TAG, "Method retrieveRawData: start");

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = DatabaseContract.InsolesRawDataTable.SESSION_COL6 + " = ?";
        String[] selectionArgs = { String.valueOf(sessionID) };
        String orderBy = DatabaseContract.InsolesRawDataTable.TIMESTAMP_COL1 + " ASC";

        List<String> dataList = new ArrayList<>();

        try (
                Cursor cursor = db.query(
                        DatabaseContract.InsolesRawDataTable.TABLE_NAME,      // The table to query
                        null,                                                 // The columns to return
                        selection,                                            // The columns for the WHERE clause
                        selectionArgs,                                        // The values for the WHERE clause
                        null,                                                 // don't group the rows
                        null,                                                 // don't filter by row groups
                        orderBy                                               // The sort order
                )
        ) {
            if (cursor == null || cursor.getCount() <= 0) {
                Log.d(TAG, "Method retrieveRawData: No insoles data found by query");
                return null;
            }

            Log.e(TAG, "Reading data..");
            int count = 0;
            while (cursor.moveToNext()) {
                System.out.print((count==40)? "\n" : ".");
                count++;
                int defLeft = cursor.getInt(2);
                int defRight = cursor.getInt(3);
                String dataLeft = cursor.getString(4);
                String dataRight = cursor.getString(5);
                //String dataLeft = (defLeft == 0) ? "" : cursor.getString(4).substring(1, 18);
                //String dataRight = (defRight == 0) ? "" : cursor.getString(5).substring(1, 18);

                //String dataLeft = populateDataArray(defLeft, cursor.getString(4));
                //String dataRight = populateDataArray(defRight, cursor.getString(5));

                dataLeft = (defLeft == 0) ? ALL_NULL : dataLeft.substring(1, dataLeft.length()-1);
                dataRight = (defRight == 0) ? ALL_NULL : dataRight.substring(1, dataRight.length()-1);

                InsolesRawData rawData = new InsolesRawData(
                        cursor.getLong(1),
                        defLeft,
                        defRight,
                        dataLeft,
                        dataRight,
                        null
                );
                dataList.add(rawData.toString());
            }
        }

        Log.e(TAG, "Method retrieveRawData: end");
        return dataList;
    }


    public List<InsolesRawHeader> retrieveAllRawHeaders(long sessionID){
        Log.v(TAG, "Method retrieveAllRawHeaders: start");

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "date BETWEEN ? AND ?";
        long dateMillis = Calendar.getInstance().getTimeInMillis();

        String[] selectionArgs = {
                getSpecificDayMidnight(dateMillis),
                fromMillisToString(dateMillis)
        };

        List<InsolesRawHeader> headerList = new ArrayList<>();

        try (
                Cursor cursor = db.query(
                        DatabaseContract.InsolesRawHeaderTable.TABLE_NAME,      // The table to query
                        null,                                          // The columns to return
                        selection,                                     // The columns for the WHERE clause
                        selectionArgs,//selectionArgs,                 // The values for the WHERE clause
                        null,                                          // don't group the rows
                        null,                                          // don't filter by row groups
                        null                                           // The sort order
                )
        ) {
            if (cursor == null || cursor.getCount() <= 0) {
                Log.d(TAG, "Method retrieveAllRawHeaders: No insoles data found by query");
                return null;
            }

            while (cursor.moveToNext()) {
                InsolesRawHeader header = new InsolesRawHeader(
                        cursor.getLong(0),      // session data
                        cursor.getDouble(1),    // sample rate
                        cursor.getInt(2),       // left ID
                        cursor.getInt(3),       // left sensor
                        cursor.getInt(4),       // right ID
                        cursor.getInt(5)        // right sensor
                );

                headerList.add(header);
            }
        }

        Log.v(TAG, "Method retrieveAllRawHeaders: end");
        return headerList;
    }

    public List<String> retrieveAllRawData(){
        Log.v(TAG, "Method retrieveAllRawData: start");

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "date BETWEEN ? AND ?";
        long dateMillis = Calendar.getInstance().getTimeInMillis();

        String[] selectionArgs = {
                getSpecificDayMidnight(dateMillis),
                fromMillisToString(dateMillis)
        };

        List<String> dataList = new ArrayList<>();

        try (
                Cursor cursor = db.query(
                        DatabaseContract.InsolesRawDataTable.TABLE_NAME,      // The table to query
                        null,                                          // The columns to return
                        selection,                                     // The columns for the WHERE clause
                        selectionArgs,//selectionArgs,                 // The values for the WHERE clause
                        null,                                          // don't group the rows
                        null,                                          // don't filter by row groups
                        null                                           // The sort order
                )
        ) {
            if (cursor == null || cursor.getCount() <= 0) {
                Log.d(TAG, "Method retrieveAllRawData: No insoles data found by query");
                return null;
            }

            while (cursor.moveToNext()) {
                dataList.add(cursor.toString());
            }
        }

        Log.v(TAG, "Method retrieveAllRawData: end");
        return dataList;
    }

    public List<Insoles> retrieveInsolesDataByDay(long dateMillis){
        Log.v(TAG, "Method retrieveInsolesDataByDay: start");
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        /*String selection = "date BETWEEN ? AND ?";

        String[] selectionArgs = {
                getSpecificDayMidnight(dateMillis),
                fromMillisToString(dateMillis)
        };*/

        List<Insoles> resultInsolesSet = new ArrayList<>();

        try (
                Cursor cursor = db.query(
                        DatabaseContract.InsolesTable.TABLE_NAME,       // The table to query
                        null,                                  // The columns to return
                        null,//selection,                                   // The columns for the WHERE clause
                        null,//selectionArgs,                               // The values for the WHERE clause
                        null,                                        // don't group the rows
                        null,                                        // don't filter by row groups
                        null                                         // The sort order
                )
        ) {
            if (cursor == null || cursor.getCount() <= 0) {
                Log.d(TAG, "Method retrieveInsolesDataByDay: No call found by query");
                return null;
            }

            while (cursor.moveToNext()) {
                List<Double> dataLeft = new ArrayList<>();
                List<Double> dataRight = new ArrayList<>();
                boolean leftAllNull = true;
                boolean rightAllNull = true;

                int columnCount = cursor.getColumnCount();

                // to be checked when GAIT_SESSION will be added to the table
                for(int index = 2; index < columnCount; index++){
                    if(index <= 20){
                        if(cursor.isNull(index)){
                            dataLeft.add(null);
                        } else{
                            leftAllNull = false;
                            dataLeft.add(cursor.getDouble(index));
                        }
                        //dataLeft.add((cursor.isNull(index)) ? null : cursor.getDouble(index));
                    } else{
                        if(cursor.isNull(index)){
                            dataRight.add(null);
                        } else{
                            rightAllNull = false;
                            dataRight.add(cursor.getDouble(index));
                        }
                        //dataRight.add((cursor.isNull(index)) ? null : cursor.getDouble(index));
                    }

                }
                Insoles i = new Insoles(cursor.getLong(1),
                        leftAllNull ? null : dataLeft,
                        rightAllNull ? null:dataRight);
                resultInsolesSet.add(i);
            }
        }

        Log.v(TAG, "Method retrieveInsolesDataByDay: end");
        return resultInsolesSet;
    }


    private String populateDataArray(int msgDefinition, String dataReceived){

        if(msgDefinition == 0 && dataReceived.length() == 2){
            Log.v(TAG, "Method PopulateDataArray: received 0 and empty data");
            return dataReceived;
        }
        // Create data array
        dataReceived = dataReceived.substring(1, dataReceived.length()-1);
        String[] dataArray = dataReceived.split(",");

        // Check with msgDefinition
        char[] msgChar = (Integer.toBinaryString(msgDefinition)).toCharArray();
        int bitIndex = LAST_BIT_POSITION;
        int dataIndex = 0;

        String data = "";

        while(bitIndex >= 0){
            if((bitIndex >= 2 && bitIndex <= 5) || (bitIndex >= 7 && bitIndex <= 10)){
                Log.d("SKIP", "salta 8 values");
                bitIndex--;
                continue;
            }

            if(Character.getNumericValue(msgChar[bitIndex]) == 1){
                data += dataArray[dataIndex] + (bitIndex != 0 ? ", " : "");
            } else{
                data += (bitIndex != 0 ? ", " : "");
            }
            dataIndex++;
            bitIndex--;
        }

        return data;



    }
}
