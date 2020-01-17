package com.marklesparkle.hexmathv3;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PreGameActivity extends AppCompatActivity {

    ConstraintLayout convertMenuLayout, mathMenuLayout;

    //CONVERT BUTTONS
    Button startingDecimal, startingBinary, startingOctal, startingHex;
    Button endingDecimal, endingBinary, endingOctal, endingHex;
    Button convertEasy, convertMedium, convertHard;

    //MATH BUTTONS
    Button add, subtract;
    Button binary, octal, hexmath;
    Button mathEasy, mathMedium, mathHard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);

        convertMenuLayout = findViewById(R.id.convertMenuLayout);
        mathMenuLayout = findViewById(R.id.mathMenuLayout);

        //CONVERT MENU
        startingDecimal = findViewById(R.id.startingDecimal);
        startingBinary = findViewById(R.id.startingBinary);
        startingOctal = findViewById(R.id.startingOctal);
        startingHex = findViewById(R.id.startingHex);

        endingDecimal = findViewById(R.id.endingDecimal);
        endingBinary = findViewById(R.id.endingBinary);
        endingOctal = findViewById(R.id.endingOctal);
        endingHex = findViewById(R.id.endingHex);

        convertEasy = findViewById(R.id.convertEasy);
        convertMedium = findViewById(R.id.convertMedium);
        convertHard = findViewById(R.id.convertHard);

        //MATH MENU
        add = findViewById(R.id.add);
        subtract = findViewById(R.id.subtract);
        //TODO - add multiply and divide

        mathEasy = findViewById(R.id.mathEasy);
        mathMedium = findViewById(R.id.mathMedium);
        mathHard = findViewById(R.id.mathHard);

        binary = findViewById(R.id.binary);
        octal = findViewById(R.id.octal);
        hexmath = findViewById(R.id.hexmath);


        //setting the visibility of views
        String gameType = getIntent().getStringExtra("GAME_TYPE");
        if(gameType.equals("CONVERT")){
            GameSettings.setGameType(GameType.CONVERT);
            convertMenuLayout.setVisibility(View.VISIBLE);
            mathMenuLayout.setVisibility(View.GONE);
            Log.i("PreGameActivity","Opening convert menu");

        } else { //MATH GAME_TYPE
            mathMenuLayout.setVisibility(View.VISIBLE);
            convertMenuLayout.setVisibility(View.GONE);
            Log.i("PreGameActivity","Opening math menu");

        }


    }

    //MATH MENU

    /**
     * If an operation button from the math menu is clicked
     *
     * The method will adjust the game's operation setting
     * and toggle the colors of the selected and unselected buttons
     *
     * @param view
     */
    public void operationClicked(View view){
        //Converting the view into a Button object
        Button operationButton = null;
        if (view instanceof Button) {
            operationButton = (Button) view;
        }

        String operation = operationButton.getText().toString();

        //all other buttons get gray color, but accent pressed button and change GameVariables.
        switch(operation){
            case "ADD":
                add.setBackgroundResource(R.color.lime);//unselected color
                subtract.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setGameType(GameType.ADD);
                break;

            case "SUBTRACT":
                subtract.setBackgroundResource(R.color.rust);//unselected color
                add.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setGameType(GameType.SUBTRACT);
                break;

            //COMING SOON TODO - add multiply and divide
        }

    }

    /**
     * Called when an attached button is clicked
     * The method changes the game's difficulty settings
     * and alters the colors of the selected and unselected buttons
     * @param view
     */
    public void mathDifficultyClicked(View view){
        //Converting the view into a Button object
        Button difficultyButton = null;
        if (view instanceof Button) {
            difficultyButton = (Button) view;
        }

        String difficulty = difficultyButton.getText().toString();

        //all other buttons get gray color, but accent pressed button and change GameVariables.
        switch(difficulty){
            case "EASY":
                mathEasy.setBackgroundResource(R.color.lime);//unselected color
                mathMedium.setBackgroundResource(R.color.unselected);//unselected color
                mathHard.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setDifficulty(Difficulty.EASY);
                break;

            case "MEDIUM":
                mathMedium.setBackgroundResource(R.color.rust);//unselected color
                mathEasy.setBackgroundResource(R.color.unselected);//unselected color
                mathHard.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setDifficulty(Difficulty.MEDIUM);
                break;

            case "HARD":
                mathHard.setBackgroundResource(R.color.cool);//unselected color
                mathEasy.setBackgroundResource(R.color.unselected);//unselected color
                mathMedium.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setDifficulty(Difficulty.HARD);
                break;
        }
    }

    /**
     * Called when an attached base button is clicked
     * Changes the game's Base variable
     * and alters the colors of the unselected buttons
     * @param view
     */
    public void baseClicked(View view){
        //Converting the view into a Button object
        Button baseButton = null;
        if (view instanceof Button) {
            baseButton = (Button) view;
        }

        String base = baseButton.getText().toString();

        //all other buttons get gray color, but accent pressed button and change GameVariables.
        switch(base){
            case "BINARY":
                binary.setBackgroundResource(R.color.lime);//unselected color
                octal.setBackgroundResource(R.color.unselected);//unselected color
                hexmath.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setBase(Base.BINARY);
                break;

            case "OCTAL":
                octal.setBackgroundResource(R.color.rust);//unselected color
                binary.setBackgroundResource(R.color.unselected);//unselected color
                hexmath.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setBase(Base.OCTAL);
                break;

            case "HEXMATH":
                hexmath.setBackgroundResource(R.color.cool);//unselected color
                binary.setBackgroundResource(R.color.unselected);//unselected color
                octal.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setBase(Base.HEXMATH);
                break;
        }
    }

    // CONVERT MENU

    /**
     * Called when an attached button is clicked
     * The method changes the game's difficulty settings
     * and alters the colors of the selected and unselected buttons
     * @param view
     */
    public void convertDifficultyClicked(View view){
        //Converting the view into a Button object
        Button difficultyButton = null;
        if (view instanceof Button) {
            difficultyButton = (Button) view;
        }

        String difficulty = difficultyButton.getText().toString();

        //all other buttons get gray color, but accent pressed button and change GameVariables.
        switch(difficulty){
            case "EASY":
                convertEasy.setBackgroundResource(R.color.lime);//unselected color
                convertMedium.setBackgroundResource(R.color.unselected);//unselected color
                convertHard.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setDifficulty(Difficulty.EASY);
                break;

            case "MEDIUM":
                convertMedium.setBackgroundResource(R.color.rust);//unselected color
                convertEasy.setBackgroundResource(R.color.unselected);//unselected color
                convertHard.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setDifficulty(Difficulty.MEDIUM);
                break;

            case "HARD":
                convertHard.setBackgroundResource(R.color.cool);//unselected color
                convertEasy.setBackgroundResource(R.color.unselected);//unselected color
                convertMedium.setBackgroundResource(R.color.unselected);//unselected color
                GameSettings.setDifficulty(Difficulty.HARD);
                break;
        }
    }

    /**
     * Called when an attached base button is clicked
     * Changes the game's Base variable
     * and alters the colors of the unselected buttons
     * @param view
     */
    public void startingBaseClicked(View view){
        //Converting the view into a Button object
        Button baseButton = null;
        if (view instanceof Button) {
            baseButton = (Button) view;
        }

        String base = baseButton.getText().toString();

        //all other buttons get gray color, but accent pressed button and change GameVariables.
        switch(base){
            case "BINARY":
                startingBinary.setBackgroundResource(R.color.lime);//unselected color
                startingOctal.setBackgroundResource(R.color.unselected);//unselected color
                startingHex.setBackgroundResource(R.color.unselected);//unselected color
                startingDecimal.setBackgroundResource(R.color.unselected);
                GameSettings.setStartingBase(Base.BINARY);
                break;

            case "OCTAL":
                startingOctal.setBackgroundResource(R.color.rust);//unselected color
                startingBinary.setBackgroundResource(R.color.unselected);//unselected color
                startingHex.setBackgroundResource(R.color.unselected);//unselected color
                startingDecimal.setBackgroundResource(R.color.unselected);
                GameSettings.setStartingBase(Base.OCTAL);
                break;

            case "HEXMATH":
                startingHex.setBackgroundResource(R.color.cool);//unselected color
                startingBinary.setBackgroundResource(R.color.unselected);//unselected color
                startingOctal.setBackgroundResource(R.color.unselected);//unselected color
                startingDecimal.setBackgroundResource(R.color.unselected);
                GameSettings.setStartingBase(Base.HEXMATH);
                break;
            case "DECIMAL":
                startingDecimal.setBackgroundResource(R.color.alien);
                startingBinary.setBackgroundResource(R.color.unselected);
                startingOctal.setBackgroundResource(R.color.unselected);
                startingHex.setBackgroundResource(R.color.unselected);
                GameSettings.setStartingBase(Base.DECIMAL);

        }
    }

    /**
     * Called when an attached base button is clicked
     * Changes the game's Base variable
     * and alters the colors of the unselected buttons
     * @param view
     */
    public void endingBaseClicked(View view){
        //Converting the view into a Button object
        Button baseButton = null;
        if (view instanceof Button) {
            baseButton = (Button) view;
        }

        String base = baseButton.getText().toString();

        //all other buttons get gray color, but accent pressed button and change GameVariables.
        switch(base){
            case "BINARY":
                endingBinary.setBackgroundResource(R.color.lime);//unselected color
                endingOctal.setBackgroundResource(R.color.unselected);//unselected color
                endingHex.setBackgroundResource(R.color.unselected);//unselected color
                endingDecimal.setBackgroundResource(R.color.unselected);
                GameSettings.setEndingBase(Base.BINARY);
                break;

            case "OCTAL":
                endingOctal.setBackgroundResource(R.color.rust);//unselected color
                endingBinary.setBackgroundResource(R.color.unselected);//unselected color
                endingHex.setBackgroundResource(R.color.unselected);//unselected color
                endingDecimal.setBackgroundResource(R.color.unselected);
                GameSettings.setEndingBase(Base.OCTAL);
                break;

            case "HEXMATH":
                endingHex.setBackgroundResource(R.color.cool);//unselected color
                endingBinary.setBackgroundResource(R.color.unselected);//unselected color
                endingOctal.setBackgroundResource(R.color.unselected);//unselected color
                endingDecimal.setBackgroundResource(R.color.unselected);
                GameSettings.setEndingBase(Base.HEXMATH);
                break;

            case "DECIMAL":
                endingDecimal.setBackgroundResource(R.color.alien);
                endingBinary.setBackgroundResource(R.color.unselected);//unselected color
                endingOctal.setBackgroundResource(R.color.unselected);//unselected color
                endingHex.setBackgroundResource(R.color.unselected);
                GameSettings.setEndingBase(Base.DECIMAL);
        }
    }

    // BOTH MENU

    /**
     * Called from the START button in the mathMenuLayout
     * It calls startGame()
     *
     * @param view
     */
    public void startClicked(View view){

        //if starting base and ending base on a convert game are the same
        //don't let the game start
        if(GameSettings.getGameType() == GameType.CONVERT && GameSettings.startingBase == GameSettings.endingBase){
            Toast.makeText(this, "Starting and ending bases must be different", Toast.LENGTH_SHORT).show();
        } else {
            startGame();
            finish();
        }
    }

    /**
     * Method is activated when back buttons are clicked
     * Views switch to show the main menu
     * @param view
     */
    public void backClicked(View view){
        finish();
    }

    /**
     * Sends a Toast to the screen telling the users that
     * this particular feature is coming soon
     * @param view
     */
    public void comingSoonClicked(View view){
        Toast.makeText(this, "This feature is coming in the next update!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Starts the GameActivity
     */
    private void startGame(){
        try {
            Intent k = new Intent(PreGameActivity.this, GameActivity.class);
            startActivity(k);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
