package com.marklesparkle.hexmathv3;

import android.util.Log;
import android.widget.Toast;

public class QuestionGenerator {

    Difficulty difficulty;
    GameType gameType; //describes game type/ operation
    Base base;
    Base startingBase;
    Base endingBase;

    int minValue, maxValue;

    /**
     * One QuestionGenerator is used for each game
     * and generates questions based on the GameSettings that were applied at the start of the game
     */
    public QuestionGenerator(){
        this.gameType= GameSettings.getGameType();
        this.difficulty = GameSettings.getDifficulty();

        if(gameType == GameType.CONVERT){//convert type game needs two bases
            this.startingBase = GameSettings.getStartingBase();
            this.endingBase = GameSettings.getEndingBase();
            this.base = null;

        }else{ //any other game type is just one base
            this.base = GameSettings.getBase();
            this.startingBase = null;
            this.endingBase = null;
        }
    }

    /**
     * Generates the first number, the second number and the answer for the question
     * The method will loop if, in subtraction, the first number is small than the second
     * as that would result in a negative number
     *
     */
    public Question generateQuestion() {
        Question question = null;

        switch(gameType){
            case CONVERT:
                question = generateConvert();
                break;

            case ADD:
                question = generateAdd();
                break;

            case SUBTRACT:
                question = generateSubtract();
                break;

            default:
                Log.e("generateQuestion","No GameType matching: "+gameType);
        }

        return question;
    }

    /**
     * Generates a convert question based on the difficulty and base
     * then returns it back to generateQuestion()
     *
     * @return convertQuestion
     */
    private Question generateConvert(){

        // GETTING MIN and MAX
        switch(startingBase){
            case BINARY:
                switch (difficulty) {
                    case EASY:
                        minValue = 1;//0 is too easy
                        maxValue = 7;//7 is the 3 digit max
                        break;

                    case MEDIUM:
                        minValue = 4;//2 digits is too easy
                        maxValue = 15;//15 is the 4 digit max
                        break;

                    case HARD:
                        minValue = 8;//3 digits is too easy
                        maxValue = 31;//31 is the 5 digit max
                        break;
                }
                break;

            case OCTAL:
                switch (difficulty) {
                    case EASY:
                        minValue = 1;//0 is too easy
                        maxValue = 7;//7 is the 1 digit max
                        break;

                    case MEDIUM:
                        minValue = 8;//1 digits is too easy
                        maxValue = 32;//15 is half the 2 digit max
                        break;

                    case HARD:
                        minValue = 33;//half of 2 digits is too easy
                        maxValue = 63;//63 is the 2 digit max
                        break;
                }
                break;

            case HEXMATH:
                switch (difficulty) {
                    case EASY:
                        minValue = 1;//0 is too easy
                        maxValue = 9;//9 is the last number before letters appear
                        break;

                    case MEDIUM:
                        minValue = 5;//anything less than 4 is too easy
                        maxValue = 16;//16 is the 1 digit max
                        break;

                    case HARD:
                        minValue = 17;//1 digit is too easy
                        maxValue = 88;//88 seems like a good number to cap at
                        break;
                }
            case DECIMAL:
                switch (difficulty) {
                    case EASY:
                        minValue = 1;//0 is too easy
                        maxValue = 9;//9 is the last number before 10s place
                        break;

                    case MEDIUM:
                        minValue = 10;//start of 10s place value
                        maxValue = 20;//a decently difficult number
                        break;

                    case HARD:
                        minValue = 20;//from the decently difficult number
                        maxValue = 50;//to the hard as crap number
                        break;
                }
        }

        //GENERATE VALUE
        int range = maxValue-minValue;
        int questionValue = minValue + (int)(Math.random() * (range+1));//generates [min - max]


        //CONVERT
        String startingNumber = "";
        String endingNumber = "";

        switch(startingBase){
            case BINARY:
                startingNumber = Integer.toBinaryString(questionValue);
                break;

            case OCTAL:
                startingNumber = Integer.toOctalString(questionValue);
                break;

            case HEXMATH:
                startingNumber = Integer.toHexString(questionValue);
                break;
            case DECIMAL:
                startingNumber = ""+questionValue;
                break;
        }

        switch (endingBase){
            case BINARY:
                endingNumber = Integer.toBinaryString(questionValue);
                break;

            case OCTAL:
                endingNumber = Integer.toOctalString(questionValue);
                break;

            case HEXMATH:
                endingNumber = Integer.toHexString(questionValue);
                break;
            case DECIMAL:
                endingNumber = ""+questionValue;
        }

        //returns a new convert Question
        return new Question(startingNumber, endingNumber);
    }

