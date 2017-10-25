package com.anull.dev.leo.battleships.field;


import com.anull.dev.leo.battleships.ship.Ship;

public class GameCellInfo {
    private int i;
    private int j;
    private Ship mShip;
    private String mCellType;

    public GameCellInfo(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public Ship getShip() {
        return mShip;
    }

    public void setShip(Ship ship) {
        mShip = ship;
    }

    public String getCellType() {
        return mCellType;
    }

    public void setCellType(String cellType) {
        mCellType = cellType;
    }
}
