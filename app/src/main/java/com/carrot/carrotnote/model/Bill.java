package com.carrot.carrotnote.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "bills")
public class Bill implements Parcelable, Cloneable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bill_id")
    private int id;

    @ColumnInfo(name = "bill_time")
    private long mTime;

    @ColumnInfo(name = "bill_number")
    private double mNumber;

    @ColumnInfo(name = "bill_reason")
    private String mReason;

    @ColumnInfo(name = "bill_note")
    private String mNote;


    @Ignore
    public Bill() {
        mTime = System.currentTimeMillis();
        mNumber = 0;
        mReason = "";
        mNote = "";
    }

    public Bill(long time, double number, String reason, String note) {
        mTime = time;
        mNumber = number;
        mReason = reason;
        mNote = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public double getNumber() {
        return mNumber;
    }

    public void setNumber(double number) {
        this.mNumber = number;
    }

    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        this.mReason = reason;
    }


    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }


    public Bill copy() {
        try {
            return (Bill) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("why it happened?");
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bill)) return false;
        Bill bill = (Bill) o;
        return id == bill.id &&
                mTime == bill.mTime &&
                Double.compare(bill.mNumber, mNumber) == 0 &&
                Objects.equals(mReason, bill.mReason) &&
                Objects.equals(mNote, bill.mNote);
    }


    private boolean stringEquals(String a,String b) {
        if (TextUtils.isEmpty(a) && TextUtils.isEmpty(b))
            return true;
        return Objects.equals(a,b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mTime, mNumber, mReason, mNote);
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", mTime=" + mTime +
                ", mNumber=" + mNumber +
                ", mReason='" + mReason + '\'' +
                ", mNote='" + mNote + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.mTime);
        dest.writeDouble(this.mNumber);
        dest.writeString(this.mReason);
        dest.writeString(this.mNote);
    }

    protected Bill(Parcel in) {
        this.id = in.readInt();
        this.mTime = in.readLong();
        this.mNumber = in.readDouble();
        this.mReason = in.readString();
        this.mNote = in.readString();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel source) {
            return new Bill(source);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };
}
