package com.example.lr2.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Train implements Parcelable
{
    private int id;
    private String title;
    private int color;
    private int prepTime;
    private int workTime;
    private int freeTime;
    private int cycleNum;
    private int setNum;
    private int freeOfSet;

    public Train(int id, String title, int color, int prepTime, int workTime, int freeTime, int cycleNum, int setNum, int freeOfSet)
    {
        this.id = id;
        this.title = title;
        this.color = color;
        this.prepTime = prepTime;
        this.workTime = workTime;
        this.freeTime = freeTime;
        this.cycleNum = cycleNum;
        this.setNum = setNum;
        this.freeOfSet = freeOfSet;
    }

    public Train(Parcel source)
    {
        id = source.readInt();
        title = source.readString();
        color = source.readInt();
        prepTime = source.readInt();
        workTime = source.readInt();
        freeTime = source.readInt();
        cycleNum = source.readInt();
        setNum = source.readInt();
        freeOfSet = source.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {return color;}

    public void setColor(int color) {this.color = color;}

    public int getPrepTime() { return prepTime; }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(int freeTime) {
        this.freeTime = freeTime;
    }

    public int getCycleNum() {
        return cycleNum;
    }

    public void setCycleNum(int cycleNum) {
        this.cycleNum = cycleNum;
    }

    public int getSetNum() {
        return setNum;
    }

    public void setSetNum(int setNum) {
        this.setNum = setNum;
    }

    public int getFreeOfSet() {
        return freeOfSet;
    }

    public void setFreeOfSet(int freeOfSet) {
        this.freeOfSet = freeOfSet;
    }

    @Override
    public String toString()
    {
        return Integer.toString(this.id);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(color);
        dest.writeInt(prepTime);
        dest.writeInt(workTime);
        dest.writeInt(freeTime);
        dest.writeInt(cycleNum);
        dest.writeInt(setNum);
        dest.writeInt(freeOfSet);
    }


    public static final Creator<Train> CREATOR = new Creator<Train>()
    {
        @Override
        public Train createFromParcel(Parcel source)
        {
            return new Train(source);
        }

        @Override
        public Train[] newArray(int size)
        {
            return new Train[size];
        }
    };

}
