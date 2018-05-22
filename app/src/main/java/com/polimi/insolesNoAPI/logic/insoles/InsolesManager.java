package com.polimi.insolesNoAPI.logic.insoles;

import android.content.Context;

import com.polimi.insolesNoAPI.dao.DaoGaitSession;
import com.polimi.insolesNoAPI.dao.DaoInsoles;
import com.polimi.insolesNoAPI.model.insoles.Insoles;
import com.polimi.insolesNoAPI.model.insoles.InsolesHeader;
import com.polimi.insolesNoAPI.model.insoles.InsolesRawData;
import com.polimi.insolesNoAPI.model.insoles.InsolesRawHeader;

import java.util.List;

/* Thread Safe Class */
public class InsolesManager {
    // Multiple threads access to the class variables
    private static final String TAG = InsolesManager.class.getSimpleName();


    /* ****** GAIT SESSION ****** */
    public void saveGaitSession(Context context, InsolesHeader data){
        DaoGaitSession daoGaitSession = new DaoGaitSession(context);
        try {
            daoGaitSession.storeGaitSession(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<InsolesHeader> getGaitSessionByDay(Context context, long dateMillis){
        DaoGaitSession daoGaitSession = new DaoGaitSession(context);
        return daoGaitSession.retrieveGaitSessionByDay(dateMillis);
    }

    /* ****** RAW DATA ****** */
    public void saveRawData(Context context, InsolesRawData insolesRawData){
        DaoInsoles daoInsoles = new DaoInsoles(context);
        try {
            daoInsoles.storeRawData(insolesRawData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long saveRawHeader(Context context, InsolesRawHeader rawHeader){
        DaoInsoles daoInsoles = new DaoInsoles(context);
        try {
            return daoInsoles.storeRawHeader(rawHeader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    public List<String> getRawData(Context context, long sessionID){
        return new DaoInsoles(context).retrieveRawData(sessionID);
    }

    public String getRawHeader(Context context, long sessionID){
        return new DaoInsoles(context).retrieveRawHeader(sessionID);
    }

    public List<InsolesRawHeader> getAllRawHeaders(Context context){
        return new DaoInsoles(context).retrieveAllRawHeaders();
    }

    /* ****** INSOLES ****** */
    public void saveInsoles(Context context, Insoles insoles){
        DaoInsoles daoInsoles = new DaoInsoles(context);
        try {
            daoInsoles.storeInsolesData(insoles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Insoles> getInsolesDataByDay(Context context, long dateMillis){
        DaoInsoles daoInsoles = new DaoInsoles(context);
        return daoInsoles.retrieveInsolesDataByDay(dateMillis);
    }

    public void closeDatabase(Context context){
        DaoInsoles daoInsoles = new DaoInsoles(context);
        daoInsoles.closeConnection();
    }
}
