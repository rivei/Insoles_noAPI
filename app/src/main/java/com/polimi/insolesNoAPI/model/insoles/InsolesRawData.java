package com.polimi.insolesNoAPI.model.insoles;


public class InsolesRawData {

    private Long timestamp;
    private Integer msgDefinitionLeft;
    private Integer msgDefinitionRight;
    private String dataLeft;
    private String dataRight;
    private Long sessionID;

    public InsolesRawData(Long ts, Integer msgDefLeft, Integer msgDefRight, String dataLeft, String dataRight, Long sessionID){
        this.timestamp = ts;
        this.msgDefinitionLeft = msgDefLeft;
        this.msgDefinitionRight = msgDefRight;
        this.dataLeft = dataLeft;
        this.dataRight = dataRight;
        this.sessionID = sessionID;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Integer getMsgDefinitionLeft() {
        return msgDefinitionLeft;
    }

    public String getDataLeft() {
        return dataLeft;
    }

    public Integer getMsgDefinitionRight() {
        return msgDefinitionRight;
    }

    public String getDataRight() {
        return dataRight;
    }

    public Long getSessionID() {
        return sessionID;
    }

    public void setSessionID(Long sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public String toString(){

        return timestamp            + ", " +
               msgDefinitionLeft    + ", " +
               msgDefinitionRight   + ", " +
               dataLeft             + ", " +
               dataRight            ;
    }
}
