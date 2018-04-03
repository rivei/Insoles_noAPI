package com.polimi.insolesNoAPI.applicationLogic.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.polimi.insolesNoAPI.logic.log.SmartphoneLogReader;
import com.polimi.insolesNoAPI.model.phoneReport.Call;
import com.polimi.insolesNoAPI.ui.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class CreateTimestampFileService extends IntentService {
    private static final String TAG = CreateTimestampFileService.class.getSimpleName();

    private static final String SESSION_DATE = "sessionDate";

    private static final String DATA_NULL   = "Data NULL";
    private static final String HEADER      = "ts_start, ts_end \n";

    public CreateTimestampFileService() {
        super("CreateOutputFileService");
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Start service");

        long sessionDate = intent.getLongExtra(SESSION_DATE, -100);
        //long sessionID = 1;

        if(sessionDate == -100){
            Log.e("ERROR", "Cannot receive "+SESSION_DATE);
            return;
        }

        if(!isExternalStorageWritable()){
            Log.e("ERROR", "External memory not available");
            return;
        }
        SmartphoneLogReader smartphoneLogReader = new SmartphoneLogReader(this);
        List<String[]> timestampCall = smartphoneLogReader.readCallByTimestamp(sessionDate);

        if(timestampCall == null || timestampCall.size() == 0){
            Log.e(TAG, "No call during gait session");
            return;
        }

        String filename = "Insoles - " + sessionDate + " - calls";
        File f;
        FileOutputStream outputStream;

        try {
            f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filename);
            outputStream = new FileOutputStream(f);

            // Print header
            outputStream.write(HEADER.getBytes());

            // Print Timestamp Data
            for(String[] ts : timestampCall){
                String s = ts[0] + ", "+ ts[1] + "\n";
                outputStream.write(
                        s.getBytes()
                );
                outputStream.write("\n".getBytes());
            }

            outputStream.close();
            Log.e("END", "Output Stream Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "End service");
    }

    private void setMainActivityTextView(String val){
        try {
            if (MainActivity.getInst() != null)
                MainActivity.getInst().updateUIStrings(val);
        } catch (Exception e) {
            Log.e(TAG, "Exception caught: cannot update UI strings");
            e.printStackTrace();
        }
    }

}
