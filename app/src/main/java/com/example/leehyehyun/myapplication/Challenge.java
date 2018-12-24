package com.example.leehyehyun.myapplication;

import java.util.ArrayList;

public class Challenge {
    private String challengeName = "";
    private ArrayList<WorkOut> arrWorkOut = new ArrayList<WorkOut>();
    private boolean checked;

    public Challenge(String challengeName, ArrayList<WorkOut> _arrWorkOut) {
        this.challengeName = challengeName;
        this.arrWorkOut = _arrWorkOut;
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
}
