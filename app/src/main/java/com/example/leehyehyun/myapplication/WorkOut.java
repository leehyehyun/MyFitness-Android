package com.example.leehyehyun.myapplication;

import java.io.Serializable;

public class WorkOut implements Serializable {
    private String name;
    private boolean checked = false;
    private String strCompleteDate;
    private String imagePath;

    public WorkOut(String name, String imagePath, String strCompleteDate) {
        this.name = name;
        this.imagePath = imagePath;
        this.strCompleteDate = strCompleteDate;
    }

    public WorkOut(String name, String strCompleteDate) {
        this.name = name;
        this.strCompleteDate = strCompleteDate;
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

    public String getStrCompleteDate() {
        return strCompleteDate;
    }

    public void setStrCompleteDate(String strCompleteDate) {
        this.strCompleteDate = strCompleteDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
