package com.anull.dev.leo.battleships.ship;


import android.os.Parcel;
import android.os.Parcelable;

class ShipCellInfo implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.i);
        dest.writeInt(this.j);
        dest.writeByte(this.isDamaged ? (byte) 1 : (byte) 0);
    }

    protected ShipCellInfo(Parcel in) {
        this.i = in.readInt();
        this.j = in.readInt();
        this.isDamaged = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ShipCellInfo> CREATOR = new Parcelable.Creator<ShipCellInfo>() {
        @Override
        public ShipCellInfo createFromParcel(Parcel source) {
            return new ShipCellInfo(source);
        }

        @Override
        public ShipCellInfo[] newArray(int size) {
            return new ShipCellInfo[size];
        }
    };
}
