package myandroidhello.com.ap_project.model;

/**
 * Created by Stanley on 2018/3/11.
 */

public class AddGroup {
    private int gid;
    private String gname;
    private String name;
    private String type;
    private String place;
    private int uid;
    private String date;
    private String url;
    private int number;
    private int remain;

    public AddGroup(int gid, String name, String type, String place, int uid, String date, int number, int remain,String gname,String url) {
        this.gid = gid;
        this.gname = gname;
        this.name = name;
        this.type = type;
        this.place = place;
        this.uid = uid;
        this.date = date;
        this.number = number;
        this.remain = remain;
        this.url=url;
    }

    public int getGid() {
        return gid;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPlace() {
        return place;
    }

    public int getUid() {
        return uid;
    }

    public String getDate() {
        return date;
    }

    public int getNumber() {
        return number;
    }

    public int getRemain() {
        return remain;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
