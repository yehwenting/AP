package myandroidhello.com.ap_project.model;

import android.app.Application;

/**
 * Created by Yehwenting on 2018/3/17.
 */

public class GlobalVariables extends Application {
    private String id;
    private String url="";
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
