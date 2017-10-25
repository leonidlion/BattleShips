package com.anull.dev.leo.battleships;


import android.os.Parcel;

public class Utils {
    public static void write2DimArray(int[][] array, Parcel parcel) {
        parcel.writeInt(array.length);
        for(int[] arrayDim1 : array) {
            parcel.writeInt(arrayDim1.length);
            parcel.writeIntArray(arrayDim1);
        }
    }

    public static int[][] read2DimArray(Parcel parcel) {
        int[][] array = new int[parcel.readInt()][];
        for(int i = 0; i < array.length; i++) {
            int arraySize = parcel.readInt();
            array[i] = new int[arraySize];
            parcel.readIntArray(array[i]);
        }
        return array;
    }
}
