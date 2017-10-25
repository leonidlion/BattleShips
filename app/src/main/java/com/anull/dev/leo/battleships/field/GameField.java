package com.anull.dev.leo.battleships.field;


import android.os.Parcel;
import android.os.Parcelable;

import com.anull.dev.leo.battleships.Constants;
import com.anull.dev.leo.battleships.Utils;
import com.anull.dev.leo.battleships.ship.Ship;
import com.anull.dev.leo.battleships.ship.ShipSizes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.anull.dev.leo.battleships.field.CellTypes.*;
import static com.anull.dev.leo.battleships.ship.ShipSizes.*;

public class GameField implements Parcelable {
    private static final int UP = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int DOWN = 3;

    private static final int MAX_INDEX = 9;
    private static final int MIN_INDEX = 0;

    private int[][] mCells;
    private int startI, startJ;
    private Random mRandom;
    private List<Ship> mShips;

    public GameField(){
        mCells = new int[10][10];
        mRandom = new Random();
        mShips = new ArrayList<>();
        initCells();
        generateShips();
    }

    private GameField(Parcel in) {
        this.mCells = Utils.read2DimArray(in);
        this.startI = in.readInt();
        this.startJ = in.readInt();
        this.mShips = new ArrayList<>();
        in.readList(this.mShips, Ship.class.getClassLoader());
    }

    private void initCells(){
        for (int i = 0; i < mCells.length; i++){
            for (int j = 0; j < mCells.length; j++){
                mCells[i][j] = CellTypes.EMPTY;
            }
        }
    }

    private void generateShips(){
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

    private void buildShip(int shipSize){
        generateStartPosition();

        if (shipSize != ShipSizes.ONE_DECKER) {
            List<Integer> possibleDirections = generatePossibleDirection(shipSize);
            if (possibleDirections.isEmpty()) {
                buildShip(shipSize);
            } else {
                buildShipInDirection(shipSize, possibleDirections);
            }
        }else {
            checkNearCells(true, UP, shipSize);
            Ship ship = new Ship(shipSize);
            ship.addCoordinate(startI, startJ);
            mShips.add(ship);
            mCells[startI][startJ] = CellTypes.SHIP;
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

        if ((startI - shipDecker) >= MIN_INDEX){
            if (checkNearCells(false, UP, shipDecker))
                possibleDirections.add(UP);
        }
        if ((startI + shipDecker) <= MAX_INDEX){
            if (checkNearCells(false, DOWN, shipDecker))
                possibleDirections.add(DOWN);
        }
        if ((startJ - shipDecker) >= MIN_INDEX){
            if (checkNearCells(false, LEFT, shipDecker))
                possibleDirections.add(LEFT);
        }
        if ((startJ + shipDecker) <= MAX_INDEX){
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

    private void buildShipInDirection(int shipDecker, List<Integer> possibleDirections) {
        int randomDirection = possibleDirections.get(mRandom.nextInt(possibleDirections.size()));
        checkNearCells(true, randomDirection, shipDecker);
        Ship currentShip = new Ship(shipDecker);

        switch (randomDirection){
            case DOWN:
                for (int i = startI; i < (startI + shipDecker); i++){
                    mCells[i][startJ] = SHIP;
                    currentShip.addCoordinate(i, startJ);
                }
                break;
            case UP:
                for (int i = startI; i > (startI - shipDecker); i--){
                    mCells[i][startJ] = SHIP;
                    currentShip.addCoordinate(i, startJ);
                }
                break;
            case LEFT:
                for (int j = startJ; j > (startJ - shipDecker); j--){
                    mCells[startI][j] = SHIP;
                    currentShip.addCoordinate(startI, j);
                }
                break;
            case RIGHT:
                for (int j = startJ; j < (startJ + shipDecker); j++){
                    mCells[startI][j] = SHIP;
                    currentShip.addCoordinate(startI, j);
                }
                break;
        }
        mShips.add(currentShip);
    }

    public int getCellType(int i, int j){
        if (mCells[i][j] == SHIP){
            for (Ship x : mShips){
                if(x.getCellStatus(i,j) != 0) {
                    return x.getCellStatus(i, j);
                }
            }
        } else {
            return mCells[i][j];
        }
        return -1;
    }

    public void attack(int i, int j){
        if (mCells[i][j] == SHIP){
            for (Ship x : mShips){
                if (x.tryAttackShip(i,j))
                    return;
            }
        }
    }

    public GameCellInfo getCellInfo(int i, int j){
        GameCellInfo info = new GameCellInfo(i,j);
        switch (mCells[i][j]){
            case SHIP:
                info.setCellType(Constants.SHIP_CELL_CLICK);
                for (Ship x : mShips) {
                    if (x.isCurrentShipInCell(i, j)) {
                        info.setI(i);
                        info.setJ(j);
                        info.setShip(x);
                        return info;
                    }
                }
            case EMPTY:
                info.setCellType(Constants.EMPTY_CELL_CLICK);
                return info;
            case NEAR_SHIP_CELL:
                info.setCellType(Constants.NEAR_SHIP_CELL_CLICK);
                return info;
            default: return info;
        }
    }

    public static final Parcelable.Creator<GameField> CREATOR = new Parcelable.Creator<GameField>() {
        @Override
        public GameField createFromParcel(Parcel source) {
            return new GameField(source);
        }

        @Override
        public GameField[] newArray(int size) {
            return new GameField[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Utils.write2DimArray(mCells, dest);
        dest.writeInt(this.startI);
        dest.writeInt(this.startJ);
        dest.writeList(this.mShips);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
