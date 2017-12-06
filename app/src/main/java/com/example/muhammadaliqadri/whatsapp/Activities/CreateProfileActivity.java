package com.example.muhammadaliqadri.whatsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.muhammadaliqadri.whatsapp.Database.UserOpenHelper;
import com.example.muhammadaliqadri.whatsapp.Model.FirebaseWhatsappUser;
import com.example.muhammadaliqadri.whatsapp.Model.WhatsappUser;
import com.example.muhammadaliqadri.whatsapp.R;
import com.example.muhammadaliqadri.whatsapp.Utility.BitmapUtility;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class CreateProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton photo;
    private WhatsappUser user;
    private EditText userName;
    private Bitmap profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_profile);
        photo = findViewById(R.id.photo);
        userName = findViewById(R.id.name);
        profilePhoto = photo.getDrawingCache();
        getSupportActionBar().setTitle("Profile Info");

        user = (WhatsappUser) getIntent().getSerializableExtra("user");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", userName.getText().toString());
        if(profilePhoto != null)
            outState.putByteArray("image", BitmapUtility.getBytes(profilePhoto.extractAlpha()));
        outState.putSerializable("user", user);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.getByteArray("image") != null)
            photo.setImageBitmap(BitmapUtility.getImage(savedInstanceState.getByteArray("image")));
        userName.setText(savedInstanceState.getString("name"));
        user = (WhatsappUser)savedInstanceState.getSerializable("user");
    }

    public void onNext(View view){

        String name = userName.getText().toString();

        if(name.equals("")){
            userName.setError("Enter a username");
            userName.requestFocus();
        }
        else {
            user.setUserName(name);
            if(profilePhoto == null)
                profilePhoto = BitmapFactory.decodeResource(CreateProfileActivity.this.getResources(), R.drawable.ic_person_black_24dp);
            user.setProfilePhoto(profilePhoto);
            user.setStatus(getResources().getString(R.string.default_status));

            //Save in DB local at the client
            UserOpenHelper helper = new UserOpenHelper(CreateProfileActivity.this);
            final SQLiteDatabase database = helper.getWritableDatabase();
            user.updateUser(database);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            //save in firebase users as now this client will be officially registered
            user.saveUserToFirebase(this);
        }
    }

    public void forwardToMainContact(){
        //TODO: forward to the contacts page here show the contacts page here
        //Toast.makeText(this, "Forward to main contacts", Toast.LENGTH_LONG).show();

        WhatsappUser user=getUserFromDb();

        Intent intent = new Intent(CreateProfileActivity.this, CustomTabActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();

    }

    public void onSelectingImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Choose app to show pictures"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            try {
                profilePhoto = BitmapUtility.scaleDown(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), 640, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            photo.setImageBitmap(profilePhoto);
        }
    }

    public WhatsappUser getUserFromDb(){

        UserOpenHelper helper = new UserOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserOpenHelper.USER_PROFILE_TABLE, null);
        WhatsappUser user=new WhatsappUser();

        if(cursor.moveToNext()){

            user.load(cursor);

        }
        cursor.close();
        return user;

    }
}
