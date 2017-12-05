package com.example.muhammadaliqadri.whatsapp.CustomViews.Chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.muhammadaliqadri.whatsapp.R;
import com.example.muhammadaliqadri.whatsapp.Utility.BitmapUtility;

import org.w3c.dom.Text;

/**
 * Created by Muhammad Ali Qadri on 05/12/2017.
 */

public class ChattingBubble extends RelativeLayout {

    private boolean isFromCurrentUser;
    private boolean isImage;
    private ImageButton imageButton;
    private TextView textView;
    private Bitmap shownImage;
    private String text;

    public ChattingBubble(Context context, boolean isFromCurrentUser, boolean isImage) {
        super(context);
        this.isFromCurrentUser = isFromCurrentUser;
        this.isImage = isImage;
        init();
    }

    public ChattingBubble(Context context, AttributeSet attrs, boolean isFromCurrentUser, boolean isImage) {
        super(context, attrs);
        this.isFromCurrentUser = isFromCurrentUser;
        this.isImage = isImage;
        init();
    }

    protected void init(){
        if(isFromCurrentUser && isImage){ //Image sent by current user
            inflate(getContext(), R.layout.right_chat_bubble_image, this);
            imageButton = findViewById(R.id.image_msg);
        }
        else if(isFromCurrentUser && !isImage){ //Text send by current user
            inflate(getContext(), R.layout.right_chat_bubble, this);
            textView = findViewById(R.id.txt_msg);
        }
        else if(!isFromCurrentUser && isImage){ //Image sent by other user
            inflate(getContext(), R.layout.left_chat_bubble_image, this);
            imageButton = findViewById(R.id.image_msg);
        }
        else {  //Text sent by other user
            inflate(getContext(), R.layout.left_chat_bubble, this);
            textView = findViewById(R.id.txt_msg);
        }
    }

    public void setShownImage(Bitmap map){
        shownImage = map;
        if(imageButton != null)
            imageButton.setImageBitmap(BitmapUtility.scaleDown(map, 540, true));
    }

    public Bitmap getShownImage(){
        return shownImage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        if(textView != null)
            textView.setText(text);
    }
}
