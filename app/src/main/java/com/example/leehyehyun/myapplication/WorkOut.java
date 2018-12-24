package com.example.leehyehyun.myapplication;

import java.io.Serializable;
import java.util.Date;

public class WorkOut implements Serializable {
    private String name;
    private boolean checked = false;
    private int image;
    private String strCompleteDate;

    public WorkOut(String name, String strCompleteDate) {
        this.name = name;
        this.strCompleteDate = strCompleteDate;
    }

    public WorkOut(String name, int drawable) {
        this.name = name;
        this.image = drawable;
    }

    public WorkOut(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getImage() {
        return image;
    }

    public String getStrCompleteDate() {
        return strCompleteDate;
    }

    public void setStrCompleteDate(String strCompleteDate) {
        this.strCompleteDate = strCompleteDate;
    }
}
