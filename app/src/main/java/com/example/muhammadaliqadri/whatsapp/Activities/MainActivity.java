package com.example.muhammadaliqadri.whatsapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.muhammadaliqadri.whatsapp.Database.UserOpenHelper;
import com.example.muhammadaliqadri.whatsapp.Model.FirebaseWhatsappUser;
import com.example.muhammadaliqadri.whatsapp.Model.WhatsappUser;
import com.example.muhammadaliqadri.whatsapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.firebase.ui.auth.User;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int userCheck = performUserCheck();

        if(userCheck == 1){
            gotoCreateProfile(true);
        }
        else if(userCheck == 2){
            //TODO: R-Start main chat activity
            finish();
        }
        else {
            // not signed in
            //Using firebase UI for user verification
            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }
    }

    private int performUserCheck(){
        UserOpenHelper helper = new UserOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserOpenHelper.USER_PROFILE_TABLE, null);

        int ret = 0;    //user does not exist
        if(cursor.moveToNext()){
            if(cursor.getString(1) == null)
                ret = 1;    //User has not assigned a username (registration half complete)
            else ret = 2;   //User has fully completed registration

        }
        cursor.close();
        return ret;
    }

    private void gotoCreateProfile(boolean isHalfComplete){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        WhatsappUser whatsappUser = new WhatsappUser();
        whatsappUser.setPhoneNumber(user.getPhoneNumber());
        whatsappUser.setUserId(user.getUid());

        if(!isHalfComplete){
            UserOpenHelper helper = new UserOpenHelper(MainActivity.this);
            SQLiteDatabase db = helper.getReadableDatabase();
            whatsappUser.saveNew(db);
        }

        Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
        intent.putExtra("user", whatsappUser);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                //Check if user has already created an account
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                Query query = mDatabase.child("USERS").orderByChild("phoneNumber").equalTo(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //user has already used the app so simply get his user data and continue to main app page
                        if(dataSnapshot.exists()){
                            //TODO: get required data from firebase and save to db and forward to contacts activity
                            if(dataSnapshot.exists()){
                                //Means the user already had an account
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    WhatsappUser u = ds.getValue(FirebaseWhatsappUser.class);
                                    //TODO: make sure the user information is stored in the db
                                }
                            }
                        }
                        else {
                            //new user
                            gotoCreateProfile(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else {
                // Sign in failed, check response for error code
                if(response == null){
                    Toast.makeText(this, R.string.fui_trouble_signing_in, Toast.LENGTH_LONG).show();
                    return;
                }
                if(response.getErrorCode() == ErrorCodes.NO_NETWORK){
                    Toast.makeText(this, R.string.no_intrenet_connection, Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
