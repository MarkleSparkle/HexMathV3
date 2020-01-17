package com.marklesparkle.hexmathv3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.LocaleDisplayNames;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout mainMenuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMenuLayout = findViewById(R.id.mainMenuLayout);

        //setting default game settings
        configureSettings();
    }

    /**
     * The method switches the VISIBLE and GONE layouts
     * to allow the user to alter convert game options
     * before beginning the game
     *
     * @param view
     */
    public void convertClicked(View view){
        Intent i = new Intent(getApplicationContext(), PreGameActivity.class);

        //putting extra of GAME_TYPE as CONVERT
        String gameType = "CONVERT";
        i.putExtra("GAME_TYPE",gameType);

        //start pregame
        startActivity(i);
    }

    /**
     * Opens a separate menu to display game options
     * then sends those details to GameActivity
     * and begins the game
     *
     * @param view
     */
    public void mathClicked(View view){
        Intent i = new Intent(getApplicationContext(), PreGameActivity.class);

        //putting extra of GAME_TYPE as CONVERT
        String gameType = "MATH";
        i.putExtra("GAME_TYPE",gameType);

        //start pregame
        startActivity(i);
    }

    /**
     * Runs this method when the BITWISE OPERATIONS button is pressed
     *
     */
    public void bitwiseOperationsClicked(View view){
        Toast.makeText(this, "This feature will be available in a future update.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Runs this method when the CALCULATE button is pressed
     */
    public void calculateClicked(View view){
        Toast.makeText(this, "This feature will be available in a future update.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Starts the OptionsActivity
     *
     * @param view
     */
    public void optionsClicked(View view){
        Intent i = new Intent(getApplicationContext(), OptionsActivity.class);
        startActivity(i);
    }

    /**
     * This method is run on app start up
     *
     * It gets the SharedPreferences of the user
     * and adjusts all components of the app that is affected
     * by them
     */
    private void configureSettings(){

        //default GameSettings
        GameSettings.setGameType(GameType.ADD);
        GameSettings.setDifficulty(Difficulty.EASY);
        GameSettings.setBase(Base.BINARY);              //TODO - do shared preferences for these settings too?
        GameSettings.setStartingBase(Base.BINARY);
        GameSettings.setEndingBase(Base.BINARY);

        loadSharedPreferences();
    }

    /**
     * Loads the saved Shared Preferences settings and applies them.
     * This is usually done when the app is loaded, but will also be done
     * after settings are changed.
     */
    public void loadSharedPreferences(){
        //initializing SharedPreferences (accessing the file)
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);// 0 is for private mode

        String convert = sharedPreferences.getString("CONVERT", "");//default value NULL
        String math = sharedPreferences.getString("MATH", "");//default value NULL
        String repeatingTimes = sharedPreferences.getString("RT", "");//default value NULL

        Log.d("loadSharedPreferences","CONVERT = "+convert+" | MATH = "+math+" | RT = "+repeatingTimes);

        //giving the SharedPreferences to OptionsActivity and GameActivity
        switch (convert){
            case "ltr":
                GameSettings.setConvertInputType(InputType.LRT);
                break;

            case "rtl":
                GameSettings.setConvertInputType(InputType.RTL);
                break;

            default:
                Log.e("loadSharedPreferences", "No setting "+convert+" for convert InputType. InputType set to LRT");
                GameSettings.setConvertInputType(InputType.LRT);
        }

        switch (math){
            case "ltr":
                GameSettings.setMathInputType(InputType.LRT);
                break;

            case "rtl":
                GameSettings.setMathInputType(InputType.RTL);
                break;

            default:
                Log.e("loadSharedPreferences", "No setting "+math+" for math InputType. InputType set to LTR");
                GameSettings.setMathInputType(InputType.LRT);
        }

        switch(repeatingTimes){
            case "NEVER":
                GameSettings.setRepeatingTimes(RepeatingTimes.NEVER);
                break;
            case "ONCE":
                GameSettings.setRepeatingTimes(RepeatingTimes.ONCE);
                break;
            case "TWICE":
                GameSettings.setRepeatingTimes(RepeatingTimes.TWICE);
                break;
            case "THRICE":
                GameSettings.setRepeatingTimes(RepeatingTimes.THRICE);
                break;
            default:
                Log.e("loadSharedPreferences", "No setting "+repeatingTimes+" for repeatingTimes. RepeatingTimes set to NEVER");
                GameSettings.setRepeatingTimes(RepeatingTimes.NEVER);

        }

        Log.d("loadSharedPreferences","convertInputType: "+GameSettings.getConvertInputType()
            +" | mathInputType = "+GameSettings.getMathInputType() + "repeatingTimes: "+GameSettings.getRepeatingTimes());

    }

}
