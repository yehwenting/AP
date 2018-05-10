package myandroidhello.com.ap_project.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jenny on 2018/4/27.
 */

public class XGroupitem implements Parcelable {

    private String date;
    private String gname;
    private String place;
    private String type;


    public XGroupitem(String date, String gname, String place, String type) {
        this.date = date;
        this.gname = gname;
        this.place = place;
        this.type = type;

    }

    public XGroupitem() {

    }


    protected XGroupitem(Parcel in) {
        date = in.readString();
        gname= in.readString();
        place = in.readString();
        type = in.readString();
    }

    public static final Creator<XGroupitem> CREATOR = new Creator<XGroupitem>() {
        @Override
        public XGroupitem createFromParcel(Parcel in) {
            return new XGroupitem(in);
        }

        @Override
        public XGroupitem[] newArray(int size) {
            return new XGroupitem[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "XGroupitem{" +
                "date='" + date + '\'' +
                "gname='" + gname + '\'' +
                ", place='" + place + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(gname);
        dest.writeString(place);
        dest.writeString(type);
    }
}
