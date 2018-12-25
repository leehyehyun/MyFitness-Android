package com.example.leehyehyun.myapplication;

import java.util.ArrayList;

public class Challenge {
    private String challengeName = "";
    private ArrayList<WorkOut> arrWorkOut = new ArrayList<WorkOut>();
    private boolean checked;
    private int id;

    public Challenge(int id, String challengeName) {
        this.id = id;
        this.challengeName = challengeName;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public ArrayList<WorkOut> getArrWorkOut() {
        return arrWorkOut;
    }

    public void clearArrWorkOut(){
        arrWorkOut.clear();
    }

    public void setArrWorkOut(ArrayList<WorkOut> arrWorkOut) {
        this.arrWorkOut = arrWorkOut;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
