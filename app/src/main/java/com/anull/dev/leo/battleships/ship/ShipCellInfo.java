package com.anull.dev.leo.battleships.ship;


class ShipCellInfo {
    private int i;
    private int j;
    private boolean isDamaged;

    ShipCellInfo(int i, int j) {
        this.i = i;
        this.j = j;
    }

    int getI() {
        return i;
    }

    int getJ() {
        return j;
    }

    boolean isDamaged() {
        return isDamaged;
    }

    void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }
}
