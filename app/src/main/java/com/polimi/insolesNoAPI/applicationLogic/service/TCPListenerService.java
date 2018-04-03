package com.polimi.insolesNoAPI.applicationLogic.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polimi.insolesNoAPI.logic.insoles.InsolesManager;
import com.polimi.insolesNoAPI.model.insoles.InsolesRawData;
import com.polimi.insolesNoAPI.model.insoles.InsolesRawHeader;
import com.polimi.insolesNoAPI.ui.MainActivity;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class TCPListenerService extends IntentService {
    private static final String TAG = TCPListenerService.class.getSimpleName();

    private static final String SESSION_ID = "sessionID";
    private static final String SESSION_DATE = "sessionDate";
    private long sessionID, sessionDate;

    private final static String RATE_STRING = "sample_rate";
    private final static String LEFT_STRING = "left";
    private final static String RIGHT_STRING = "right";
    private final static String INS_ID_STRING = "insole_id";
    private final static String INS_SENS_STRING = "insole_sn";

    private static final int SERVER_PORT = 8888;
    private static final int MSG_LENGTH_BYTE = 4;
    private int mem_count = 0;

    public TCPListenerService() {
        super("TCPListenerService");
    }

    private int fromByteToInt (byte[] byteArray){
        ByteBuffer wrapped = ByteBuffer.wrap(byteArray); // big-endian by default

        return wrapped.getInt();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Start service");

        final Context context = this;

        setMainActivityTextView("Trying to connect..");
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            Socket connectionSocket = serverSocket.accept();
            DataInputStream in = new DataInputStream(connectionSocket.getInputStream());
            MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(in) )
        {
            setMainActivityTextView("Connecting..");

            Log.e("Client port: ",""+serverSocket.getLocalPort());
            sessionDate = Calendar.getInstance().getTimeInMillis();

            final ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
            TypeReference<List<Object>> typeReference = new TypeReference<List<Object>>(){};

            // First raw is header (InsolesRawHeader object)
            if(unpacker.hasNext()){
                sessionID = readFirstRow(unpacker, objectMapper, typeReference, sessionDate);
            }
            else{
                Log.e("Error", "Nothing to read");
                return;
            }
            //int count = 0;
            setMainActivityTextView("Receiving ..");
            while (unpacker.hasNext()){

                // read four bytes to detect payload length
                byte[] payloadLength = unpacker.readPayload(MSG_LENGTH_BYTE);

                // convert bytes to integer value that is the payload length
                int lengthValue = fromByteToInt(payloadLength);

                // read that bytes
                byte[] payloadContent = unpacker.readPayload(lengthValue);

                // Deserialize
                final List<Object> rawData = objectMapper.readValue(payloadContent, typeReference);

                //checkAppMemoryUsage();

                new Thread(new Runnable() {
                    public void run() {
                        InsolesManager insolesManager = new InsolesManager();
                        insolesManager.saveRawData(
                                context,
                                new InsolesRawData(
                                        (Long) rawData.get(1),      // timestamp
                                        (Integer) rawData.get(2),   // message definition left
                                        (Integer) rawData.get(3),   // message definition right
                                        rawData.get(4).toString(),  // data left
                                        rawData.get(5).toString(),  // data right
                                        sessionID
                                )
                        );
                    }
                }).start();
            }
            setMainActivityTextView("End receiving");
            Log.e("Fine", "Socket closed");
        } catch (IOException e) {
            Log.e("TCP", "C: Error", e);
        }

        // Create data output file
        Intent createFileIntent = new Intent(this, CreateOutputFileService.class);
        createFileIntent.putExtra(SESSION_ID, sessionID);
        createFileIntent.putExtra(SESSION_DATE, sessionDate);
        startService(createFileIntent);

        // Create call timestamp file
        Intent timestampIntent = new Intent(this, CreateTimestampFileService.class);
        timestampIntent.putExtra(SESSION_DATE, sessionDate);
        startService(timestampIntent);

        Log.i(TAG, "End service");
    }

    private long readFirstRow(MessageUnpacker unpacker, ObjectMapper objectMapper, TypeReference<List<Object>> typeReference,
                              long sessionDate) throws IOException {

        // read four bytes to detect payload length
        byte[] payloadLength = unpacker.readPayload(MSG_LENGTH_BYTE);

        // convert bytes to integer value that is the payload length
        int lengthValue = fromByteToInt(payloadLength);

        // read that bytes
        byte[] payloadContent = unpacker.readPayload(lengthValue);

        // Deserialize
        final List<Object> rawData = objectMapper.readValue(payloadContent, typeReference);

        // deserialize data + database insert
        LinkedHashMap map = (LinkedHashMap) rawData.get(1);
        InsolesManager insolesManager = new InsolesManager();

        //Log.e("Ricevo", rawData.toString());

        return insolesManager.saveRawHeader(
                this,
                new InsolesRawHeader(
                        sessionDate,
                        (Double) map.get(RATE_STRING),                                          // sample rate
                        (Integer)(((LinkedHashMap) map.get(LEFT_STRING)).get(INS_ID_STRING)),   // left ID
                        (Integer)(((LinkedHashMap) map.get(LEFT_STRING)).get(INS_SENS_STRING)), // left sensor
                        (Integer)(((LinkedHashMap) map.get(RIGHT_STRING)).get(INS_ID_STRING)),  // right ID
                        (Integer)(((LinkedHashMap) map.get(RIGHT_STRING)).get(INS_SENS_STRING)) // right sensor
                )
        );
    }

    private void checkAppMemoryUsage(){
        Runtime runtime = Runtime.getRuntime();
        long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        if(availHeapSizeInMB <= 90){
            System.out.print(availHeapSizeInMB+"-");
            if(mem_count == 30) {
                mem_count = 0;
                System.out.print("\n");
            }
            mem_count++;
        }

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
