package myandroidhello.com.ap_project.Model;

/**
 * Created by Yehwenting on 2018/4/30.
 */

public class CompetitionGroup {
    private String cid,uName,uPic,cpName,time,place,num,remain,note;

    public CompetitionGroup(String uName, String uPic, String cpName, String time, String place, String num, String remain, String note,String cid) {
        this.uName = uName;
        this.uPic = uPic;
        this.cpName = cpName;
        this.time = time;
        this.place = place;
        this.num = num;
        this.remain = remain;
        this.note = note;
        this.cid=cid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPic() {
        return uPic;
    }

    public void setuPic(String uPic) {
        this.uPic = uPic;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
