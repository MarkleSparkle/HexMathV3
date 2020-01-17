package com.marklesparkle.hexmathv3;

public class Question {

    /* Instance Variables */

    String firstNumber, secondNumber, answerNumber;
    String startingNumber, endingNumber;
    String usersAnswer;
    boolean answerIsCorrect;


    /* Constructors */

    /**
     * Math Constructor
     * This constructor is used for creating the Questions with math problems
     * because this question only has to deal with one base
     * and therefore had different parameters
     *
     * @param firstNumber
     * @param secondNumber
     * @param answerNumber
     */
    public Question(String firstNumber, String secondNumber, String answerNumber){

        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
        this.answerNumber = answerNumber;

        this.startingNumber = null;
        this.endingNumber = null;

        this.usersAnswer = null;
        this.answerIsCorrect = false;
    }

    /**
     * Convert Constructor
     * This constructor is used for creating the Questions with convert problems
     * this is because convert questions only need 2 numbers
     *
     * @param startingNumber
     * @param endingNumber
     */
    public Question(String startingNumber, String endingNumber){
        this.firstNumber = null;
        this.secondNumber = null;
        this.answerNumber = null;

        this.startingNumber = startingNumber;
        this.endingNumber = endingNumber;

        this.answerIsCorrect = false;
        this.usersAnswer = null;
    }

    /* Mutator */

    public void setUsersAnswer(String usersAnswer) {
        this.usersAnswer = usersAnswer;
    }

    public void setAnswerIsCorrect(boolean isCorrect){
        this.answerIsCorrect = isCorrect;
    }

    /* Accessors */

    //Math accessors

    public String getFirstNumber() {
        return firstNumber;
    }

    public String getSecondNumber() {
        return secondNumber;
    }

    public String getAnswerNumber() {
        return answerNumber;
    }

    //Convert accessors

    public String getStartingNumber() {
        return startingNumber;
    }

    public String getEndingNumber(){
        return endingNumber;
    }

    public String getUsersAnswer() {
        return usersAnswer;
    }

    public boolean isAnswerCorrect(){
        return answerIsCorrect;
    }
}
