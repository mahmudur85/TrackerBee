package com.trackertraced.trackerbee.locationmodule.model;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mahmudur on 17-Mar-15.
 */
public class LocationModel implements Parcelable {
    private static final String KEY_LOCATION = "LOCATION";
    private static final String KEY_TIMESTAMP = "timestamp";

    private Location location;
    private long timestamp;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public LocationModel(Location location, long timestamp) {
        this.location = location;
        this.timestamp = timestamp;
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param parcel in a parcel from which to read this object
     *               - See more at: http://shri.blog.kraya.co.uk/2010/04/26/android-parcel-data-to-pass-between-activities-using-parcelable-classes/#sthash.X5wSSbiD.dpuf
     */

    private LocationModel(Parcel parcel) {
        this.location = parcel.readParcelable(Location.class.getClassLoader());
        this.timestamp = parcel.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.location, flags);
        dest.writeLong(this.timestamp);
    }

    /**
     * Creator required for class implementing the parcelable interface.
     */
    public static final Parcelable.Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel source) {
            return new LocationModel(source);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationModel: ");
        sb.append("{ Location = ").append(this.location);
        sb.append(", Timestamp = ").append(this.timestamp);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationModel)) return false;

        LocationModel that = (LocationModel) o;

        if (timestamp != that.timestamp) return false;
        if (location != null ? !location.equals(that.location) : that.location != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
}
