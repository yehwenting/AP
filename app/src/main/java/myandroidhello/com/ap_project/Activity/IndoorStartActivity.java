package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.barcode.BarcodeCaptureActivity;

public class IndoorStartActivity extends Navigation_BaseActivity {
    private static final String LOG_TAG = IndoorStartActivity.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    String showUri = "http://140.119.19.36:80/start.php";
    com.android.volley.RequestQueue requestQueue;

    private TextView mResultTextView;
    private String uid ;
    private String uname;
    private String pname="健身房";
    private String ename=null;

    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_start);

        mResultTextView = (TextView) findViewById(R.id.result_textview);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        toolBar_title=findViewById(R.id.toolbar_title);

        //toolbar
        toolbar.setTitle(" ");//設置ToolBar Title
        toolBar_title.setText("開 始 運 動");
        setUpToolBar();//使用父類別的setUpToolBar()，設置ToolBar
//        CurrentMenuItem = 0;
//        NV.getMenu().getItem(CurrentMenuItem).setChecked(true);//設置Navigation目前項目被選取狀態
        //bottomNavigation
        bottomNavigationView.setSelectedItemId(R.id.start);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.reserve:
                                Intent intent=new Intent(IndoorStartActivity.this, ReserveActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.group:
                                Intent intent1=new Intent(IndoorStartActivity.this, JFGroupActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.start:
                                Intent intent2=new Intent(IndoorStartActivity.this, MenuActivity.class);
                                startActivity(intent2);
                                break;
                        }

                        return true;
                    }
                });

        Button scanBarcodeButton = (Button) findViewById(R.id.scan_barcode_button);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });


//        setting Pinfo
        GlobalVariables user=(GlobalVariables)getApplicationContext();
        uid=user.getId();
        uname=user.getName();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    String getData = barcode.displayValue;
                    ename = getData;
