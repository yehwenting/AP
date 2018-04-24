package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.model.GlobalVariables;

public class PersonInfoActivity extends Navigation_BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;
    private ImageView pic;
    private TextView info;
    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        toolBar_title=findViewById(R.id.toolbar_title);
        edit=findViewById(R.id.edit);

        //toolbar
        toolbar.setTitle("");//設置ToolBar Title
        toolBar_title.setText(R.string.view_two);
        setUpToolBar();//使用父類別的setUpToolBar()，設置ToolBar
        CurrentMenuItem = 1;
        NV.getMenu().getItem(CurrentMenuItem).setChecked(true);//設置Navigation目前項目被選取狀態
        //bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.reserve:
                                Intent intent=new Intent(PersonInfoActivity.this, ReserveActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.group:
                                Intent intent1=new Intent(PersonInfoActivity.this, JFGroupActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.start:
                                Intent intent2=new Intent(PersonInfoActivity.this, MenuActivity.class);
                                startActivity(intent2);
                                break;
                        }

                        return true;
                    }
                });

        pic=findViewById(R.id.personalPic);
        info=findViewById(R.id.personalInfo);
        GlobalVariables User = (GlobalVariables)getApplicationContext();
        displayProfilePic(pic,User.getUrl());
        getPersonalData(User.getId());
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(PersonInfoActivity.this, EditPInfoActivity.class);
                startActivity(intent);
            }
        });
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(PersonInfoActivity.this,MedalActivity.class);
                startActivity(intent);
            }
        });

    }
    private void displayProfilePic(ImageView imageView, String url) {

        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.user)
                .transform(transformation)
                .into(imageView);
    }
    private void getPersonalData(final String id){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.READ_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("success",response);
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray subArray = jsonObject.getJSONArray("response");
                            String name=subArray.getJSONObject(0).getString("name");
                            String picUrl=subArray.getJSONObject(0).getString("pic_url");
                            String stuId=subArray.getJSONObject(0).getString("stu_id");
                            String phoneNum=subArray.getJSONObject(0).getString("phoneNum");
                            String email=subArray.getJSONObject(0).getString("email");
                            String height=subArray.getJSONObject(0).getString("height");
                            String weight=subArray.getJSONObject(0).getString("weight");
                            String ezcard=subArray.getJSONObject(0).getString("ezcard");
                            String sex=subArray.getJSONObject(0).getString("sex");
                            String college=subArray.getJSONObject(0).getString("college");
                            String department=subArray.getJSONObject(0).getString("department");
                            info.setText("姓名 : "+name+"\n電話 : "+phoneNum+
                                        "\n性別 : "+sex+"\n信箱 : "+email+
                                        "\n學號 : "+stuId+"\n身高 : "+height+
                                        "\n體重 : "+weight+"\n悠遊卡卡號 : "+ezcard+
                                        "\n學院 : "+college+"\n學系 : "+department);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("error","do not get equipment from mysql 2");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                Mysql mysql=new Mysql();
                String query=mysql.checkExistId(id);
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(PersonInfoActivity.this).addToRequestque(stringRequest);

    }
}
