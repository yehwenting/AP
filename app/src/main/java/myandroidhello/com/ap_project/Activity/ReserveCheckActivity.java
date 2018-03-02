package myandroidhello.com.ap_project.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;

public class ReserveCheckActivity extends AppCompatActivity {

    public TextView reserve_data;
    public Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_check);

        reserve_data=findViewById(R.id.reserve_check_data);
        confirm=findViewById(R.id.confirm);
        showReserveData();
    }

    private void showReserveData() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.READ_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                    String text=reserve_data.getText().toString()+"\n"+formattedDate+"\n"+eName;
//                                    Log.d("text",reserve_data.getText().toString());
                                    reserve_data.setText(text);

                                }
                            }
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
