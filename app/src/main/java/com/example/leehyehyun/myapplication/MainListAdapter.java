package com.example.leehyehyun.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainListAdapter extends BaseAdapter {
    private ArrayList<Challenge> arrChallenge = new ArrayList<>() ;

    public MainListAdapter() {
    }

    @Override
    public int getCount() {
        return arrChallenge.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.textView1) ;

        Challenge challengeItem = arrChallenge.get(position);

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
                    selectedChallenges.add(challengeItem);
                }else{
                    v.setBackgroundResource(R.color.gray);
                    selectedChallenges.remove(challengeItem);
                }
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
        return arrChallenge.get(position) ;
    }

    public void setChallengeList(ArrayList<Challenge> _arrChallenge) {
        arrChallenge.addAll(_arrChallenge);
    }

    private ArrayList<Challenge> selectedChallenges = new ArrayList<>();
    public ArrayList<Challenge> getSelectedChallenges(){
        return selectedChallenges;
    }

    public void clearArrChallenge(){
        arrChallenge.clear();
    }

    public void clearSelectedChallenges(){
        selectedChallenges.clear();
    }
}
