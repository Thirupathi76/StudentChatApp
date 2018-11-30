package com.demos.studentchatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.demos.studentchatapp.ChatAdapter;
import com.demos.studentchatapp.ChatBean;
import com.demos.studentchatapp.Utilities;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageView btn_send_msg;
    private String temp_key;
    RecyclerView list;
    ArrayList<ChatBean> chatList;
    ChatRAdapter adapter;
    LinearLayout rootView;
    EditText input_msg;
    private FirebaseFirestore firestore;
    String userName, branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        firestore = FirebaseFirestore.getInstance();
        chatList = new ArrayList<>();
        btn_send_msg = findViewById(R.id.btn_send);
        input_msg = findViewById(R.id.msg_input);

        list = findViewById(R.id.chatList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);
        mLayoutManager.setStackFromEnd(true);
        rootView = findViewById(R.id.rootView);

        userName = Utilities.getPreference(MainActivity.this, Utilities.USER_NAME);
        branch = Utilities.getPreference(MainActivity.this, Utilities.BRANCH);


        if (getSupportActionBar() != null) {
            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);*/
            getSupportActionBar().setTitle("Marolix");
        }

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.checkConnection(MainActivity.this)) {
                    if (!"".equals(input_msg.getText().toString())) {
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("user_name", userName);
                        map2.put("message", input_msg.getText().toString());
                        map2.put("date_time", getDateTime());
//                    map2.put("id",);
                        input_msg.setText("");
                        firestore.collection("Student chat").add(map2).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Failed to send msg", e.getMessage());

                            }
                        });

                    }
                } else {
                    Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("on start ", " called chat");
        branch = Utilities.getPreference(MainActivity.this, Utilities.BRANCH);
        try {
//            chatList.clear();
            firestore.collection("Student chat").orderBy("date_time", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    /*  if (documentSnapshot.exists() && documentSnapshot != null) {*/
                    chatList.clear();
                    for (DocumentSnapshot documents : documentSnapshots.getDocuments()) {

                        documents.getString("user_name");
                        String str = String.valueOf(documents.getData());
                        String user_name = documents.getString("user_name");
                        String message = documents.getString("message");
//                        String branch =  documents.getDocument().getString("branch");
                        String date_time = documents.getString("date_time");
                        String key = documents.getId();
                        Log.e("str changed doc", str);
//                        ChatBean bean = new ChatBean(user_name, message, date_time, key);
                        ChatBean bean = new ChatBean();
                        bean.setName(user_name);
                        bean.setMessage(message);
                        bean.setDate(date_time);
                        bean.setKey(key);
                        //doc.getDocument().toObject(BillBean.class);
                        chatList.add(bean);
                    }
                    adapter = new ChatRAdapter(MainActivity.this, chatList);
                    list.setAdapter(adapter);
                    list.scrollToPosition(chatList.size() - 1);
                    adapter.notifyDataSetChanged();

                    /*for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
//                    billList.clear();
                        chatList.clear();
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String str = String.valueOf(doc.getDocument().getData());
                            String user_name = doc.getDocument().getString("user_name");
                            String message = doc.getDocument().getString("message");
//                        String branch =  doc.getDocument().getString("branch");
                            String date_time = doc.getDocument().getString("date_time");
                            String key = doc.getDocument().getId();
                            Log.e("str changed doc", str);
                            ChatBean bean = new ChatBean(user_name, message, date_time, key);
                            //doc.getDocument().toObject(BillBean.class);
                            chatList.add(bean);

                        }
                        adapter = new ChatAdapter(MainActivity.this, chatList);
                        list.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
//                    Log.e("details onStart", user_name + " $$ " + item_name + " $$ " + item_price + " $$ " + date_time);
                    }*/

                    /* } else {

                    Log.e("onStart ", " doc doesn't exists");


                }*/
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    String getDateTime() {
        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        Log.e("Date and Time", stringDate);
        return stringDate;
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }
}
