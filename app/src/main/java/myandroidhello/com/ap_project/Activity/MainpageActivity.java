package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import myandroidhello.com.ap_project.Fragment.Home2Fragment;
import myandroidhello.com.ap_project.Fragment.HomeFragment;
import myandroidhello.com.ap_project.Adapter.SectionsPagerAdapter;
import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.UniversalImageLoader;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.Model.GlobalVariables;

public class MainpageActivity extends Navigation_BaseActivity {


    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;

    private Context mContext = MainpageActivity.this;

    //widgets
    private ViewPager mViewPager;
    private RelativeLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        getGlobalData();

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        toolBar_title=findViewById(R.id.toolbar_title);

        //toolbar
        toolbar.setTitle("");//設置ToolBar Title
        toolBar_title.setText(R.string.view_one);
        setUpToolBar();//使用父類別的setUpToolBar()，設置ToolBar
        CurrentMenuItem = 0;
        NV.getMenu().getItem(CurrentMenuItem).setChecked(true);//設置Navigation目前項目被選取狀態
        //bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.reserve:
                        Intent intent=new Intent(MainpageActivity.this, ReserveActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.group:
                        Intent intent1=new Intent(MainpageActivity.this, JFGroupActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.start:
                        Intent intent2=new Intent(MainpageActivity.this, MenuActivity.class);
                        startActivity(intent2);
                        break;
                }

                return true;
            }
        });

        //center content
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        mFrameLayout = findViewById(R.id.container);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relLayoutParent);

        setupViewPager();
        initImageLoader();
    }

    private void getGlobalData() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.READ_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("rrrrr",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray subArray = jsonObject.getJSONArray("response");
                            Log.d("t1",String.valueOf(jsonObject));
                            String id=subArray.getJSONObject(0).getString("fb_id");
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
                            GlobalVariables User = (GlobalVariables)getApplicationContext();
                            User.setId(id);
                            User.setUrl(picUrl);
                            User.setName(name);
                            User.setStuId(stuId);
                            User.setPhoneNum(phoneNum);
                            User.setEmail(email);
                            User.setHeight(height);
                            User.setWeight(weight);
                            User.setEzcard(ezcard);
                            User.setSex(sex);
                            User.setCollege(college);
                            User.setDepartment(department);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("error","do not save to mysql 2");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                Mysql mysql=new Mysql();
                GlobalVariables User = (GlobalVariables)getApplicationContext();
                String id=User.getId();
                String query=mysql.checkExistId(id);
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(MainpageActivity.this).addToRequestque(stringRequest);

    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /**
     * Responsible for adding two fragment: home fragment and 推播
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment()); //index 0
        adapter.addFragment(new Home2Fragment());//index 1
        mViewPager.setAdapter(adapter);

//        adapter.notifyDataSetChanged();
//        mViewPager.invalidateBullets(adapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("動 態");
        tabLayout.getTabAt(1).setText("推 播");

    }


}
