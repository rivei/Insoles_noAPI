package com.polimi.insolesNoAPI.applicationLogic.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.polimi.insolesNoAPI.logic.insoles.InsolesManager;
import com.polimi.insolesNoAPI.model.insoles.Insoles;
import com.polimi.insolesNoAPI.model.insoles.InsolesRawHeader;
import com.polimi.insolesNoAPI.ui.DataPreviewActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReadTestingService  extends IntentService {
    private static final String TAG = ReadTestingService.class.getSimpleName();

    private static final String HEADER_NULL = "Header NULL";
    private static final String DATA_NULL   = "Data NULL";

    public ReadTestingService() {
        super("ReadTestingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        long sessionID = -100;
        long sessionDate = -100;

        InsolesManager insolesManager = new InsolesManager();
        List<InsolesRawHeader> rawHeaderList = insolesManager.getAllRawHeaders(this);

        for(InsolesRawHeader irh: rawHeaderList){
            sessionID = irh.getSessionID();
            sessionDate = irh.getSessionDate();
            String rawHeader = insolesManager.getRawHeader(this, sessionID);
            List<String> rawDataList = insolesManager.getRawData(this, sessionID);

            String filename = "Session" + sessionID;
            final String header = "ts, msg_def_left, msg_def_right, " +
                    "l_acc_x, l_acc_y, l_acc_z, "+
                    "l_pres_0, l_pres_1, l_pres_2, l_pres_3, l_pres_4, l_pres_5, l_pres_6, " +
                    "l_pres_7, l_pres_8, l_pres_9, l_pres_10, l_pres_11, l_pres_12, " +
                    "l_tf, l_cop_x, l_cop_y, " +
                    "r_acc_x, r_acc_y, r_acc_z, " +
                    "r_pres_0, r_pres_1, r_pres_2, r_pres_3, r_pres_4, r_pres_5, r_pres_6, " +
                    "r_pres_7, r_pres_8, r_pres_9, r_pres_10, r_pres_11, r_pres_12, " +
                    "r_tf, r_cop_x, r_cop_y \n";
            File f;
            FileOutputStream outputStream;
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
        }
/*        // Show Data from last trial
        Intent datapreviewIntent = new Intent(this, DataPreviewActivity.class);
        datapreviewIntent.putExtra(SESSION_ID, sessionID);
        startActivity(datapreviewIntent);*/
    }

}
