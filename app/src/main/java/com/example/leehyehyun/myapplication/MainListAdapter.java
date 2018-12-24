package com.example.leehyehyun.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainListAdapter extends BaseAdapter {
    private ArrayList<Challenge> listViewItemList = new ArrayList<Challenge>() ;
    private ArrayList<WorkOut> arrWorkOutList = new ArrayList<WorkOut>() ;

    public MainListAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;

        Challenge challengeItem = listViewItemList.get(position);

        titleTextView.setText(challengeItem.getChallengeName());

        if(challengeItem.isChecked()){
            titleTextView.setBackgroundResource(R.color.selectedColor);
        }else{
            titleTextView.setBackgroundResource(R.color.gray);
        }

        titleTextView.setTag(R.string.tag1,challengeItem);
        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Challenge challengeItem = (Challenge)v.getTag(R.string.tag1);
                challengeItem.setChecked(!challengeItem.isChecked());

                if(challengeItem.isChecked()){ // 선택되게 보이게하기
                    v.setBackgroundResource(R.color.selectedColor);
                    arrWorkOutList.addAll(challengeItem.getArrWorkOut());
                }else{
                    v.setBackgroundResource(R.color.gray);
                    arrWorkOutList.removeAll(challengeItem.getArrWorkOut());
                }

//                Log.w("is-",challengeItem.getChallengeName()+" / "+challengeItem.isChecked()+" / "+challengeItem.getArrWorkOut().size()+" / "+challengeItem.getArrWorkOut().get(0).getName());
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Challenge getItem(int position) {
        return listViewItemList.get(position) ;
    }

    private ArrayList<WorkOut> arrWorkOut = new ArrayList<WorkOut>();

    public void addItem(Challenge mChallenge) {
        listViewItemList.add(mChallenge);
        arrWorkOut.addAll(mChallenge.getArrWorkOut());
    }

    public ArrayList<WorkOut> getArrWorkOutList() {
        return arrWorkOutList;
    }

    public void setClear() {
        arrWorkOutList.clear();
    }
}
