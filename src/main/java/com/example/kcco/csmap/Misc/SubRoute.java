package com.example.kcco.csmap.Misc;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by KaChan on 5/26/2015.
 */
public class SubRoute implements Parcelable {

    private ArrayList<Location> thisRoute;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(thisRoute);
    }

    public SubRoute(Parcel in){

        if (thisRoute == null) {
            thisRoute = new ArrayList();
        }
        in.readTypedList(thisRoute, Location.CREATOR);
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SubRoute createFromParcel(Parcel in) {
            return new SubRoute(in);
        }

        public SubRoute[] newArray(int size) {
            return new SubRoute[size];
        }
    };
}
