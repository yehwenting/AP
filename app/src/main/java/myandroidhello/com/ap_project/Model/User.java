package myandroidhello.com.ap_project.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jenny on 2018/3/2.
 */

public class User implements Parcelable {

    private String user_id;
    private String pic_url;
    private String username;

    public User(String user_id, String pic_url, String username) {
        this.user_id = user_id;
        this.pic_url = pic_url;
        this.username = username;
    }

    public User() {

    }


    protected User(Parcel in) {
        user_id = in.readString();
        pic_url = in.readString();
        username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", pic_url='" + pic_url + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(pic_url);
        dest.writeString(username);
    }
}