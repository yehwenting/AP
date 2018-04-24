package myandroidhello.com.ap_project.Data;

/**
 * Created by Yehwenting on 2017/11/21.
 */

public class Mysql {


    public String addUserToMysql(String id,String name,String stu_id,String phoneNum,String email,String sex,String pic){
        String addsql="INSERT into user(fb_id,name,stu_id,phoneNum,email,height,weight,ezcard,ex_status,pic_url,sex) VALUES('"
                +id+"','"+name+"','"+stu_id+"','"+phoneNum+"','"+email+"','0','0','0','false','"+pic+"','"+sex+"');";
        return addsql;
    }

    public String updateUserToMysql(String id,String name,String stu_id,String phoneNum,String email,String sex){
        String updatesql="UPDATE user SET name='" +name+ "',stu_id='"+stu_id+
                "',phoneNum='"+phoneNum+"',email='"+email+"',sex='"+sex+"'\n" +
                "where fb_id='"+id+"';";
        return updatesql;
    }

    public String updateUserDetailToMysql(String id,String height,String weight,String ezcard,String college,String department){
        String updatesql="UPDATE user SET height='" +height+ "',weight='"+weight+
                "',ezcard='"+ezcard+"',college='"+college+"',department='"+department+"'\n" +
                "where fb_id='"+id+"';";
        return updatesql;
    }

    public String checkExistId(String id){
        return "select * from user where fb_id='"+id+"'";
    }

    public String getEquipmentName(){
        String getData="SELECT * FROM `equipment`";
        return getData;
    }

    public String saveReservation(String id,int start_time,int end_time,String eName,String uid,String date ){
        String data="INSERT into reserve(number,start_time,end_time,eName,uid,date) VALUES('"
                +id+"','"+start_time+"','"+end_time+"','"+eName+"','"+uid+"','"+date+"');";
        return data;
    }

    public String getReservationData(String number){
        String data="SELECT reserve.* , equipment.url FROM equipment, reserve " +
                "WHERE reserve.eName = equipment.eName AND reserve.number='"+number+"' ORDER BY start_time";
//        String data="SELECT * FROM `reserve` WHERE number='"+number+"' ORDER BY start_time";
        return data;
    }

    public String checkReserveTimeAvailable(String equipment){
        String data="SELECT start_time,end_time FROM `reserve` WHERE `eName`='"+equipment+"'";
        return data;
    }

    public String deleteReserveData(String id){
        String data="DELETE FROM `reserve` WHERE id="+id;
        return data;
    }

    public String addFriend(String user1,String user2){
        String data="INSERT INTO friends(user1,user2) VALUES('"+user1+"','"+user2+"');";
        return data;
    }

    public String checkIsFriend(String user1,String user2){
        String data="SELECT * FROM friends WHERE user1='"+user1+"' AND user2='"+user2+"'";
        return data;
    }

    public String deleteFriend(String user1,String user2){
        String data="DELETE FROM friends WHERE user1='"+user1+"' AND user2='"+user2+"';";
        return data;
    }

    public String addUserToGroupSql(String uid, int gid){
        String joingroup ="INSERT INTO joingroup (uid,gid) VALUES('"+uid+"','"+gid+"');" ;
        return joingroup;
    }

    public String createNewGroup(String name, String Type, String place, String uid,
                                 String date, int number, int remain){
        String createNewGroup ="INSERT into findgroup(gname,type, place, uid, date, number,remain)" +
                "VALUES "+ "('"+name+"','"+Type+"','"+place+"','"+uid+"','"+date+"','"+number+"','"+remain+"');";
        return createNewGroup;
    }

    public String getGroup(){
        String selectGroup = "SELECT findgroup.*,user.name,user.pic_url FROM findgroup,user " +
                "WHERE findgroup.uid=user.fb_id" +
                " ORDER BY findgroup.date DESC;";
        return  selectGroup;
    }
    public String checkIfJoined(String uid,String gid){
        String data="SELECT * FROM joingroup WHERE uid='"+uid+"' AND gid='"+gid+"'";
        return data;
    }


}
