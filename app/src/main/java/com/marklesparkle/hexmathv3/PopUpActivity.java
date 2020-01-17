package com.marklesparkle.hexmathv3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class PopUpActivity extends Activity {

    //found from the youtube video Angga Risky
    //https://www.youtube.com/watch?v=eX-TdY6bLdg

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        //setting the pop up layout size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.6),(int)(height*.5));

        //setting the pop up layout position
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }

    public void deleteButtonClicked(View view){
        // returning the dateString String data to the MainActivity context, with the instruction to delete
        // the Date
        Log.d("PopUp","YES CLICKED");
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void closeButtonClicked(View view){
        //returning to the DisplayDateActivity context with the message NOT to delete
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
