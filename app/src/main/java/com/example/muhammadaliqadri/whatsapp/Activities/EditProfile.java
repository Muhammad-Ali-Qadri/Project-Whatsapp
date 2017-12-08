package com.example.muhammadaliqadri.whatsapp.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.muhammadaliqadri.whatsapp.Model.CircularImageView;
import com.example.muhammadaliqadri.whatsapp.R;


public class EditProfile extends AppCompatActivity {

    private static final int SELECT_IMAGE =1 ;
    String x;
    String y;
    private String savedText;
    private static final String SAVED_TEXT_KEY1 = "";
    private static final String SAVED_TEXT_KEY2 = "";
  /*  protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


                if((requestCode == SELECT_IMAGE)){
                   Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();



                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
            /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */
        /*    ImageView g=(ImageView)findViewById(R.id.profile_image);
            g.setImageBitmap(yourSelectedImage);
                }


    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode ==0&& resultCode == RESULT_OK
                    && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };


                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = findViewById(R.id.profile_image);

                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        ImageView v=(ImageView)findViewById(R.id.imageview) ;
        ImageView vv=(ImageView)findViewById(R.id.imagevieww) ;
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foo();
            }
        });
        vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateusername();
            }
        });


        ImageView v2=(ImageView)findViewById(R.id.imageView6) ;
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar();
            }
        });
        ImageView v3=(ImageView)findViewById(R.id.imagevieww) ;
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateusername();
            }
        });


        final EditText edittext=(EditText) findViewById(R.id.edit);
        final EditText edittext1=(EditText) findViewById(R.id.editt);
        edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditText v1 =(EditText) findViewById(R.id.edit);
                    v1.setCursorVisible(false);
                    v1.setEnabled(false);

                    v1.setFocusableInTouchMode(false);
                    v1.setClickable(false);

                    return true;
                }
                return false;
            }
        });
        edittext1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditText v1 =(EditText) findViewById(R.id.editt);
                    v1.setCursorVisible(false);
                    v1.setEnabled(false);

                    v1.setFocusableInTouchMode(false);
                    v1.setClickable(false);

                    return true;
                }
                return false;
            }
        });

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        final EditText edittext=(EditText) findViewById(R.id.edit);
        final EditText edittext1=(EditText) findViewById(R.id.editt);
        x=edittext.getText().toString();
        y=edittext1.getText().toString();
        outState.putString(SAVED_TEXT_KEY1, x);
        outState.putString(SAVED_TEXT_KEY2, y);

    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final EditText edittext=(EditText) findViewById(R.id.edit);


        final EditText edittext1=(EditText) findViewById(R.id.editt);
        edittext.setText(savedInstanceState.getString(SAVED_TEXT_KEY1));
        edittext1.setText(savedInstanceState.getString(SAVED_TEXT_KEY2));
        //  String myString = savedInstanceState.getString(SAVED_TEXT_KEY);
    }
    public void foo()
    {
        EditText v=(EditText) findViewById(R.id.edit);
        v.setCursorVisible(true);
        v.setEnabled(true);

        v.setFocusableInTouchMode(true);
        v.setClickable(true);
        v.setInputType(InputType.TYPE_CLASS_TEXT);
        v.requestFocus(); //to trigger the soft input
    }



    public void bar()
    {
      /*  Intent intent = new Intent();
      /*  intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,SELECT_IMAGE);*/
        // Intent intent = new Intent();
// Show only images, no videos or anything else
        //    intent.setType("image/*");

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 0);
    }
    public void updateusername()
    {
        EditText v=(EditText) findViewById(R.id.editt);
        v.setCursorVisible(true);
        v.setEnabled(true);

        v.setFocusableInTouchMode(true);
        v.setClickable(true);
        v.setInputType(InputType.TYPE_CLASS_TEXT);
        v.requestFocus(); //to trigger the soft input
    }
}
