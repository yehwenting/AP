package myandroidhello.com.ap_project.Data;

/**
 * Created by Yehwenting on 2017/11/21.
 */

public class Mysql {


    public String addUserToMysql(String id,String name,String stu_id,String phoneNum,String email,String sex){
        String addsql="INSERT into user(fb_id,name,stu_id,phoneNum,email,height,weight,ezcard,ex_status,pic_url,sex) VALUES('"
                +id+"','"+name+"','"+stu_id+"','"+phoneNum+"','"+email+"','0','0','0','false','0','"+sex+"');";
        return addsql;
    }

    public String updateUserDetailToMysql(String id,String height,String weight,String ezcard){
        String updatesql="UPDATE user SET height='" +height+ "',weight='"+weight+
                "',ezcard='"+ezcard+"'\n" +
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

    public String saveReservation(int id,int start_time,int end_time,String eName,int uid,String date ){
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



}
