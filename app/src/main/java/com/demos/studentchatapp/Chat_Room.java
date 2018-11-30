package com.demos.studentchatapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class Chat_Room extends AppCompatActivity {

    private ImageView btn_send_msg, btn_emoji;
    private EmojiconEditText input_msg;

    private String user_name, room_name;
    private DatabaseReference root;
    private String temp_key;
    RecyclerView list;
    ArrayList<ChatBean> chatList;
    ChatRAdapter adapter;
    LinearLayout rootView;
    EmojIconActions emojIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        chatList = new ArrayList<>();
        btn_send_msg = findViewById(R.id.btn_send);
        input_msg = findViewById(R.id.msg_input);
        btn_emoji = findViewById(R.id.smiley);



        list = findViewById(R.id.chatList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);
        mLayoutManager.setStackFromEnd(true);
        rootView = findViewById(R.id.rootView);
        emojIcon = new EmojIconActions(this, rootView, input_msg, btn_emoji);
//        emojIcon.closeEmojIcon();
        emojIcon.ShowEmojIcon();
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.marolix_icon);*/

        /*user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();*/

        room_name = "Student chat";//Utilities.getPreference(Chat_Room.this, Utilities.GROUP_NAME);
        user_name = Utilities.getPreference(Chat_Room.this, Utilities.USER_NAME);

        setTitle("Marolix");
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        root = FirebaseDatabase.getInstance().getReference(room_name);
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utilities.checkConnection(Chat_Room.this)) {
                    if (!"".equals(input_msg.getText().toString())) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        temp_key = root.push().getKey();
                        root.updateChildren(map);

                        DatabaseReference message_root = root.child(temp_key);//temp_key
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("user_name", user_name);
                        map2.put("message", input_msg.getText().toString());
                        map2.put("date_time", getDateTime());
                        input_msg.setText("");
                        message_root.updateChildren(map2);
                    }
                } else {
                    Toast.makeText(Chat_Room.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                /*append_chat_conversation(dataSnapshot);*/
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new ChatRAdapter(this, chatList);
        list.scrollToPosition(chatList.size() - 1);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    public String getContactName(final String phoneNumber, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
                Log.e("Contact name phone num", contactName + " , " + phoneNumber);
            }
            cursor.close();
        }
        if (contactName.equals(""))
            return phoneNumber;
        return contactName;
    }

    String getDateTime() {
        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        Log.e("Date and Time", stringDate);
        return stringDate;
    }

    private String chat_msg, chat_user_name, chat_date;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {
            ChatBean chatData = new ChatBean();
            String date = (String) ((DataSnapshot) i.next()).getValue();
            String message = (String) ((DataSnapshot) i.next()).getValue();
            String name = (String) ((DataSnapshot) i.next()).getValue();
            chatData.setDate(date);
            chatData.setMessage(message); //getContactName((String) ((,Chat_Room.this));
            chatData.setName(name);

//            createNotify(name, message, dataSnapshot.getChildren().hashCode(),true);
            
            /*chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
            chat_date = (String) ((DataSnapshot)i.next()).getValue();*/
            chatList.add(chatData);
//            chat_conversation.append(chat_user_name +" : "+chat_msg +"+ : "+chat_date + "\n");

        }
        adapter = new ChatRAdapter(this, chatList);
        list.setAdapter(adapter);
        list.scrollToPosition(chatList.size() - 1);
        /*list.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                                       int oldBottom) {
                if (bottom < oldBottom) {
                    list.post(new Runnable() {
                        @Override
                        public void run() {
                            list.scrollToPosition(list.getAdapter().getItemCount() - 1);
                        }
                    });
                }
            }
        });*/
        adapter.notifyDataSetChanged();
    }


    public void createNotify(String name, String content, int id, boolean isGroup) {
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setContentTitle(name)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setAutoCancel(true);
        if (isGroup) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(
                        Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
        notificationManager.notify(id,
                notificationBuilder.build());
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                Utilities.clearAllPrefernces(this);
                startActivity(new Intent(this, AuthActivity.class));
                break;

        }
        return false;
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
