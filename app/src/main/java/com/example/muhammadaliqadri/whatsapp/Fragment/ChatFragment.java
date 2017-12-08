package com.example.muhammadaliqadri.whatsapp.Fragment;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.muhammadaliqadri.whatsapp.Adapter.ContactListAdapter;
import com.example.muhammadaliqadri.whatsapp.Database.UserOpenHelper;
import com.example.muhammadaliqadri.whatsapp.Model.WhatsappUser;
import com.example.muhammadaliqadri.whatsapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
public class ChatFragment extends Fragment {


/*

    public static ArrayList<WhatsappUser> rowItems;
    ListView mylistview;
    View view;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    public static ContactListAdapter contactListAdapter;

    public ChatFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        rowItems = new ArrayList<WhatsappUser>();


        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'RepositoryName' node
        mFirebaseDatabase = mFirebaseInstance.getReference();

        if(savedInstanceState!=null){
            rowItems=(ArrayList<WhatsappUser>)savedInstanceState.getSerializable("chats");
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabStartChat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //loadContactsFormSqlite(getContext());
        //loadMessagesFromSqlite(getContext());
        setContactsView(view);

        return view;
    }
    public void setContactsView(View view) {
        contactListAdapter = new ContactListAdapter(getActivity(), rowItems,1);
        mylistview = view.findViewById(R.id.chat_list);
        mylistview.setAdapter(contactListAdapter);
    }

    private boolean UserIsInContacts(ArrayList<WhatsappUser> records, String phone) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getPhoneNumber().equalsIgnoreCase(phone)) {
                return true;
            }
        }
        return false;
    }

    void getImage(String string_url, final WhatsappUser u) {

        RequestQueue myQueue = Volley.newRequestQueue(getActivity().getBaseContext());

// Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(string_url,
                new com.android.volley.Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {

                        u.setProfilePhoto(bitmap);
                        contactListAdapter.notifyDataSetChanged();

                        UserOpenHelper helper = new UserOpenHelper(getActivity().getBaseContext());
                        final SQLiteDatabase database = helper.getWritableDatabase();
                        u.updateContactUser(database);

                    }
                }, 0, 0, null,
                new com.android.volley.Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
// Access the RequestQueue through your singleton class.
        myQueue.add(request);

    }

    public void loadContactsFormSqlite(Context context) {

        UserOpenHelper helper = new UserOpenHelper(getActivity().getBaseContext());
        final SQLiteDatabase database = helper.getWritableDatabase();

        String query = "SELECT * FROM " + UserOpenHelper.USERS_TABLE;
        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            WhatsappUser object = new WhatsappUser();
            object.load(cursor);
            rowItems.add(object);
        }
        cursor.close();
        database.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("contacts", rowItems);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            rowItems = (ArrayList<WhatsappUser>) savedInstanceState.getSerializable("contacts");
        }
    }

    public void loadMessagesFromSqlite(Context context){

    }
    public static void getRows(ArrayList<WhatsappUser> r){
        for(int i=0;i<r.size();i++){
            rowItems.add(r.get(i));
        }
        contactListAdapter.notifyDataSetChanged();
    }
*/

}