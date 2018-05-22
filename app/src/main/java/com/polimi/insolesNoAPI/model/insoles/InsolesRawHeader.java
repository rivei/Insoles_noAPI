package com.polimi.insolesNoAPI.model.insoles;


public class InsolesRawHeader {
    private Long sessionDate;
    private Double sampleRate;

    private Integer rightID;
    private Integer rightSensor;

    private Integer leftID;
    private Integer leftSensor;
    private long sessionID; //add for retrieve all data

    public InsolesRawHeader(Long sessionDate, Double sampleRate, Integer leftID, Integer leftSensor, Integer rightID, Integer rightSensor) {
        this.sessionDate = sessionDate;
        this.sampleRate = sampleRate;
        this.leftID = leftID;
        this.leftSensor = leftSensor;
        this.rightID = rightID;
        this.rightSensor = rightSensor;
    }

    public Long getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Long sessionDate) {
        this.sessionDate = sessionDate;
    }

    public double getSampleRate() {
        return sampleRate;
    }

    public int getRightID() {
        return rightID;
    }

    public int getRightSensor() {
        return rightSensor;
    }

    public int getLeftID() {
        return leftID;
    }

    public int getLeftSensor() {
        return leftSensor;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    @Override
    public String toString(){
        return sessionDate  + ", " +
               sampleRate   + ", " +
               leftID       + ", " +
               leftSensor   + ", " +
               rightID      + ", " +
               rightSensor  ;
    }
}
