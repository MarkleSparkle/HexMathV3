package com.marklesparkle.hexmathv3;

public class GameSettings {

    /* Local Variables */

    static GameType gameType;

    static Difficulty difficulty;

    //for math type games only
    static Base base;

    //for convert type games only
    static Base startingBase;
    static Base endingBase;

    static InputType convertInputType;
    static InputType mathInputType;

    static RepeatingTimes repeatingTimes;


    /* Accessors */

    public static GameType getGameType() { return gameType; }

    public static Difficulty getDifficulty() { return difficulty; }

    public static Base getBase() { return base; }

    public static Base getStartingBase() { return startingBase;}

    public static Base getEndingBase() { return endingBase; }

    public static InputType getConvertInputType() { return convertInputType; }

    public static InputType getMathInputType() { return  mathInputType; }

    public static RepeatingTimes getRepeatingTimes() { return repeatingTimes; }

    /* Mutators */

    public static void setGameType(GameType gameType) { GameSettings.gameType = gameType; }

    public static void setDifficulty(Difficulty difficulty) { GameSettings.difficulty = difficulty; }

    public static void setStartingBase(Base startingBase) { GameSettings.startingBase = startingBase; }

    public static void setEndingBase(Base endingBase) { GameSettings.endingBase = endingBase; }

    public static void setBase(Base base) { GameSettings.base = base; }

    public static void setConvertInputType(InputType convertInputType) { GameSettings.convertInputType = convertInputType; }

    public static void setMathInputType(InputType mathInputType) { GameSettings.mathInputType = mathInputType; }

    public static void setRepeatingTimes(RepeatingTimes repeatingTimes) { GameSettings.repeatingTimes = repeatingTimes; }
}
