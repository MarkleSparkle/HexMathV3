package com.marklesparkle.hexmathv3;

import android.app.admin.NetworkEvent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {

    Button rtlConvertButton, ltrConvertButton;
    Button rtlMathButton, ltrMathButton;
    Button neverButton, onceButton, twiceButton, thriceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        rtlConvertButton = findViewById(R.id.rtlConvertButton);
        ltrConvertButton = findViewById(R.id.ltrConvertButton);

        rtlMathButton = findViewById(R.id.rtlMathButton);
        ltrMathButton = findViewById(R.id.ltrMathButton);

        neverButton = findViewById(R.id.neverButton);
        onceButton = findViewById(R.id.oneTime);
        twiceButton = findViewById(R.id.twiceButton);
        thriceButton = findViewById(R.id.thriceButton);

        setVisuals();
    }

    /**
     * Saving sharedPreferences that are changed in OptionsActivity
     * tutorial from https://www.journaldev.com/9412/android-shared-preferences-example-tutorial
     */
    public void saveConvertPreference(String convert){
        //initializing SharedPreferences (accessing the file)
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);// 0 is for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //putting (key, value);
        editor.putString("CONVERT",convert);

        editor.commit();//just like GIT you gotta commit
    }

    /**
     * Saving sharedPreferences that are changed in OptionsActivity
     * tutorial from https://www.journaldev.com/9412/android-shared-preferences-example-tutorial
     */
    public void saveMathPreference(String math){
        //initializing SharedPreferences (accessing the file)
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);// 0 is for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //putting (key, value);
        editor.putString("MATH", math);

        editor.commit();//just like GIT you gotta commit
    }

    /**
     * Saving sharedPreferences that are changed in OptionsActivity
     * tutorial from https://www.journaldev.com/9412/android-shared-preferences-example-tutorial
     */
    public void saveRepeatingPreference(String repeatingTimes){
        //initializing SharedPreferences (accessing the file)
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);// 0 is for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //putting (key, value);
        editor.putString("RT", repeatingTimes);

        editor.commit();//just like GIT you gotta commit
    }

    /**
     * Changes the InputType of GameSettings for the CONVERT games
     *
     * When one of two buttons are clicked,
     * the setVisuals() method is then called to change button colors
     *
     * @param view
     */
    public void convertInputClicked(View view) {
        String preference;

        //Converting the view into a Button object
        Button buttonUsed = null;
        if (view instanceof Button) {
            buttonUsed = (Button) view;
        }

        //changing the saved preferences and the settings used based on the text of the button clicked
        switch (buttonUsed.getText().toString()){
            case "LEFT-TO-RIGHT":
                GameSettings.setConvertInputType(InputType.LRT);
                preference = "ltr";
                break;

            case "RIGHT-TO-LEFT":
                GameSettings.setConvertInputType(InputType.RTL);
                preference = "rtl";
                break;

            default:
                GameSettings.setConvertInputType(InputType.LRT);
                preference = "ltr";
                Log.e("convertInputClicked","There is no "+buttonUsed.getText().toString()+" button. LTR setting selected");
        }

        saveConvertPreference(preference);
        setVisuals();

    }

    /**
     * Changes the InputType of GameSettings for the MATH games
     *
     * When one of two buttons are clicked,
     * the setVisuals() method is then called to change button colors
     * @param view
     */
    public void mathInputClicked(View view) {
        String preference;

        //Converting the view into a Button object
        Button buttonUsed = null;
        if (view instanceof Button) {
            buttonUsed = (Button) view;
        }

        //changing the saved preferences and the settings used based on the text of the button clicked
        switch (buttonUsed.getText().toString()){
            case "LEFT-TO-RIGHT":
                GameSettings.setMathInputType(InputType.LRT);
                preference = "ltr";
                break;

            case "RIGHT-TO-LEFT":
                GameSettings.setMathInputType(InputType.RTL);
                preference = "rtl";
                break;

            default:
                GameSettings.setMathInputType(InputType.LRT);
                preference = "ltr";
                Log.e("mathInputClicked","There is no "+buttonUsed.getText().toString()+" button. LTR setting selected");
        }

        saveMathPreference(preference);
        setVisuals();
    }

    /**
     * This method is called when the "NONE", "ONCE", "TWICE", OR "THRICE"
     * button is pressed
     *
     * The method alters the appropriate button colors,
     * saves the SharedPreference and updates the GameSettings class
     * @param view
     */
    public void rqClicked(View view){
        String preference;

        //Converting the view into a Button object
        Button buttonUsed = null;
        if (view instanceof Button) {
            buttonUsed = (Button) view;
        }
        Log.e("rqClicked","Button Pressed = "+ buttonUsed.getText().toString());

        //changing the saved preferences and the settings used based on the text of the button clicked
        switch (buttonUsed.getText().toString().toUpperCase()){
            case "NEVER":
                GameSettings.setRepeatingTimes(RepeatingTimes.NEVER);
                preference = RepeatingTimes.NEVER.toString();
                Log.i("rqClicked","NEVER button recognised.");
                break;

            case "ONCE":
                GameSettings.setRepeatingTimes(RepeatingTimes.ONCE);
                preference = RepeatingTimes.ONCE.toString();
                Log.i("rqClicked","ONCE button recognised.");
                break;

            case "TWICE":
                GameSettings.setRepeatingTimes(RepeatingTimes.TWICE);
                preference = RepeatingTimes.TWICE.toString();
                Log.i("rqClicked","TWICE button recognised.");
                break;

            case "THRICE":
                GameSettings.setRepeatingTimes(RepeatingTimes.THRICE);
                preference = RepeatingTimes.THRICE.toString();
                Log.i("rqClicked","THRICE button recognised.");
                break;

            default:
                GameSettings.setRepeatingTimes(RepeatingTimes.NEVER);
                preference = RepeatingTimes.NEVER.toString();
                Log.e("rqClicked","There is no "+preference+" button. Repeating Setting changed to NEVER.");
        }
        Log.i("got saved preference","RepeatingTimes Clicked = "+buttonUsed.getText().toString());
        saveRepeatingPreference(preference);
        setVisuals();
    }

    /**
     * Changing button visuals based on what is
     * saved in GameSettings
     */
    public void setVisuals(){

        //altering the CONVERT setting buttons to account for the GameSettings
        switch (GameSettings.getConvertInputType()) {
            case LRT:
                ltrConvertButton.setBackgroundResource(R.color.cool);
                rtlConvertButton.setBackgroundResource(R.color.unselected);
                break;
            case RTL:
                ltrConvertButton.setBackgroundResource(R.color.unselected);
                rtlConvertButton.setBackgroundResource(R.color.alien);
                break;
            default:
                Log.e("setVisuals", "No button selected - no change on convert.");
        }

        //altering the MATH setting buttons
        switch (GameSettings.getMathInputType()) {
            case LRT:
                ltrMathButton.setBackgroundResource(R.color.cool);
                rtlMathButton.setBackgroundResource(R.color.unselected);
                break;
            case RTL:
                ltrMathButton.setBackgroundResource(R.color.unselected);
                rtlMathButton.setBackgroundResource(R.color.alien);
                break;
            default:
                Log.e("setVisuals", "No button selected - no change on math.");
        }

        //altering repeating times buttons
        RepeatingTimes repeatingTimes = GameSettings.getRepeatingTimes();
        if(repeatingTimes == null){//if the repeatingTimes is NULL (not yet set)
            GameSettings.repeatingTimes = RepeatingTimes.NEVER;//set the repeating times to NEVER
            neverButton.setBackgroundResource(R.color.lime);//setting appropriate colors
            onceButton.setBackgroundResource(R.color.unselected);
            twiceButton.setBackgroundResource(R.color.unselected);
            thriceButton.setBackgroundResource(R.color.unselected);
            saveRepeatingPreference(repeatingTimes.toString());
        }
        else {//if repeatingTimes is NOT null (meaning there is already a SharedPreference file)
            switch (GameSettings.repeatingTimes) {
                case NEVER:
                    neverButton.setBackgroundResource(R.color.lime);
                    onceButton.setBackgroundResource(R.color.unselected);
                    twiceButton.setBackgroundResource(R.color.unselected);
                    thriceButton.setBackgroundResource(R.color.unselected);
                    break;

                case ONCE:
                    neverButton.setBackgroundResource(R.color.unselected);
                    onceButton.setBackgroundResource(R.color.rust);
                    twiceButton.setBackgroundResource(R.color.unselected);
                    thriceButton.setBackgroundResource(R.color.unselected);
                    break;

                case TWICE:
                    neverButton.setBackgroundResource(R.color.unselected);
                    onceButton.setBackgroundResource(R.color.unselected);
                    twiceButton.setBackgroundResource(R.color.cool);
                    thriceButton.setBackgroundResource(R.color.unselected);
                    break;

                case THRICE:
                    neverButton.setBackgroundResource(R.color.unselected);
                    onceButton.setBackgroundResource(R.color.unselected);
                    twiceButton.setBackgroundResource(R.color.unselected);
                    thriceButton.setBackgroundResource(R.color.alien);
                    break;

                default:
                    neverButton.setBackgroundResource(R.color.lime);
                    onceButton.setBackgroundResource(R.color.rust);
                    twiceButton.setBackgroundResource(R.color.cool);
                    thriceButton.setBackgroundResource(R.color.alien);
                    Toast.makeText(this, "Please report this error to developers", Toast.LENGTH_LONG).show();
                    Log.e("setVisuals", "No button selected - no change on repeating");
            }
        }

    }

}