package com.polimi.insolesNoAPI.ui;

import android.Manifest;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.polimi.insolesNoAPI.R;
import com.polimi.insolesNoAPI.applicationLogic.service.CreateTimestampFileService;
import com.polimi.insolesNoAPI.applicationLogic.service.TCPListenerService;
import com.polimi.insolesNoAPI.ui.fragment.PermissionErrorFragment;
import com.polimi.insolesNoAPI.ui.fragment.PermissionFragment;
import com.polimi.insolesNoAPI.ui.interfaces.PermissionDialogListener;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements PermissionDialogListener, ComponentCallbacks2 {

    private static MainActivity ins;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 12345;
    private static final String[] permissionsArray = {
            /* Permission Group "Phone", which includes READ_CALL_LOG, CALL_PHONE*/
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET,

            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //private DialogFragment initFragment;
    private DialogFragment permissionFragment;
    private DialogFragment permissionErrorFragment;

    /**
     * Release memory when the UI becomes hidden or when system resources become low.
     * @param level the memory-related event that was raised.
     */
    public void onTrimMemory(int level) {

        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                Log.e("TRIM_MEMORY", "UI_HIDDEN");
                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                Log.e("TRIM_MEMORY", "RUNNING_MODERATE");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                Log.e("TRIM_MEMORY", "RUNNING_LOW");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                Log.e("TRIM_MEMORY", "RUNNING_CRITICAL");
                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                Log.e("TRIM_MEMORY", "BACKGROUND");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                Log.e("TRIM_MEMORY", "MODERATE");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                Log.e("TRIM_MEMORY", "COMPLETE");
                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                Log.e("TRIM_MEMORY", "default");
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ins = this;

        TextView instructionsText = (TextView)findViewById(R.id.instructionText);
        instructionsText.setText(R.string.instructions);

        boolean permissionRequestNeeded = false;

        for (String s : permissionsArray) {
            if (ContextCompat.checkSelfPermission(this, s)
                    != PackageManager.PERMISSION_GRANTED){
                permissionRequestNeeded = true;
                break;
            }
        }

        if(permissionRequestNeeded){
            permissionFragment = new PermissionFragment();
            permissionFragment.setCancelable(false);
            permissionFragment.show(getSupportFragmentManager(), "permission");
        }
    }

    @Override
    public void onDestroy()
    {
        ins = null;
        super.onDestroy();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        permissionFragment.dismiss();

        ActivityCompat.requestPermissions(this, permissionsArray,
                ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        if(permissionFragment != null){
            permissionFragment.dismiss();
        }

        if(permissionErrorFragment != null){
            permissionErrorFragment.dismiss();
        }

        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if(requestCode == ASK_MULTIPLE_PERMISSION_REQUEST_CODE){
            boolean denied = false;

            for (int permissionResult: grantResults) {
                if(permissionResult == PackageManager.PERMISSION_DENIED){
                    denied = true;
                    break;
                }
            }

           if(denied){
               new Timer().schedule(new TimerTask() {
                   @Override public void run() {
                       permissionErrorFragment = new PermissionErrorFragment();
                       permissionErrorFragment.setCancelable(false);
                       permissionErrorFragment.show(getSupportFragmentManager(), "permissionError");
                   }
               }, 0);
            }
        }
    }



    public void startClicked(View view) {
        startService(new Intent(this, TCPListenerService.class));

        /*Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        // Create call timestamp file
        Intent timestampIntent = new Intent(this, CreateTimestampFileService.class);
        timestampIntent.putExtra("sessionDate", c.getTimeInMillis());
        startService(timestampIntent);*/
    }

    public static MainActivity getInst()
    {
        return ins;
    }

    public void updateUIStrings(final String s) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.stateText);
                textView.setText(s);
            }
        });
    }

}
