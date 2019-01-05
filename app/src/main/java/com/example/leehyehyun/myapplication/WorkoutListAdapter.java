package com.example.leehyehyun.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class WorkoutListAdapter extends BaseAdapter {
    private ArrayList<WorkOut> arrWorkOutList = new ArrayList<>() ;

    public WorkoutListAdapter() {
    }

    @Override
    public int getCount() {
        return arrWorkOutList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.textView1);
        CheckBox checkbox = convertView.findViewById(R.id.checkbox);
        checkbox.setVisibility(View.VISIBLE);

        WorkOut workOut = arrWorkOutList.get(position);

        titleTextView.setText(workOut.getName());

        titleTextView.setTag(R.string.tag1,workOut);
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkOut workOut = (WorkOut)v.getTag(R.string.tag1);

                Intent intent = new Intent(context, WorkOutImageActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("workout",(Serializable) workOut);
                intent.putExtra("bundle",args);
                context.startActivity(intent);
            }
        });

        checkbox.setChecked(workOut.isChecked());

        checkbox.setTag(R.string.tag1,workOut);
        checkbox.setTag(R.string.tag2,titleTextView);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WorkOut workOut = (WorkOut)buttonView.getTag(R.string.tag1);
                TextView _titleTextView = (TextView)buttonView.getTag(R.string.tag2);
                workOut.setChecked(!workOut.isChecked());

                if(workOut.isChecked()){
                    //오늘의 운동기록 어레이에 넣음
                    _titleTextView.setBackgroundResource(R.color.selectedColor);
                    arrTodayWorkout.add(workOut);
                }else{
                    //오늘의 운동기록 어레이에서 제거
                    _titleTextView.setBackgroundResource(R.color.gray);
                    arrTodayWorkout.remove(workOut);
                }
            }
        });
        return convertView;
    }
    private ArrayList<WorkOut> arrTodayWorkout = new ArrayList<>();

    public void clearArrTodayWorkout(){
        arrTodayWorkout.clear();
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return arrWorkOutList.get(position) ;
    }

    public void setArrWorkOutList(ArrayList<WorkOut> arrWorkOutList) {
        this.arrWorkOutList = arrWorkOutList;
    }

    public ArrayList<WorkOut> getArrWorkOutList() {
        return arrWorkOutList;
    }

    public ArrayList<WorkOut> getArrTodayWorkout() {
        return arrTodayWorkout;
    }

    public String getArrTodayWorkoutStr() {
        String str = "";
        for(int i = 0 ; i < arrTodayWorkout.size() ; i++){
            if(i == arrTodayWorkout.size()-1){
                str += arrTodayWorkout.get(i).getName();
            }else{
                str += arrTodayWorkout.get(i).getName()+",";
            }
        }
        return str;
    }
}
