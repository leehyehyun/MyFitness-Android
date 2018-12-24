package com.example.leehyehyun.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WorkoutListAdapter extends BaseAdapter {
    private ArrayList<WorkOut> arrWorkOutList = new ArrayList<WorkOut>() ;

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

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;

        WorkOut workOut = arrWorkOutList.get(position);

        titleTextView.setText(workOut.getName());

        titleTextView.setTag(R.string.tag1,workOut);
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkOut workOut = (WorkOut)v.getTag(R.string.tag1);
                workOut.setChecked(!workOut.isChecked());

                if(workOut.isChecked()){ // 선택되게 보이게하기
                    v.setBackgroundResource(R.color.selectedColor);
                    arrWorkOutList.add(new WorkOut(workOut.getName(), true));
                }else{
                    v.setBackgroundResource(R.color.gray);
                    arrWorkOutList.add(new WorkOut(workOut.getName(), false));
                }

//                Log.w("is-",challengeItem.getChallengeName()+" / "+challengeItem.isChecked()+" / "+challengeItem.getArrWorkOut().size()+" / "+challengeItem.getArrWorkOut().get(0).getName());
            }
        });

        return convertView;
    }

    public boolean isChecked(int position) {
        return arrWorkOutList.get(position).isChecked();
    }

    public void setChecked(int position, boolean checked) {
        arrWorkOutList.get(position).setChecked(checked);
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return arrWorkOutList.get(position) ;
    }

    public void addItem(WorkOut workOut) {
        arrWorkOutList.add(workOut);
    }

    public void setArrWorkOutList(ArrayList<WorkOut> arrWorkOutList) {
        this.arrWorkOutList = arrWorkOutList;
    }

    public ArrayList<WorkOut> getArrWorkOutList() {
        return arrWorkOutList;
    }
}
