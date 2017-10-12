package com.anull.dev.leo.battleships;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameField {
    private static final int EMPTY = 0;
    private static final int SHIP = 1;
    private static final int NEAR_SHIP_CELL = 2;

    private static final int UP = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;

    private static final int MAX_INDEX = 9;
    private static final int MIN_INDEX = 0;

    private static final int FOUR_DECKER = 4;
    private static final int THREE_DECKER = 3;
    private static final int TWO_DECKER = 2;
    private static final int ONE_DECKER = 1;

    private int[][] mCells;
    private int startI, startJ;
    private Random mRandom;
    private int mCellColor;
    private int mShipColor;

    public GameField(){
        mCells = new int[10][10];
        mRandom = new Random();
    }

    public void setColors(int cellColor, int shipColor){
        mCellColor = cellColor;
        mShipColor = shipColor;
    }

    public void addCell(int i, int j){
        mCells[i][j] = EMPTY;
    }

    public void generateShips(){
        buildShip(FOUR_DECKER);
        buildShip(THREE_DECKER);
        buildShip(THREE_DECKER);
        buildShip(TWO_DECKER);
        buildShip(TWO_DECKER);
        buildShip(TWO_DECKER);
        buildShip(ONE_DECKER);
        buildShip(ONE_DECKER);
        buildShip(ONE_DECKER);
        buildShip(ONE_DECKER);
    }

    private void buildShip(int shipDecade){
        generateStartPosition();

        if (shipDecade != ONE_DECKER) {
            List<Integer> possibleDirections = generatePossibleDirection(shipDecade);
            if (possibleDirections.isEmpty()) {
                buildShip(shipDecade);
            } else {
                buildShipInDirection(shipDecade, possibleDirections);
            }
        }else {
            checkNearCells(true, UP, shipDecade);
            mCells[startI][startJ] = SHIP;
        }
    }

    private void generateStartPosition() {
        boolean isStartPositionCorrect = false;
        while (!isStartPositionCorrect) {
            startI = mRandom.nextInt(10);
            startJ = mRandom.nextInt(10);
            if (mCells[startI][startJ] != SHIP &&
                    mCells[startI][startJ] != NEAR_SHIP_CELL){
                isStartPositionCorrect = true;
            }
        }
    }

    private List<Integer> generatePossibleDirection(int shipDecker) {
        List<Integer> possibleDirections = new ArrayList<>();
        Log.d(TAG, "I: " + startI + " J: " + startJ + " DECK: " + shipDecker);

        if ((startI - shipDecker) >= MIN_INDEX){
            Log.d(TAG, "UP: " + (startI - shipDecker));
            if (checkNearCells(false, UP, shipDecker))
                possibleDirections.add(UP);
        }
        if ((startI + shipDecker) <= MAX_INDEX){
            Log.d(TAG, "DOWN: " + (startI + shipDecker));
            if (checkNearCells(false, DOWN, shipDecker))
                possibleDirections.add(DOWN);
        }
        if ((startJ - shipDecker) >= MIN_INDEX){
            Log.d(TAG, "LEFT: " + (startJ - shipDecker));
            if (checkNearCells(false, LEFT, shipDecker))
                possibleDirections.add(LEFT);
        }
        if ((startJ + shipDecker) <= MAX_INDEX){
            Log.d(TAG, "RIGHT: " + (startJ + shipDecker));
            if (checkNearCells(false, RIGHT, shipDecker))
                possibleDirections.add(RIGHT);
        }

        return possibleDirections;
    }

    private boolean checkNearCells(boolean fillNearCells, int directions, int shipDecker) {
        int up = startI - 1 >= MIN_INDEX ? startI -1 : startI;
        int down = startI + 1 <= MAX_INDEX ? startI + 1 : startI;
        int left = startJ - 1 >= MIN_INDEX ? startJ - 1 : startJ;
        int right = startJ + 1 <= MAX_INDEX ? startJ + 1 : startJ;

        switch (directions){
            case UP:
                if (startI - shipDecker == -1){
                    up = MIN_INDEX;
                }else {
                    up = startI - shipDecker >= MIN_INDEX ? startI - shipDecker : startI;
                }
                break;
            case DOWN:
                if (startI + shipDecker == 10)
                    down = MAX_INDEX;
                else
                    down = startI + shipDecker <= MAX_INDEX ? startI + shipDecker : startI;
                break;
            case LEFT:
                if (startJ - shipDecker == -1)
                    left = MIN_INDEX;
                else
                    left = startJ - shipDecker >= MIN_INDEX ? startJ - shipDecker : startJ;
                break;
            case RIGHT:
                if (startJ + shipDecker == MAX_INDEX)
                    right = MAX_INDEX;
                else
                    right = startJ + shipDecker <= MAX_INDEX ? startJ + shipDecker : startJ;
                break;
        }
        for (int x = left; x <= right; x++){
            for (int y = up; y <= down; y++){
                if (mCells[y][x] == SHIP){
                    return false;
                }
            }
        }
        if (fillNearCells){
            for (int x = left; x <= right; x++){
                for (int y = up; y <= down; y++) {
                    mCells[y][x] = NEAR_SHIP_CELL;
                }
            }
        }
        return true;
    }

    private static final String TAG = "GameField";
    private void buildShipInDirection(int shipDecker, List<Integer> possibleDirections) {
        int randomDirection = possibleDirections.get(mRandom.nextInt(possibleDirections.size()));
        checkNearCells(true, randomDirection, shipDecker);
        switch (randomDirection){
            case DOWN:
                for (int i = startI; i < (startI + shipDecker); i++){
                    Log.d(TAG, "buildShipInDirection: I " + i);
                    mCells[i][startJ] = SHIP;
                }
                break;
            case UP:
                for (int i = startI; i > (startI - shipDecker); i--){
                    Log.d(TAG, "buildShipInDirection: I " + i);
                    mCells[i][startJ] = SHIP;
                }
                break;
            case LEFT:
                for (int j = startJ; j > (startJ - shipDecker); j--){
                    Log.d(TAG, "buildShipInDirection: J " + j);
                    mCells[startI][j] = SHIP;
                }
                break;
            case RIGHT:
                for (int j = startJ; j < (startJ + shipDecker); j++){
                    Log.d(TAG, "buildShipInDirection: J " + j);
                    mCells[startI][j] = SHIP;
                }
                break;
        }
    }

    public int getCellColor(int i, int j){
       if (mCells[i][j] == SHIP)
           return mShipColor;
        else return mCellColor;
   }
}
