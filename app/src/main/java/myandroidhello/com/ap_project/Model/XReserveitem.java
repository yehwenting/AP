package myandroidhello.com.ap_project.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jenny on 2018/4/27.
 */

public class XReserveitem implements Parcelable {

    private String date;
    private String ename;
    private long start_time;
    private long end_time;
    private String pic_path;

    public XReserveitem(String date, String ename, long start_time, long end_time, String pic_path) {
        this.date = date;
        this.ename = ename;
        this.start_time = start_time;
        this.end_time = end_time;
        this.pic_path = pic_path;
    }

    public XReserveitem() {

    }


    protected XReserveitem(Parcel in) {
        date = in.readString();
        ename= in.readString();
        start_time = in.readLong();
        end_time = in.readLong();
        pic_path = in.readString();
    }

    public static final Creator<XReserveitem> CREATOR = new Creator<XReserveitem>() {
        @Override
        public XReserveitem createFromParcel(Parcel in) {
            return new XReserveitem(in);
        }

        @Override
        public XReserveitem[] newArray(int size) {
            return new XReserveitem[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }


    @Override
    public String toString() {
        return "XReserveitem{" +
                "date='" + date + '\'' +
                "ename='" + ename + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", pic_path='" + pic_path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(ename);
        dest.writeLong(start_time);
        dest.writeLong(end_time);
        dest.writeString(pic_path);
    }
}
