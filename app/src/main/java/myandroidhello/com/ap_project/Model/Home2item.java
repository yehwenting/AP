package myandroidhello.com.ap_project.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jenny on 2018/3/25.
 */

public class Home2item implements Parcelable {

    private String content;
    private String date;
    private String icon_path;
    private String uid;
    private String sid;
    private Long longdate;

    public Home2item(String content, String date, String icon_path, String uid, String sid, Long longdate) {
        this.content = content;
        this.date = date;
        this.icon_path = icon_path;
        this.uid = uid;
        this.sid = sid;
        this.longdate = longdate;
    }

    public Home2item() {

    }


    protected Home2item(Parcel in) {
        content = in.readString();
        date = in.readString();
        icon_path = in.readString();
        uid = in.readString();
        sid = in.readString();
        longdate = in.readLong();
    }

    public static final Creator<Home2item> CREATOR = new Creator<Home2item>() {
        @Override
        public Home2item createFromParcel(Parcel in) {
            return new Home2item(in);
        }

        @Override
        public Home2item[] newArray(int size) {
            return new Home2item[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPic_path() {
        return icon_path;
    }

    public void setPic_path(String icon_path) {
        this.icon_path = icon_path;
    }

    public Long getLongdate() {
        return longdate;
    }

    public void setLongdate(Long longdate) {
        this.longdate = longdate;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "Home2item{" +
                "content='" + content + '\'' +
                "uid='" + uid + '\'' +
                "sid='" + sid + '\'' +
                ", start_time='" + date + '\'' +
                ", icon_path='" + icon_path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(icon_path);
        dest.writeString(uid);
        dest.writeString(sid);
    }
}