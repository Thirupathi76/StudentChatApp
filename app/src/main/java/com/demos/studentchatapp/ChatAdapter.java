package com.demos.studentchatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ChatAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<ChatBean> arrayList;
    SharedPreferences getPref;
    String user_name;
    public static final int SENDER = 0;
    public static final int RECEIVER = 1;


    public ChatAdapter(Context context, ArrayList<ChatBean> arrayList)
        /*, String[] description, String[] date, Integer[] images)*/{

        this.arrayList = arrayList;
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
//            getPref = context.getSharedPreferences(MainActivity.Pref, MODE_PRIVATE);
            user_name = Utilities.getPreference(context, Utilities.USER_NAME);
            //getPref.getString(Utilities.USER_NAME, "");

            if (!arrayList.get(i).getName().equals(user_name))
                view = inflater.inflate(R.layout.chat_list, null);
            else
                view = inflater.inflate(R.layout.my_chat_list,null);

//            viewHolder.id=(TextView)view.findViewById(R.id.suh_name);
            viewHolder.date = view.findViewById(R.id.date);
            viewHolder.message = view.findViewById(R.id.chat_message);
            viewHolder.name = view.findViewById(R.id.user_name);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (arrayList.size() != 0) {

            viewHolder.date.setText(arrayList.get(i).getDate());
            viewHolder.message.setText(arrayList.get(i).getMessage());
            viewHolder.name.setText(arrayList.get(i).getName());
            /*if (arrayList.get(i).getName().equals(user_name)) {
                viewHolder.name.setTextColor(Color.parseColor("#2962FF"));
            }*/

        }

        return view;
    }




    private class ViewHolder {
        TextView date, name;
        TextView message;
    }



}
