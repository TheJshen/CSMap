package com.example.kcco.csmap.ClassroomSchedule;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David on 5/22/2015.
 */
public class ScheduleInfo implements Parcelable {

    private String dayOfWeek;
    private String timePeriod;
    private String className;

    public ScheduleInfo(String dayOfWeek, String timePeriod, String className) {
        this.dayOfWeek = dayOfWeek;
        this.timePeriod = timePeriod;
        this.className = className;
    }

    private ScheduleInfo(Parcel in) {
        this.dayOfWeek = in.readString();
        this.timePeriod = in.readString();
        this.className = in.readString();
    }

    public String getPrintable() {
        String strOut = dayOfWeek + " " + timePeriod + " " + className;
        return strOut;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dayOfWeek);
        dest.writeString(timePeriod);
        dest.writeString(className);
    }

    public static final Parcelable.Creator<ScheduleInfo> CREATOR
            = new Parcelable.Creator<ScheduleInfo>() {
        @Override
        public ScheduleInfo createFromParcel(Parcel in) {
            return new ScheduleInfo(in);
        }

        @Override
        public ScheduleInfo[] newArray(int size) {
            return new ScheduleInfo[size];
        }
    };
}
