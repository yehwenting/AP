package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import myandroidhello.com.ap_project.Adapter.ReserveCheckAdapter;
import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;

public class ReserveCheckActivity extends Navigation_BaseActivity {

    public static TextView reserve_data;
    public Button confirm;
    private TextView toolBar_title;
    public static RecyclerView recyclerView;
    public static List<ReserveCheckAdapter.Data> dataList1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_check);

        reserve_data=findViewById(R.id.reserve_check_data);
        confirm=findViewById(R.id.confirm);
        toolBar_title=findViewById(R.id.toolbar_title);
        recyclerView =findViewById(R.id.reserve_check_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //toolbar
        toolbar.setTitle("");//設置ToolBar Title
        toolBar_title.setText(R.string.reserve);
        setUpToolBar();//使用父類別的setUpToolBar()，設置ToolBar
//        CurrentMenuItem = 0;
//        NV.getMenu().getItem(CurrentMenuItem).setChecked(true);//設置Navigation目前項目被選取狀態


        showReserveData();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(ReserveCheckActivity.this, MainpageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void showReserveData() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.READ_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<ReserveCheckAdapter.Data> dataList = new ArrayList<>();
                        dataList1=dataList;
                        try {
                            Log.d("success",response);
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray subArray = jsonObject.getJSONArray("response");
                            Log.d("response111",String.valueOf(subArray.length()));
                            if(subArray.length()==0){
                                reserve_data.setText("無預約資料");
                            }else{
                                for(int i=0;i<subArray.length();i++){
                                    int start_time=Integer.parseInt(subArray.getJSONObject(i).getString("start_time"));
                                    Date time=new java.util.Date((long)start_time*1000);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    // give a timezone reference for formatting (see comment at the bottom)
                                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                                    String formattedDate = sdf.format(time);
                                    String eName=subArray.getJSONObject(i).getString("eName");
                                    String img=subArray.getJSONObject(i).getString("url");
                                    String id=subArray.getJSONObject(i).getString("id");

                                    // insert reserve data into data
                                    ReserveCheckAdapter.Data data = new ReserveCheckAdapter.Data(id,formattedDate, eName,img);
                                    dataList.add(data);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // initialize the list view adapter
                        Log.d("fff","success");
                        ReserveCheckAdapter reserveCheckAdapter = new ReserveCheckAdapter(dataList);
                        recyclerView.setAdapter(reserveCheckAdapter);
                        reserveCheckAdapter.notifyDataSetChanged();


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
                Bundle bundle = getIntent().getExtras();
                String id = bundle.getString("number");
                String query=mysql.getReservationData(id);
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(ReserveCheckActivity.this).addToRequestque(stringRequest);
    }


}