//                    mResultTextView.setText(barcode.displayValue);
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    Log.d("123223", "onActivityResult: ename=" + ename);
                    Log.d("22222","hello");
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, showUri+"?getData="+getData, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("1111111","onResponse");
                                    try {
                                        //這邊要和上面json的名稱一樣
                                        JSONArray data = response.getJSONArray("data");
                                        Boolean canExercise = false;
                                        Boolean isReserved = false;
                                        if (data.length()!=0){
                                            //get the smallest start_time
                                            Long reservedStartTime = Long.parseLong(data.getJSONObject(0).getString("start_time"))*1000;
                                            String reservedUid = null;
                                            Long reservedEndTime = Long.parseLong(data.getJSONObject(0).getString("end_time"))*1000;
                                            for (int i = 0; i < data.length(); i++) {
                                                JSONObject jasonData = data.getJSONObject(i);
                                                Long start_time = Long.parseLong(jasonData.getString("start_time"))*1000;
                                                Long end_time = Long.parseLong(jasonData.getString("end_time"))*1000;
                                                Log.d("IndoorStartActivity", "onResponse: start_time: " + start_time);
                                                Log.d("IndoorStartActivity", "onResponse: reservedStartTime: " + reservedStartTime);
                                                Log.d("IndoorStartActivity", "onResponse: currentTime: " + System.currentTimeMillis());
                                                Log.d("IndoorStartActivity", "onResponse: reservedEndTime: " + reservedEndTime);
                                                if (start_time<System.currentTimeMillis() && end_time > System.currentTimeMillis()){
                                                    isReserved = true;

                                                    reservedStartTime = start_time;
                                                    reservedUid = jasonData.getString("uid");
//                                                    reservedEndTime = jasonData.getLong("end_time")*1000;
//                                                    Log.d("IndoorStartActivity", "onResponse: reservedEndTime: " + reservedEndTime);
                                                }
                                            }
                                            if (isReserved){
                                                if (uid.equals(reservedUid) ){
                                                    Log.d("IndoorStartActivity", "onResponse: reservedStartTime: " + reservedStartTime);
                                                    Log.d("IndoorStartActivity", "onResponse: reservedEndTime: " + reservedEndTime);
                                                    canExercise = true;
                                                }else {
                                                    reservedStartTime = reservedStartTime + 300000;
                                                    SimpleDateFormat simpleDateformat = new SimpleDateFormat("hh:mm");
                                                    Date date = new Date();
                                                    date.setTime(reservedStartTime);
                                                    String availableTime = simpleDateformat.format(date);
                                                    mResultTextView.setText("現在有人預約\n若到" + availableTime + "預約者還沒來即可使用");
                                                    mResultTextView.setTextColor(Color.RED);
                                                }
                                            }else {
                                                canExercise = true;
                                            }
//                                            if (uid.equals(reservedUid) ){
//                                                Log.d("IndoorStartActivity", "onResponse: reservedStartTime: " + reservedStartTime);
//                                                Log.d("IndoorStartActivity", "onResponse: reservedEndTime: " + reservedEndTime);
//                                                canExercise = true;
//                                            }else if(reservedStartTime-System.currentTimeMillis()<1800000 && reservedStartTime > System.currentTimeMillis()){
//                                                SimpleDateFormat simpleDateformat = new SimpleDateFormat("hh:mm");
//                                                Date date = new Date();
//                                                date.setTime(reservedStartTime);
//                                                String nextReservation = simpleDateformat.format(date);
//                                                Toast.makeText(IndoorStartActivity.this,"溫馨提醒：目前機台可以使用\n但下一位使用者的預約時間為" + nextReservation + "，請注意時間！",Toast.LENGTH_LONG).show();
//                                                Log.d("IndoorStartActivity", "onResponse: reservedStartTime: " + reservedStartTime);
//                                                Log.d("IndoorStartActivity", "onResponse: reservedEndTime: " + reservedEndTime);
//                                                canExercise = true;
//                                            }else if (reservedStartTime < System.currentTimeMillis() && reservedEndTime > System.currentTimeMillis()){
//                                                Log.d("IndoorStartActivity", "onResponse:reservedStartTime: " + reservedStartTime);
//                                                Log.d("IndoorStartActivity", "onResponse: reservedEndTime: " + reservedEndTime);
//                                                reservedStartTime = reservedStartTime + 300000;
//                                                SimpleDateFormat simpleDateformat = new SimpleDateFormat("hh:mm");
//                                                Date date = new Date();
//                                                date.setTime(reservedStartTime);
//                                                String availableTime = simpleDateformat.format(date);
//                                                mResultTextView.setText("現在有人預約\n若到" + availableTime + "預約者還沒來即可使用");
//                                                mResultTextView.setTextColor(Color.RED);
//                                            }else{
//                                                canExercise = true;
//                                            }

                                        }else {
                                            canExercise = true;
                                        }
                                        if(canExercise){
                                            Intent intent = new Intent(IndoorStartActivity.this,StartExercise1Activity.class);
                                            intent.putExtra("uid",uid );
                                            intent.putExtra("uname", uname);
                                            intent.putExtra("pname", pname);
                                            intent.putExtra("ename", ename);
                                            startActivity(intent);
                                            finish();

                                        }


                                    } catch (JSONException e) {


                                        e.printStackTrace();


                                    }


                                }


                            }, new Response.ErrorListener() {


                                @Override


                                public void onErrorResponse(VolleyError error) {
//                                if (error.getMessage() != null)
//                                    Log.d("11111111",error.getMessage());
                                    VolleyLog.e("error",error.getMessage());
//                                    try {
//                                        byte[] htmlBodyBytes = error.networkResponse.data;
//                                        Log.e(LOG_TAG, new String(htmlBodyBytes), error);
//                                    } catch (NullPointerException e) {
//                                        e.printStackTrace();
//                                    }
//



                                }

                            }) ;

                    requestQueue.add(jsonObjectRequest);


                } else mResultTextView.setText(R.string.no_barcode_captured);
            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }
}
