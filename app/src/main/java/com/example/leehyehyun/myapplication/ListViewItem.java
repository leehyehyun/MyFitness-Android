package com.example.leehyehyun.myapplication;

public class ListViewItem {
//    private String challengeName = "";
    private boolean checked;
    private Challenge mChallenge;

    public Challenge getmChallenge() {
        return mChallenge;
    }

    public void setmChallenge(Challenge mChallenge) {
        this.mChallenge = mChallenge;
    }

    //    public String getChallengeName() {
//        return challengeName;
//    }
//
//    public void setChallengeName(String challengeName) {
//        this.challengeName = challengeName;
//    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
