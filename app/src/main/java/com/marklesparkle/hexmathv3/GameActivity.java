package com.marklesparkle.hexmathv3;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    ConstraintLayout gameConstraint, gameOverConstraint;

    TableLayout hexKeyboard, octKeyboard, decKeyboard;
    LinearLayout binKeyboard;

    TextView titleText;
    TextView timerText;
    TextView firstNumberText, secondNumberText, signAndLine;
    TextView firstBaseText, secondBaseText, answerBaseText;
    TextView answerText;

    Button answerButton;

    GameType gameType;
    QuestionGenerator questionGenerator;
    Question currentQuestion;

    ArrayList<Question> questionHistory;

    long startTime;//the time in which the clock started
    long gameTime;//the length of the game
    CountDownTimer countDownTimer;
    long timeStamp = 0;

    TextView difficultyText, questionsAnsweredText, questionsCorrectText, secondsPerQuestionText, percentCorrectText;

    LinearLayout specificStatsLayout;

    final int NUMBER_TEXT_SIZE = 24;//size for all normal numbers
    final int BASE_TEXT_SIZE = 14;//size for all base numbers

    TableRow.LayoutParams lpTR, lpTL, layoutParamsForNumbers, layoutParamsForTitles, layoutParamsForBases;

    int pixelsPerDP;

    boolean previousQuestionCorrect = true;//initialized as true so that the first question would be received without issue
    int repeatedMax;
    int repeatedCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /* Initializing Variables */

        gameConstraint = findViewById(R.id.gameConstraint);
        gameOverConstraint = findViewById(R.id.gameOverConstraint);
        gameConstraint.setVisibility(View.VISIBLE);
        gameOverConstraint.setVisibility(View.GONE);

        Base base = GameSettings.getBase();

        binKeyboard = findViewById(R.id.BinKeyboard);
        octKeyboard = findViewById(R.id.OctKeyboard);
        hexKeyboard = findViewById(R.id.HexKeyboard);
        decKeyboard = findViewById(R.id.DecKeyboard);
        // heyKeyboard can never be set to View.GONE because
        // the other keyboards rely on its height

        //setting the other keyboards' visibility settings
        Base gameBase = null;
        if(GameSettings.gameType == GameType.CONVERT)
            gameBase = GameSettings.getEndingBase();
        else
            gameBase = GameSettings.getBase();

        //the keyboard shown is based on what entry is needed
        switch(gameBase){
            case BINARY:
                binKeyboard.setVisibility(View.VISIBLE);
                octKeyboard.setVisibility(View.INVISIBLE);
                hexKeyboard.setVisibility(View.INVISIBLE);
                decKeyboard.setVisibility(View.GONE);
                Log.i("GameActivity","binKeyboard X:"+binKeyboard.getX()+" Y:"+binKeyboard.getY());
                break;
            case OCTAL:
                octKeyboard.setVisibility(View.VISIBLE);
                binKeyboard.setVisibility(View.GONE);//bin is in front so it must be GONE or else it will interfere
                hexKeyboard.setVisibility(View.INVISIBLE);
                decKeyboard.setVisibility(View.GONE);
                Log.i("GameActivity","octKeyboard is "+octKeyboard.getVisibility());
                break;
            case HEXMATH:
                hexKeyboard.setVisibility(View.VISIBLE);
                binKeyboard.setVisibility(View.GONE);// bin and octal are in front so they must be set to GONE
                octKeyboard.setVisibility(View.GONE);// or they will interfere with input
                decKeyboard.setVisibility(View.GONE);
                Log.i("GameActivity","hexKeyboard is "+hexKeyboard.getVisibility());
                break;
            case DECIMAL:
                decKeyboard.setVisibility(View.VISIBLE);
                binKeyboard.setVisibility(View.GONE);//bin is in front so it must be GONE or else it will interfere
                hexKeyboard.setVisibility(View.INVISIBLE);
                octKeyboard.setVisibility(View.GONE);
                Log.i("GameActivity","octKeyboard is "+octKeyboard.getVisibility());
        }

        titleText = findViewById(R.id.titleText);
        timerText = findViewById(R.id.timerText);

        firstNumberText = findViewById(R.id.firstNumberTextView);
        secondNumberText = findViewById(R.id.secondNumberTextView);
        signAndLine = findViewById(R.id.signAndLineTextView);
        answerText = findViewById(R.id.answerText);

        firstBaseText = findViewById(R.id.firstBaseText);
        secondBaseText = findViewById(R.id.secondBaseText);
        answerBaseText = findViewById(R.id.answerBaseText);
        setBaseTexts();

        answerButton = findViewById(R.id.answerButton);

        gameType = GameSettings.getGameType();
        questionGenerator = new QuestionGenerator();

        questionHistory = new ArrayList<>();

        difficultyText = findViewById(R.id.difficultyText);
        questionsAnsweredText = findViewById(R.id.questionsAnsweredText);
        questionsCorrectText = findViewById(R.id.questionsCorrectText);
        secondsPerQuestionText = findViewById(R.id.secondsPerQuestionText);
        percentCorrectText = findViewById(R.id.percentCorrectText);

        specificStatsLayout= findViewById(R.id.specificStatsLayout);

        /* Initializing the LayoutParams for different Objects*/
        //layoutParamsTableRow
        lpTR = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        //layoutParamsTableLayout
        lpTL = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        //layoutParamsForNumbers
        layoutParamsForNumbers = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 3.0f);
        layoutParamsForNumbers.gravity = Gravity.LEFT;//setting layout for this and future text views as center_horizontal;

        //layoutParamsForTitles
        layoutParamsForTitles = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        layoutParamsForTitles.gravity = Gravity.RIGHT;

        //layoutParamsForBases
        layoutParamsForBases = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);


        Resources r = this.getResources();
        pixelsPerDP = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1,
                r.getDisplayMetrics()
        );

        //setting repeatedMax based on GameSettings
        switch (GameSettings.getRepeatingTimes()){
            case NEVER:
                repeatedMax = 0;
                break;
            case ONCE:
                repeatedMax = 1;
                break;
            case TWICE:
                repeatedMax = 2;
                break;
            case THRICE:
                repeatedMax = 3;
        }
        repeatedCounter = 0;

        //starting timer and
        startTimer();
        //generating a question right away
        displayNextQuestion();
    }

    //TODO - something about when the activity is stopped

    /**
     * Starts and maintains the timer as it runs
     * This method updates the clock, and calls
     * gameOver() when the time is up
     */
    private void startTimer() {
        startTime = System.currentTimeMillis();//getting the game start time

        gameTime = 60000;
        //creating a new CountDownTimer with X seconds level, where onTick will be called every 1000 millis
        countDownTimer = new CountDownTimer(gameTime, 1000) {
            @Override
            public void onTick(long l) {
                //every second that passes - update the timer
                timeStamp = l;
                int secondsLeft = (int) timeStamp / 1000;
                timerText.setText(""+secondsLeft);
            }

            @Override
            public void onFinish() {
                gameOver();
            }
        }.start();//start timer right away
    }

    /**
     * Loads and displays the next question
     * runs both at the start of the activity
     * and after each question is answered
     */
    public void displayNextQuestion() {

        Log.d("DEBUG","previousQuestionCorrect:"+previousQuestionCorrect+" | repeatedCounter:"+repeatedCounter+" | repeatedMax:"+repeatedMax);

        //handling whether to repeat a question or not
        if(!(!previousQuestionCorrect && repeatedCounter < repeatedMax)){ //previous question is correct or max is reached on incorrect answers
            //getting a new question
            currentQuestion = questionGenerator.generateQuestion();
            repeatedCounter = 0;//resetting counter
            Log.d("DEBUG","DisplayQuestion - Getting new question");
        }
        else{ //previous questions is incorrect

            // duplicates a question based on the kind of GameType
            switch (GameSettings.getGameType()){
                case CONVERT:
                    currentQuestion = new Question(currentQuestion.getStartingNumber(), currentQuestion.getEndingNumber());
                    //creates a new Question with currentQuestion's values and assigns it to currentQuestion variable
                    //this ensures that we're making a new Question object instead of copying the previous object's pointer

                default://if the GameType is MATH
                    currentQuestion = new Question(currentQuestion.getFirstNumber(), currentQuestion.getSecondNumber(), currentQuestion.getAnswerNumber());
                    //creates a new Question with currentQuestion's values and assigns it to currentQuestion variable
                    //this ensures that we're making a new Question object instead of copying the previous object's pointer
            }
        }


        //the way the question is displayed is displayed on whether
        if (gameType == GameType.CONVERT) {

            firstNumberText.setText("");
            secondNumberText.setText(currentQuestion.getStartingNumber().toUpperCase());
            //removing the sign from the previous game and tacking on an empty space
            signAndLine.setText(signAndLine.getText().toString().substring(1));

        } else { //any other game mode uses the first, second, answer style layout

            firstNumberText.setText(currentQuestion.getFirstNumber().toUpperCase());
            secondNumberText.setText(currentQuestion.getSecondNumber().toUpperCase());

            String operation = null;
            switch (gameType){
                case CONVERT:
                    operation = "";//literally nothing... on purpose
                    break;
                case ADD:
                    operation = "+";
                    break;
                case SUBTRACT:
                    operation = "-";
                    break;

                //coming soon - TODO - add multiply and divide
            }

            //removing the sign from the previous game and tacking on the new operation sign
            signAndLine.setText(operation + signAndLine.getText().toString().substring(1));

        }
    }

    /**
     * Called when the answer button is pressed
     * Method analyzes the input and adjusts background colors
     * based on the correctness of the input
     */
    public void answerClicked(View view){
        //adding question to questionHistory
        questionHistory.add(currentQuestion);
        String answer = answerText.getText().toString();

        //removing all leading zeros
        while(answer.charAt(0) == '0'){
            answer = answer.substring(1);//taking off the largest place value
        }

        if(gameType == GameType.CONVERT){//CONVERT TYPE GAMES
            currentQuestion.setUsersAnswer(answer);

            //if the answer is correct
            if(answer.equalsIgnoreCase(currentQuestion.getEndingNumber())){
                answerButton.setBackgroundResource(R.color.lime);
                currentQuestion.setAnswerIsCorrect(true);//adding the correctness value for end-of-game reporting
                previousQuestionCorrect = true;
                repeatedCounter = 0; //reset repeated counter for next question
                Log.d("DEBUG","AnswerClicked - Answer is correct");

            } else{ //answer is incorrect
                answerButton.setBackgroundResource(R.color.rust);
                currentQuestion.setAnswerIsCorrect(false);//adding the correctness value for end-of-game reporting
                previousQuestionCorrect = false;
                repeatedCounter++;//increment repeated counter
                Log.d("DEBUG","AnswerClicked - Answer is incorrect");

            }

        } else { //MATH TYPE GAMES - (ADD, SUBTRACT, MULTIPLY, DIVIDE)
            currentQuestion.setUsersAnswer(answer);

            //if the answer is correct
            if(answer.equalsIgnoreCase(currentQuestion.getAnswerNumber())){
                answerButton.setBackgroundResource(R.color.lime);
                currentQuestion.setAnswerIsCorrect(true);//adding the correctness value for end-of-game reporting
                previousQuestionCorrect = true;
                repeatedCounter=0;//reset repeatedCounter
                Log.d("DEBUG","AnswerClicked - Answer is correct");

            } else { //answer is incorrect
                answerButton.setBackgroundResource(R.color.rust);
                currentQuestion.setAnswerIsCorrect(false);//adding the correctness value for end-of-game reporting
                previousQuestionCorrect = false;
                repeatedCounter++;//increment repeated counter
                Log.d("DEBUG","AnswerClicked - Answer is incorrect");
            }
        }

        answerText.setText(""); //resetting answerText display
        displayNextQuestion();
    }

    /**
     * This method is called when the game timer runs out
     * It switches the displays (to no longer allow game play)
     * and displays the game over statistics
     */
    private void gameOver() {

        /* Initializing other variables*/
        //enables the game over views for game over statistics
        gameConstraint.setVisibility(View.GONE);
        gameOverConstraint.setVisibility(View.VISIBLE);

        //* SIMPLE STATISTICS *//
        double questionsAnswered = questionHistory.size();
        questionsAnsweredText.setText("" + (int) questionsAnswered);

        int questionsCorrect = 0;
        for (int h = 0; h < questionsAnswered; h++) {
            if (questionHistory.get(h).isAnswerCorrect())//if the answer was correct
                questionsCorrect++;
            //TODO - fix this
        }

        //setting the Title TextView
        if(GameSettings.gameType == GameType.CONVERT){
            titleText.setText(GameSettings.getStartingBase() +" TO "+GameSettings.getEndingBase());
        }
        else {//math modes
            titleText.setText(GameSettings.gameType + "ING " + GameSettings.base);
        }

        difficultyText.setText(""+GameSettings.difficulty);
        questionsCorrectText.setText("" + questionsCorrect);
        secondsPerQuestionText.setText(String.format("%.2f", (gameTime/1000) / questionsAnswered) + " secs");// game time in seconds over the number of questions answered
        percentCorrectText.setText(String.format("%.2f",(questionsCorrect/questionsAnswered)*100)+"%");

        //loops the number of questions
        for(int i = 0; i < questionHistory.size(); i++){

            //runs method for every question
            fillGameOverScroll(questionHistory.get(i),i+1);
        }

    }

    /**
     * This method fills the ScrollView that appears after the game
     */
    public void fillGameOverScroll(Question question, int questionNumber){

        /* Creating views here */

        //Creating the parent LinearLayout (child of ScrollView)
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setId(View.generateViewId());
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayoutParams.setMargins(0, dpToPixel(10), 0, dpToPixel(10));
        linearLayoutParams.setMarginStart(dpToPixel(30));
        linearLayoutParams.setMarginEnd(dpToPixel(30));
        parentLayout.setLayoutParams(linearLayoutParams);

        //Answer Correctness
        if (question.isAnswerCorrect())//if the question is correct
            parentLayout.setBackgroundResource(R.color.offLime);
            //set the background to positive connotation
        else
            parentLayout.setBackgroundResource(R.color.offRust);
        //set the background to negative connotation

        specificStatsLayout.addView(parentLayout);


        //Creating the Question Title TextView
        TextView questionTitleText = new TextView(this);
        questionTitleText.setId(View.generateViewId());
        LinearLayout.LayoutParams questionTitleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        questionTitleText.setLayoutParams(questionTitleParams);
        questionTitleText.setText("Question "+questionNumber);
        questionTitleText.setGravity(Gravity.CENTER_HORIZONTAL);
        questionTitleText.setTextSize(24);
        questionTitleText.setTextColor(Color.rgb(204,204,204));
        parentLayout.addView(questionTitleText);


        //Creating the Line ImageView
        ImageView lineImage = new ImageView(this);
        lineImage.setId(View.generateViewId());
        LinearLayout.LayoutParams lineImageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lineImageParams.setMargins(dpToPixel(16), 0,dpToPixel(16), 0);
        lineImage.setLayoutParams(lineImageParams);
        lineImage.setImageResource(R.drawable.line);
        parentLayout.addView(lineImage);


        //Creating the ConstraintLayout child
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setId(View.generateViewId());
        ConstraintLayout.LayoutParams constraintLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        //TODO - change previous line to work with MATCH CONSTRAINT (create a constraint between constraintLayout and its parent
        constraintLayoutParams.setMargins(dpToPixel(16), dpToPixel(5), dpToPixel(16), dpToPixel(5));
        constraintLayout.setLayoutParams(constraintLayoutParams);
        parentLayout.addView(constraintLayout);


        String baseText;
        switch(GameSettings.getBase()){
            case BINARY:
                baseText = "2";
                break;
            case OCTAL:
                baseText = "8";
                break;
            case HEXMATH:
                baseText = "16";
                break;
            default:
                baseText = "0";
        }

        String startingBaseText;
        switch (GameSettings.getStartingBase()) {
            case BINARY:
                startingBaseText = "2";
                break;
            case OCTAL:
                startingBaseText = "8";
                break;
            case HEXMATH:
                startingBaseText = "16";
                break;
            case DECIMAL:
                startingBaseText = "10";
                break;
            default:
                startingBaseText = "10";
        }

        String endingBaseText;
        switch (GameSettings.getEndingBase()) {
            case BINARY:
                endingBaseText = "2";
                break;
            case OCTAL:
                endingBaseText = "8";
                break;
            case HEXMATH:
                endingBaseText = "16";
                break;
            case DECIMAL:
                endingBaseText = "10";
                break;
            default:
                endingBaseText = "10";
        }


        String[] strings = new String[11];
        for(int i=0; i<strings.length; i++){

            if(GameSettings.gameType == GameType.CONVERT){
                //applied fillGameOverScroll settings for CONVERT game types
                switch (i){
                    //no case 0 or 1 because there's only one number before the line in CONVERT

                    case 2:
                        strings[i] = ""+question.getStartingNumber().toUpperCase();
                        break;

                    case 3:
                        strings[i] = ""+startingBaseText;
                        break;

                    case 4:
                        strings[i] = "_________";
                        break;

                    case 5:
                        strings[i] = "Your Ans:";
                        break;

                    case 6:
                        strings[i] = question.getUsersAnswer().toUpperCase();
                        break;

                    case 7:
                        strings[i] = endingBaseText;
                        break;

                    case 8:
                        strings[i] = "Correct Ans:";
                        break;

                    case 9:
                        strings[i] = question.getEndingNumber().toUpperCase();
                        break;

                    case 10:
                        strings[i] = endingBaseText;
                }

            } else {
                //applied fillGameOverScroll settings for MATH game types
                switch (i){
                    case 0:
                        strings[i] = ""+question.getFirstNumber().toUpperCase();
                        break;

                    case 1:
                        strings[i] = ""+baseText;
                        break;

                    case 2:
                        strings[i] = ""+question.getSecondNumber().toUpperCase();
                        break;

                    case 3:
                        strings[i] = ""+baseText;
                        break;

                    case 4:
                        String operator;
                        if(GameSettings.gameType == GameType.ADD)
                            operator = "+";
                        else
                            operator = "-";

                        strings[i] = operator+"_________";
                        break;

                    case 5:
                        strings[i] = "Your Ans:";
                        break;

                    case 6:
                        strings[i] = ""+question.getUsersAnswer().toUpperCase();
                        break;

                    case 7:
                        strings[i] = ""+baseText;
                        break;

                    case 8:
                        strings[i] = "Correct Ans:";
                        break;

                    case 9:
                        strings[i] = ""+question.getAnswerNumber().toUpperCase();
                        break;

                    case 10:
                        strings[i] = ""+baseText;
                        break;
                }
            }

        }

        /* Programmatically generating the views in ConstraintLayout */

        TextView textView = null;
        int [] ids = new int[11];
        for(int i=0; i<11; i++) {//loops through 11 times to generate 10 TextViews
            //creating a TextView
            textView = new TextView(this);
            textView.setId(View.generateViewId());
            ids[i] = textView.getId(); //adding the TextView id to an array to use for constraint setting

            //setting unique attributes for each textView (Text, size, and potential overflow cases)
            switch(i){
                case 0://first number

                    if(GameSettings.gameType == GameType.CONVERT) {
                        textView.setTextSize(BASE_TEXT_SIZE);//makes the distance between the line and the first line smaller
                        textView.setText("");//no text
                    }
                    else {
                        textView.setText(strings[i]);
                        textView.setTextSize(NUMBER_TEXT_SIZE);
                    }
                    break;

                case 1://first base
                    textView.setTextSize(BASE_TEXT_SIZE);//base text size

                    if(GameSettings.gameType == GameType.CONVERT)
                        textView.setText("");//no text on convert game types
                    else
                        textView.setText(strings[i]);
                    break;

                case 2://second number
                    textView.setText(strings[i]);
                    textView.setTextSize(NUMBER_TEXT_SIZE);
                    break;

                case 3://second base
                    textView.setText(strings[i]);
                    textView.setTextSize(BASE_TEXT_SIZE);

                    break;

                case 4://sign and line
                    textView.setText(strings[i]);
                    textView.setTextSize(NUMBER_TEXT_SIZE);

                    break;

                case 5://"Your Ans:" (display)
                    textView.setText(strings[i]);
                    textView.setTextSize(NUMBER_TEXT_SIZE);

                    break;

                case 6://[Player's Answer] (number)
                    textView.setText(strings[i]);
                    textView.setTextSize(NUMBER_TEXT_SIZE);

                    String text = textView.getText().toString();
                    if(text.length() > 8){
                        text = "..." + text.substring(text.length() - 7);
                        //cuts off the bits after the 8th bit and adds an ellipsize
                        textView.setText(text);
                    }
                    textView.setMaxLines(1);//only ONE line is allowed (so that that text will not wrap
                    break;

                case 7://Player's Answer (base)
                    textView.setText(strings[i]);
                    textView.setTextSize(BASE_TEXT_SIZE);

                    break;

                case 8://"Correct Ans:"
                    textView.setText(strings[i]);
                    textView.setTextSize(NUMBER_TEXT_SIZE);

                    break;

                case 9://[Correct Answer] (number)
                    textView.setText(strings[i]);
                    textView.setTextSize(NUMBER_TEXT_SIZE);

                    String correctText = textView.getText().toString();
                    if(correctText.length() > 8){
                        correctText = ".." + correctText.substring(correctText.length() - 7);
                        //cuts off the bits after the 8th bit and adds an ellipsize
                        textView.setText(correctText);
                    }
                    break;

                case 10://Correct Answer BASE
                    textView.setText(strings[i]);
                    textView.setTextSize(BASE_TEXT_SIZE);

                    break;

            }//end of switch

            //adding the programmatically generated TextView to the ConstraintLayout
            constraintLayout.addView(textView);

        }//end of for


        /* Setting Constraints to the previously generated TextViews */

        final int NUMBER_MARGIN_RIGHT = 80;

        //Creating the ConstraintSet to use for the ConstraintLayout
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);//this initializes the ConstraintSet with ConstraintLayout's layout params.

        for(int i = 0; i<11; i++){

            switch (i){
                case 0://first TextView (First Number)
                    //constraintSet.connect(ids[i], ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START);
                    //connecting the First Number with the LEFT of the ConstraintLayout

                    constraintSet.connect(ids[i], ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, NUMBER_MARGIN_RIGHT);
                    //connecting the First Number with the RIGHT of the ConstraintLayout
                    break;

                case 1://First Number BASE
                    constraintSet.connect(ids[i], ConstraintSet.START, ids[i-1], ConstraintSet.END, dpToPixel(4));
                    //connecting the First Number BASE to the RIGHT of the First Number (margin of 4)

                    constraintSet.connect(ids[i], ConstraintSet.BOTTOM, ids[i-1], ConstraintSet.BOTTOM);
                    //connecting the BOTTOM of the First Number Base to the BOTTOM of the First Number
                    break;

                case 2://Second Number
                    constraintSet.connect(ids[i], ConstraintSet.TOP, ids[i-2], ConstraintSet.BOTTOM);
                    //connecting the Second Number to the BOTTOM of the First Number

                    constraintSet.connect(ids[i], ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, NUMBER_MARGIN_RIGHT);
                    //connecting the First Number with the RIGHT of the ConstraintLayout
                    break;

                case 3://Second Number BASE
                    constraintSet.connect(ids[i], ConstraintSet.START, ids[i-1], ConstraintSet.END, dpToPixel(4));
                    //connecting the Second Number BASE to the RIGHT of the Second Number (margin of 4)

                    constraintSet.connect(ids[i], ConstraintSet.BOTTOM, ids[i-1], ConstraintSet.BOTTOM);
                    //connecting the BOTTOM of the Second Number BASE to the BOTTOM of the Second Number
                    break;

                case 4://SignAndLine
                    constraintSet.connect(ids[i], ConstraintSet.END, ids[i-2],ConstraintSet.END);
                    //connecting the END of the SignAndLine to the END of the Second Number

                    constraintSet.connect(ids[i], ConstraintSet.TOP, ids[i-4], ConstraintSet.BOTTOM, dpToPixel(8));
                    //connecting the TOP of SignAndLine to the BOTTOM of First Number (with an 8dp margin for formatting)
                    //This will assure that SignAndLine is right under Second Number
                    break;

                case 5://"Your Ans:" (display)
                    constraintSet.connect(ids[i], ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, dpToPixel(8));
                    //connecting the START (left) of "Your Ans:" to the START of the ConstraintLayout

                    constraintSet.connect(ids[i], ConstraintSet.TOP, ids[i-3], ConstraintSet.BOTTOM, dpToPixel(8));
                    //connecting the TOP of "Your Ans" to the BOTTOM of Second Number (with a margin of 8dp for formatting)
                    break;

                case 6://[Player's Answer]
                    constraintSet.connect(ids[i], ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, NUMBER_MARGIN_RIGHT);
                    //connecting the END of the [Player's Answer] to the END of the ConstraintLayout (margin of x for formatting)

                    constraintSet.connect(ids[i], ConstraintSet.TOP, ids[i-4], ConstraintSet.BOTTOM, dpToPixel(8));
                    //connecting the TOP of the [Player's Answer] to the BOTTOM of the Second Number (margin of 8)
                    break;

                case 7://Player's Answer BASE
                    constraintSet.connect(ids[i], ConstraintSet.START, ids[i-1], ConstraintSet.END, dpToPixel(4));
                    //connecting the Player's Answer BASE to the RIGHT of the Player's Answer (margin of 4)

                    constraintSet.connect(ids[i], ConstraintSet.BOTTOM, ids[i-1], ConstraintSet.BOTTOM, dpToPixel(4));
                    //connecting the BOTTOM of the Player's Answer BASE to the BOTTOM of Player's Answer
                    break;

                case 8://"Correct Ans:" (display)
                    constraintSet.connect(ids[i], ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, dpToPixel(8));
                    //connecting the START (left) of "Correct Ans:" to the START of the ConstraintLayout

                    constraintSet.connect(ids[i], ConstraintSet.TOP, ids[i-3], ConstraintSet.BOTTOM, dpToPixel(4));
                    //connecting the TOP of the "Correct Ans" to the BOTTOM of "Your Ans:" (with a margin of 4dp)
                    break;

                case 9://[Correct Answer]
                    constraintSet.connect(ids[i], ConstraintSet.TOP, ids[i-3], ConstraintSet.BOTTOM, dpToPixel(4));
                    //connecting the TOP of [Correct Answer] to the BOTTOM of [Player's Answer] (with a margin of 4dp)

                    constraintSet.connect(ids[i], ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, NUMBER_MARGIN_RIGHT);
                    //connecting the END of the [Correct Answer] to the END of the ConstraintLayout (margin of x for formatting)
                    break;

                case 10://Correct Answer BASE
                    constraintSet.connect(ids[i], ConstraintSet.START, ids[i-1], ConstraintSet.END, dpToPixel(4));
                    //connecting the START of the Correct Answer BASE to the END of [Correct Answer](with a margin of 4dp)

                    constraintSet.connect(ids[i], ConstraintSet.BOTTOM, ids[i-1], ConstraintSet.BOTTOM);
                    //connecting the BOTTOM of Correct Answer BASE to the BOTTOM of [Correct Answer]
                    break;
            }//end of switch

        }//end of for


        //applying all the constraint connections to constraintLayout
        constraintSet.applyTo(constraintLayout);
        Log.d("fillGameOverScroll","Applied Constraints");
    }

    private int dpToPixel(int dp){
        int pixels;

        pixels = dp * pixelsPerDP;

        return pixels;
    }

    /**
     * Method is called when the Finish button is pressed
     * The button closes the GameActivity
     * @param view
     */
    public void finishButtonClicked(View view){
        finish();
    }

    /**
     * Method users the text from the view
     * and adding its value to the answerEditText
     * @param view
     */
    public void KeyboardClick(View view) {

        //method will only add characters if there is less than 10 already
        if(answerText.getText().length() < 10) {

            //Converting the view into a Button object
            Button buttonUsed = null;
            if (view instanceof Button) {
                buttonUsed = (Button) view;
            }

            //getting Button's text
            String buttonText = buttonUsed.getText().toString();
            Log.i("KeyboardClick", "buttonText: " + buttonText);

            if(GameSettings.gameType == GameType.CONVERT){//CONVERT game types
                //if the input is Right-to-Left
                if (GameSettings.getConvertInputType() == InputType.RTL) {
                    //add the button text before the rest
                    answerText.setText(buttonText + answerText.getText());
                } else {
                    //add the button text after the rest
                    answerText.setText(answerText.getText() + buttonText);
                }
            } else {//MATH game types
                //if the input is Right-to-Left
                if (GameSettings.getMathInputType() == InputType.RTL) {
                    //add the button text before the rest
                    answerText.setText(buttonText + answerText.getText());
                } else {
                    //add the button text after the rest
                    answerText.setText(answerText.getText() + buttonText);
                }
            }
        }
    }

    /**
     * If "BACKSPACE" is pressed then reduce the editText by one
     * from either the right or the left side
     *
     * @param view
     */
    public void backspaceClicked(View view){

        String answerString = answerText.getText().toString();
        String newText= "";

        //code only runs if there is characters in the text
        if(answerString.length()>0){

            //removal depends on game settings surrounding InputType
            if(GameSettings.gameType == GameType.CONVERT){//CONVERT GAME TYPES
                if (GameSettings.getConvertInputType() == InputType.RTL) {//RTL input
                    newText = answerString.substring(1, answerString.length());
                    //removed one character from the left

                } else {//LRT input
                    newText = answerString.substring(0, answerString.length() - 1);
                    //removed one character from the right
                }
            } else {//MATH GAME TYPES
                if (GameSettings.getMathInputType() == InputType.RTL) {//RTL input
                    newText = answerString.substring(1, answerString.length());
                    //removed one character from the left

                } else {//LRT input
                    newText = answerString.substring(0, answerString.length() - 1);
                    //removed one character from the right
                }
            }


            //setting the alternated string
            answerText.setText(newText);
        }
    }

    /**
     * Setting the base indicators
     */
    private void setBaseTexts(){

        if(GameSettings.getGameType() == GameType.CONVERT){
            String startingBase = null;
            String endingBase = null;

            Log.d("GameActivity CONVERT","Starting base:"+GameSettings.getStartingBase()+" Ending base:"+GameSettings.getEndingBase()+" Base:"+GameSettings.getBase());

            switch (GameSettings.getStartingBase()){
                case BINARY:
                    startingBase = "2";
                    break;
                case OCTAL:
                    startingBase = "8";
                    break;
                case HEXMATH:
                    startingBase = "16";
                    break;
                case DECIMAL:
                    startingBase = "10";
                    break;
            }

            switch (GameSettings.getEndingBase()){
                case BINARY:
                    endingBase = "2";
                    break;
                case OCTAL:
                    endingBase = "8";
                    break;
                case HEXMATH:
                    endingBase = "16";
                    break;
                case DECIMAL:
                    endingBase = "10";
                    break;
            }

            firstBaseText.setText("");//nothing on first row
            secondBaseText.setText(startingBase);
            answerBaseText.setText(endingBase);

        } else{//GameType is MATH
            String base = null;

            switch (GameSettings.getBase()){
                case BINARY:
                    base = "2";
                    break;
                case OCTAL:
                    base = "8";
                    break;
                case HEXMATH:
                    base = "16";
                    break;
            }

            firstBaseText.setText(base);
            secondBaseText.setText(base);
            answerBaseText.setText(base);
        }
    }
}
