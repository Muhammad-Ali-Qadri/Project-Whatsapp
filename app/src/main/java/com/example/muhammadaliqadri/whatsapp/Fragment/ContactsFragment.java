package com.example.muhammadaliqadri.whatsapp.Fragment;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.muhammadaliqadri.whatsapp.Activities.CreateProfileActivity;
import com.example.muhammadaliqadri.whatsapp.Activities.CustomTabActivity;
import com.example.muhammadaliqadri.whatsapp.Adapter.ContactListAdapter;
import com.example.muhammadaliqadri.whatsapp.Database.UserOpenHelper;
import com.example.muhammadaliqadri.whatsapp.Manifest;
import com.example.muhammadaliqadri.whatsapp.Model.WhatsappUser;
import com.example.muhammadaliqadri.whatsapp.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {


    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    final int PICK_CONTACT = 1;
    public ArrayList<WhatsappUser> rowItems;
    ListView mylistview;
    View view;
    int INSERT_CONTACT_REQUEST = 2;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    public ContactListAdapter contactListAdapter;

    WhatsappUser user;

    public ContactsFragment() {
        // Required empty public constructor
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
           rowItems=(ArrayList<WhatsappUser>)savedInstanceState.getSerializable("contacts");
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contacts, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

// Creates a new Intent to insert a contact
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
// Sets the MIME type to match the Contacts Provider
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                startActivity(intent);
            }
        });
        setContactsView(view);
        getPermissionsToShowContacts();
        return view;
    }

    public void getPermissionsToShowContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            //load contacts from db

            loadContactsFormSqlite(getActivity().getBaseContext());
            getPhoneContactList();
        }
    }

    public void getPhoneContactList() {
        ArrayList<WhatsappUser> records = new ArrayList<WhatsappUser>();
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    WhatsappUser contact;
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        contact = new WhatsappUser();
                        contact.setPhoneNumber(phoneNo);
                        records.add(contact);
                    }
                    pCur.close();
                }
            }
        }

        if (cur != null) {
            cur.close();
        }

        RemoveContactsNotAvailableInApp(records);
    }

    public void RemoveContactsNotAvailableInApp(final ArrayList<WhatsappUser> records) {

        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot list : snapshot.getChildren()) {

                    if (list.getKey().equalsIgnoreCase("USERS")) {

                        WhatsappUser u;
                        for (DataSnapshot child : list.getChildren()) {

                            if (UserIsInContacts(records, (String) child.child("phoneNumber").getValue())
                                    && !UserIsInContacts(rowItems, (String) child.child("phoneNumber").getValue())) {

                                u = new WhatsappUser();
                                u.setPhoneNumber((String) child.child("phoneNumber").getValue());
                                u.setStatus((String) child.child("status").getValue());
                                u.setUserId((String) child.child("userId").getValue());
                                u.setUserName((String) child.child("userName").getValue());

                                rowItems.add(u);
                                contactListAdapter.notifyDataSetChanged();
                                UserOpenHelper helper = new UserOpenHelper(getActivity().getBaseContext());
                                final SQLiteDatabase database = helper.getWritableDatabase();
                                u.insertContactUser(database);


                                String photoURL = (String) child.child("profilePhotoUri").getValue();
                                getImage(photoURL, u);
                            }

                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setContactsView(View view) {
        contactListAdapter = new ContactListAdapter(getActivity(), rowItems);
        mylistview = view.findViewById(R.id.contact_list);
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
            contactListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        if (requestCode == INSERT_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getContext(), "Contacts Adding Successfully", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Contacts Adding Error", Toast.LENGTH_SHORT).show();
            }

        }
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


}