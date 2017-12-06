package com.example.muhammadaliqadri.whatsapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.muhammadaliqadri.whatsapp.Activities.CreateProfileActivity;
import com.example.muhammadaliqadri.whatsapp.Database.UserOpenHelper;
import com.example.muhammadaliqadri.whatsapp.Utility.BitmapUtility;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Muhammad Ali Qadri on 02/12/2017.
 */

public class WhatsappUser implements Serializable{

    protected String userId, userName, status, phoneNumber;
    private Bitmap profilePhoto;

    public WhatsappUser(){
        userId = userName = status = phoneNumber = "";
        profilePhoto = null;
    }

    public WhatsappUser(String id, String name, String st, String phone, Bitmap map){
        userId = id;
        userName = name;
        status = st;
        phoneNumber = phone;
        profilePhoto = map;
    }

    public Bitmap getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Bitmap profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void saveNew(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put("UID", userId);
        cv.put("PHONENUMBER", phoneNumber);
        db.insertWithOnConflict(UserOpenHelper.USER_PROFILE_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void updateUser(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put("USERNAME", userName);
        cv.put("STATUS", status);
        cv.put("PROFILEIMAGE", BitmapUtility.getBytes(profilePhoto));
        String arr[] = {userId};
        db.update(UserOpenHelper.USER_PROFILE_TABLE, cv, "UID=?", arr);
    }

    public void load(Cursor cursor){
        userId = cursor.getString(0);
        userName = cursor.getString(1);
        phoneNumber = cursor.getString(2);
        status = cursor.getString(3);
        profilePhoto = BitmapUtility.getImage(cursor.getBlob(4));
    }

    // Converts the Bitmap into a byte array for serialization
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(userId);
        out.writeObject(userName);
        out.writeObject(phoneNumber);
        out.writeObject(status);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        if(profilePhoto != null)
            profilePhoto.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
        byte bitmapBytes[] = byteStream.toByteArray();
        out.write(bitmapBytes, 0, bitmapBytes.length);
    }

    //READ DATA IN SAME ORDER AS WRITTEN
    // Deserializes a byte array representing the Bitmap and decodes it
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        userId = (String) in.readObject();
        userName = (String ) in.readObject();
        phoneNumber = (String) in.readObject();
        status = (String) in.readObject();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1)
            byteStream.write(b);
        byte bitmapBytes[] = byteStream.toByteArray();
        profilePhoto = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }

    public void saveUserToFirebase(final CreateProfileActivity activity){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("PROFILEPHOTOS/" + userId + ".png");

        final FirebaseWhatsappUser user = new FirebaseWhatsappUser();

        UploadTask uploadTask = storageRef.putBytes(BitmapUtility.getBytes(profilePhoto));
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(activity, "Unable to save to online database", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                user.setUserId(WhatsappUser.this.userId);
                user.setPhoneNumber(WhatsappUser.this.phoneNumber);
                user.setStatus(WhatsappUser.this.status);
                user.setUserName(WhatsappUser.this.userName);
                user.setProfilePhotoUri(downloadUrl);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("USERS").child(user.getUserId()).setValue(user);

                activity.forwardToMainContact();
            }
        });
    }

    //TODO: add rerieve mechanism from firebase for users here

//    public static WhatsappUser createUserFromFirebase(FirebaseWhatsappUser user){
//
//    }
}
