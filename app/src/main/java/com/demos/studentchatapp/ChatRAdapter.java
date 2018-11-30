package com.demos.studentchatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class ChatRAdapter extends RecyclerView.Adapter<ChatRAdapter.MyViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<ChatBean> arrayList;
    SharedPreferences getPref;
    String user_name;
    static final int SENDER = 0;
    static final int RECEIVER = 1;

    public ChatRAdapter(Context context, ArrayList<ChatBean> arrayList)
        /*, String[] description, String[] date, Integer[] images)*/ {

        this.arrayList = arrayList;
        this.context = context;

        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ChatRAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;
        if (viewType == SENDER)
            view = inflater.inflate(R.layout.my_chat_list, viewGroup, false);
        else if (viewType == RECEIVER)
            view = inflater.inflate(R.layout.chat_list, viewGroup, false);
        return new MyViewHolder(view != null ? view : null);
    }


    @Override
    public void onBindViewHolder(@NonNull ChatRAdapter.MyViewHolder viewHolder, int pos) {

        user_name = Utilities.getPreference(context, Utilities.USER_NAME);

        if (arrayList.size() != 0) {

            viewHolder.date.setText(arrayList.get(pos).getDate());
            viewHolder.message.setText(arrayList.get(pos).getMessage());
            viewHolder.name.setText(arrayList.get(pos).getName());
            /*if (arrayList.get(i).getName().equals(user_name)) {
                viewHolder.name.setTextColor(Color.parseColor("#2962FF"));
            }*/

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, name;
        EmojiconTextView message;

        MyViewHolder(@NonNull View view) {
            super(view);
            date = view.findViewById(R.id.date);
            message = view.findViewById(R.id.chat_message);
            name = view.findViewById(R.id.user_name);
        }

    }

    @Override
    public int getItemViewType(int position) {
        String message = arrayList.get(position).getName();
        user_name = Utilities.getPreference(context, Utilities.USER_NAME);
        if (message.equals(user_name)) {
            return SENDER;
        } else {
            return RECEIVER;
        }
    }

}
