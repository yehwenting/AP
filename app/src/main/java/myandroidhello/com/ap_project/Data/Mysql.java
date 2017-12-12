package myandroidhello.com.ap_project.Data;

/**
 * Created by Yehwenting on 2017/11/21.
 */

public class Mysql {


    public String addUserToMysql(String id,String name,String stu_id,String phoneNum,String email,String sex){
        String addsql="INSERT into login(fb_id,name,stu_id,phoneNum,email,height,weight,ezcard,ex_status,pic_url,sex) VALUES('"
                +id+"','"+name+"','"+stu_id+"','"+phoneNum+"','"+email+"','0','0','0','false','0','"+sex+"');";
        return addsql;
    }

    public String updateUserDetailToMysql(String id,String height,String weight,String ezcard){
        String updatesql="UPDATE login SET height='" +height+ "',weight='"+weight+
                "',ezcard='"+ezcard+"'\n" +
                "where fb_id='"+id+"';";
        return updatesql;
    }

    public String checkExistId(String id){
        return "select * from login where fb_id='"+id+"'";
    }



}