    /**
     * Generates an addition question based on the difficulty and base
     * then returns it back to generateQuestion()
     *
     * @return addQuestion
     */
    private Question generateAdd(){

        // GETTING MIN and MAX
        switch(base){
            case BINARY:
                switch (difficulty) {
                    case EASY:
                        minValue = 1;//0 is too easy
                        maxValue = 3;//3 is the 3 digit max
                        break;

                    case MEDIUM:
                        minValue = 3;//1 digit is too easy
                        maxValue = 7;//7 is the 3 digit max
                        break;

                    case HARD:
                        minValue = 7;//2 digits is too easy
                        maxValue = 15;//15 is the 4 digit max
                        break;
                }
                break;

            case OCTAL:
                switch (difficulty) {
                    case EASY:
                        minValue = 1;//0 is too easy
                        maxValue = 7;//7 is the 1 digit max
                        break;

                    case MEDIUM:
                        minValue = 8;//1 digits is too easy
                        maxValue = 63;//63 is the 2 digit max
                        break;

                    case HARD:
                        minValue = 64;//2 digits is too easy
                        maxValue = 511;//511 is the 3 digit max
                        break;
                }
                break;

            case HEXMATH:
                switch (difficulty) {
                    case EASY:
                        minValue = 1;//0 is too easy
                        maxValue = 9;//9 is the last number before letters appear
                        break;

                    case MEDIUM:
                        minValue = 5;//anything less than 4 is too easy
                        maxValue = 16;//16 is the 1 digit max
                        break;

                    case HARD:
                        minValue = 17;//1 digit is too easy
                        maxValue = 256;//88 seems like a good number to cap at
                        break;
                }
        }

        //GENERATE VALUES
        int range = maxValue-minValue;
        int firstValue = minValue + (int)(Math.random() * (range+1));//generates [min - max]
        int secondValue = minValue + (int)(Math.random() * (range+1));//generates [min - max]
        int answerValue = firstValue + secondValue;

        //CONVERT
        String firstNumber = "";
        String secondNumber = "";
        String answerNumber = "";

        switch(base){
            case BINARY:
                firstNumber = Integer.toBinaryString(firstValue);
                secondNumber = Integer.toBinaryString(secondValue);
                answerNumber = Integer.toBinaryString(answerValue);
                break;

            case OCTAL:
                firstNumber = Integer.toOctalString(firstValue);
                secondNumber = Integer.toOctalString(secondValue);
                answerNumber = Integer.toOctalString(answerValue);
                break;

            case HEXMATH:
                firstNumber = Integer.toHexString(firstValue);
                secondNumber = Integer.toHexString(secondValue);
                answerNumber = Integer.toHexString(answerValue);
                break;
        }

        //returns a new math Question
        return new Question(firstNumber, secondNumber, answerNumber);
    }

    /**
     * Generate a subtraction question based on the difficulty and base
     * then returns is back to generateQuestion()
     *
     * @return subtractQuestion
     */
    private Question generateSubtract(){

        // GETTING MIN and MAX
        switch(base){
            case BINARY:
                switch (difficulty) {
                    case EASY:
                        minValue = 1;//0 is too easy
                        maxValue = 3;//3 is the 3 digit max
                        break;

                    case MEDIUM:
                        minValue = 3;//1 digit is too easy
                        maxValue = 7;//7 is the 3 digit max
                        break;

                    case HARD:
                        minValue = 7;//2 digits is too easy
                        maxValue = 15;//15 is the 4 digit max
                        break;
                }
                break;

            case OCTAL:
                switch (difficulty) {
                    case EASY:
                        minValue = 1;//0 is too easy
                        maxValue = 7;//7 is the 1 digit max
                        break;

                    case MEDIUM:
                        minValue = 8;//1 digits is too easy
                        maxValue = 63;//63 is the 2 digit max
                        break;

                    case HARD:
                        minValue = 64;//2 digits is too easy
                        maxValue = 511;//511 is the 3 digit max
                        break;
                }
                break;

            case HEXMATH:
                switch (difficulty) {
                    case EASY:
                        minValue = 1;//0 is too easy
                        maxValue = 9;//9 is the last number before letters appear
                        break;

                    case MEDIUM:
                        minValue = 5;//anything less than 4 is too easy
                        maxValue = 16;//16 is the 1 digit max
                        break;

                    case HARD:
                        minValue = 17;//1 digit is too easy
                        maxValue = 256;//88 seems like a good number to cap at
                        break;
                }
        }

        //GENERATE VALUES
        int firstValue = 0;
        int secondValue = 0;
        int range;

        do {
            range = maxValue - minValue;
            firstValue = minValue + (int) (Math.random() * (range + 1));//generates [min - max]
            secondValue = minValue + (int) (Math.random() * (range + 1));//generates [min - max]
        }while(firstValue < secondValue);//firstValue must be greater than the secondValue
        int answerValue = firstValue - secondValue;

        //CONVERT
        String firstNumber = "";
        String secondNumber = "";
        String answerNumber = "";

        switch(base){
            case BINARY:
                firstNumber = Integer.toBinaryString(firstValue);
                secondNumber = Integer.toBinaryString(secondValue);
                answerNumber = Integer.toBinaryString(answerValue);
                break;

            case OCTAL:
                firstNumber = Integer.toOctalString(firstValue);
                secondNumber = Integer.toOctalString(secondValue);
                answerNumber = Integer.toOctalString(answerValue);
                break;

            case HEXMATH:
                firstNumber = Integer.toHexString(firstValue);
                secondNumber = Integer.toHexString(secondValue);
                answerNumber = Integer.toHexString(answerValue);
                break;
        }

        return new Question(firstNumber, secondNumber, answerNumber);
    }


    private int fromDecimalTo(int number, int base) {//converts a decimal number to a base

        String string = Integer.toString(number,base);//converts base
        int result = Integer.parseInt(string);//parses to an int

        return result;
    }
}