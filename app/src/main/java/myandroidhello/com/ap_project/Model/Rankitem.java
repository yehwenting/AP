package myandroidhello.com.ap_project.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jenny on 2018/3/25.
 */

public class Rankitem implements Parcelable {

    private String user_name;
    private long start_time;
    private long end_time;
    private String pic_path;

    public Rankitem(String user_name, long start_time, long end_time, String pic_path) {
        this.user_name = user_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.pic_path = pic_path;
    }

    public Rankitem() {

    }


    protected Rankitem(Parcel in) {
        user_name = in.readString();
        start_time = in.readLong();
        end_time = in.readLong();
        pic_path = in.readString();
    }

    public static final Creator<Rankitem> CREATOR = new Creator<Rankitem>() {
        @Override
        public Rankitem createFromParcel(Parcel in) {
            return new Rankitem(in);
        }

        @Override
        public Rankitem[] newArray(int size) {
            return new Rankitem[size];
        }
    };

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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
        return "Rankitem{" +
                "user_name='" + user_name + '\'' +
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
        dest.writeString(user_name);
        dest.writeLong(start_time);
        dest.writeLong(end_time);
        dest.writeString(pic_path);
    }
}