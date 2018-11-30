package com.demos.studentchatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * Created by welcome on 2/2/2018.
 */

public class BillAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<BillBean> arrayList;
    SharedPreferences getPref;
    String user_name;
    Map<String, String> numUsers = new HashMap<>();
    Map<String, Float> userBills = new HashMap<>();
    String billAmountTotal = "st";
    Float price = 0.0f;

    public BillAdapter(Context context, ArrayList<BillBean> arrayList)
        /*, String[] description, String[] date, Integer[] images)*/ {

        this.arrayList = arrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);

        try {
            if (arrayList.size() != 0) {
                for (int in = 0; in < arrayList.size(); in++) {
                    numUsers.put(arrayList.get(in).getUser(), arrayList.get(in).getUser());
                    Log.e("map data", numUsers.get(arrayList.get(in).getUser()));
                }
                for (int j = 0; j < numUsers.size(); j++) {
                    price = 0.0f;
                    for (int k = 0; k < arrayList.size(); k++) {
                        if (numUsers.get(arrayList.get(j).getUser()).equals(arrayList.get(k).getUser()))
                            price = price + Float.valueOf(arrayList.get(k).getItemPrice());
                    }
                    userBills.put(numUsers.get(arrayList.get(j).getUser()), price);
                billAmountTotal = String.valueOf(0.0 + userBills.get(arrayList.get(j).getUser()));

                }
                Log.e("vars", numUsers.size() + ", " + arrayList.size() + ", " +
                        price + ", " + userBills.size() + ", " + billAmountTotal);
                /*Log.e("Num user 1", numUsers.get("Thirupathi"));
                Log.e("Num user 2", numUsers.get("Akka"));*/

                Log.e("Num user ph 1", numUsers.get("9550907676"));
                Log.e("Num user ph 2", numUsers.get("8897252597"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
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

            view = inflater.inflate(R.layout.bill_list_item, null);

            /*if (!arrayList.get(i).getName().equals(user_name))
                view = inflater.inflate(R.layout.chat_list, null);
            else
                view = inflater.inflate(R.layout.my_chat_list,null);*/
//            viewHolder.id=(TextView)view.findViewById(R.id.suh_name);

            viewHolder.date = view.findViewById(R.id.date);
            viewHolder.item_name = view.findViewById(R.id.item_name);
            viewHolder.item_price = view.findViewById(R.id.item_price);
            viewHolder.name = view.findViewById(R.id.user_name);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (arrayList.size() != 0) {

            viewHolder.date.setText(arrayList.get(i).getItemDate());
            viewHolder.item_name.setText(arrayList.get(i).getItemName());
            viewHolder.item_price.setText("\u20B9 "+arrayList.get(i).getItemPrice());
            viewHolder.name.setText(arrayList.get(i).getUser());

//            userBills.put(numUsers.get(i), arrayList.get(i).getItemPrice());
            if (arrayList.get(i).getUser().equals(user_name)) {
                viewHolder.name.setTextColor(Color.parseColor("#2962FF"));
            }
        }
        return view;
    }

    private class ViewHolder {
        TextView date, name, item_name, item_price;
    }

}
