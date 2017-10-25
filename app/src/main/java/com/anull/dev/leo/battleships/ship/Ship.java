package com.anull.dev.leo.battleships.ship;


import android.support.annotation.Nullable;

import com.anull.dev.leo.battleships.field.CellTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ship {
    private boolean isShipAlive = true;
    private List<ShipCellInfo> mShipCells;
    private int mShipSize;
    private int mDamagedCellsCount;

    public Ship(int shipSize){
        mShipSize = shipSize;
        mShipCells = new ArrayList<>();
    }

    public void addCoordinate(int i, int j){
        mShipCells.add(new ShipCellInfo(i, j));
        if (mShipCells.size() == mShipSize)
            generateRandomShipDamage();
    }

    private void generateRandomShipDamage(){
        mDamagedCellsCount = new Random().nextInt(mShipSize + 1);
        if (mDamagedCellsCount == mShipSize) {
            isShipAlive = false;
            for (ShipCellInfo x : mShipCells){
                x.setDamaged(true);
            }
        }else {
            for (int i = 0; i < mDamagedCellsCount; i++) {
                mShipCells.get(i).setDamaged(true);
            }
        }
    }

    public int getCellStatus(int i, int j){
        ShipCellInfo info = getShipCellInfo(i,j);
        if (info != null){
            return info.isDamaged() ? CellTypes.DEAD_SHIP_CELL : CellTypes.SHIP;
        }
        return 0;
    }

    public boolean isCurrentShipInCell(int i, int j){
        return getShipCellInfo(i,j) != null;
    }

    public boolean tryAttackShip(int i, int j){
        ShipCellInfo shipCellInfo = getShipCellInfo(i,j);
        if (shipCellInfo != null){
            shipCellInfo.setDamaged(!shipCellInfo.isDamaged());
            if (shipCellInfo.isDamaged())
                mDamagedCellsCount+=1;
            else mDamagedCellsCount-=1;
            return true;
        }else return false;
    }

    @Nullable
    private ShipCellInfo getShipCellInfo(int i, int j){
        for (ShipCellInfo x : mShipCells){
            if (x.getI() == i && x.getJ() == j){
                return x;
            }
        }
        return null;
    }

    public String getShipName(){
        return mShipSize + " decker ship";
    }

    public boolean isShipAlive(){
        return isShipAlive;
    }

    public int getShipSize(){
        return mShipSize;
    }

    public int getDamagedCellsCount(){
        return mDamagedCellsCount;
    }

}
