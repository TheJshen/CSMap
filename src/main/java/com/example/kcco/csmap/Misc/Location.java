package com.example.kcco.csmap.Misc;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by KaChan on 5/26/2015.
 */
public class Location implements Parcelable{
    LatLng thisLocation;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(thisLocation.latitude);
        dest.writeDouble(thisLocation.longitude);
    }

    private Location(Parcel in) { readFromParcel(in); }

    public void readFromParcel(Parcel in) {

        double lat = in.readDouble();
        double lon = in.readDouble();
        thisLocation = new LatLng(lat, lon);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
