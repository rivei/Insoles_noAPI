package com.polimi.insolesNoAPI.applicationLogic.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.polimi.insolesNoAPI.logic.insoles.InsolesManager;
import com.polimi.insolesNoAPI.ui.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

public class CreateOutputFileService extends IntentService {
    private static final String TAG = CreateOutputFileService.class.getSimpleName();

    private static final String SESSION_ID  = "sessionID";
    private static final String SESSION_DATE = "sessionDate";
    private static final String HEADER_NULL = "Header NULL";
    private static final String DATA_NULL   = "Data NULL";

    public CreateOutputFileService() {
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

        long sessionID = intent.getLongExtra(SESSION_ID, -100);
        long sessionDate = intent.getLongExtra(SESSION_DATE, -100);
        //long sessionID = 1;

        if(sessionID == -100 || sessionDate == -100){
            Log.e("ERROR", "Cannot receive "+SESSION_ID);
            return;
        }

        if(!isExternalStorageWritable()){
            Log.e("ERROR", "External memory not available");
            return;
        }

        InsolesManager insolesManager = new InsolesManager();
        String rawHeader = insolesManager.getRawHeader(this, sessionID);

        List<String> rawDataList = insolesManager.getRawData(this, sessionID);

        String filename = "Insoles - " + sessionDate;
        File f;

        FileOutputStream outputStream;
        final String header = "ts, msg_def_left, msg_def_right, " +
                "l_acc_x, l_acc_y, l_acc_z, "+
                "l_pres_0, l_pres_1, l_pres_2, l_pres_3, l_pres_4, l_pres_5, l_pres_6, " +
                "l_pres_7, l_pres_8, l_pres_9, l_pres_10, l_pres_11, l_pres_12, " +
                "l_tf, l_cop_x, l_cop_y, " +
                "r_acc_x, r_acc_y, r_acc_z, " +
                "r_pres_0, r_pres_1, r_pres_2, r_pres_3, r_pres_4, r_pres_5, r_pres_6, " +
                "r_pres_7, r_pres_8, r_pres_9, r_pres_10, r_pres_11, r_pres_12, " +
                "r_tf, r_cop_x, r_cop_y \n";

        try {
            f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filename);
            outputStream = new FileOutputStream(f);

            // Print Raw Header
            outputStream.write(
                    (rawHeader != null) ? rawHeader.getBytes() : HEADER_NULL.getBytes()
            );
            outputStream.write("\n".getBytes());
            // Print header
            outputStream.write(header.getBytes());
            // Print Raw Data
            for(String s : rawDataList){
                outputStream.write(
                        (s != null) ? s.getBytes() : DATA_NULL.getBytes()
                );
                outputStream.write("\n".getBytes());
            }

            outputStream.close();
            Log.e("END", "Output Stream Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }

        setMainActivityTextView("Creating file: check InternalStorage/Documents/!");
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
