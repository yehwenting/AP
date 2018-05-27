package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.Model.User;

public class AccountDetailActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO back button,add to db(finish),try to make setOnItemSelectedListener efficiently

    public Spinner weight;
    public Spinner height;
    public Spinner college;
    public Spinner department;
    public EditText ezcard;
    public ArrayAdapter<Integer> adapter;
    public ArrayAdapter<String> adapterString;
    public String selectWeight;
    public String selectHeight;
    public String selectCollege;
    public String selectDepartment;
    public Button finish;
    public Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);


        weight=findViewById(R.id.weightSpinner);
        height=findViewById(R.id.heightSpinner);
        college=findViewById(R.id.collegeSpinner);
        department=findViewById(R.id.departmentSpinner);
        ezcard=findViewById(R.id.ezcard);
        addItemsOnSpinner(weight,40,150);
        addItemsOnSpinner(height,145,210);
        addCollegesOnSpinner(college);
        addDepartmentsOnSpinner(department);
        ezcard=findViewById(R.id.ezcard);
        finish=findViewById(R.id.finishButton);
        back=findViewById(R.id.backButton);



        //select
        weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                    selectWeight=adapterView.getSelectedItem().toString();
                Log.d("test",selectWeight);
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(AccountDetailActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });
        height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                selectHeight=adapterView.getSelectedItem().toString();
                Log.d("test",selectHeight);
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(AccountDetailActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });
        college.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                selectCollege=adapterView.getSelectedItem().toString();
                Log.d("test",selectCollege);
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(AccountDetailActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                selectDepartment=adapterView.getSelectedItem().toString();
                Log.d("test",selectDepartment);
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(AccountDetailActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });


        //button
        finish.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    private void saveInfoToMysql(){
        if(checkNetworkConnection()){
            StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("success",response);
                                JSONObject jsonObject=new JSONObject(response);
                                String Response=jsonObject.getString("response");
                                if(Response.equals("OK")){
                                    Log.d("success","ya");
                                            //start new activity
                                            Intent intent=new Intent(AccountDetailActivity.this,MainpageActivity.class);
//                                            intent.putExtra("user", (Bundle) getIntent().getParcelableExtra("user"));
                                            startActivity(intent);
                                            finish();
                                }else{
//                                    saveGroceryToServer(view);
                                    Log.d("error","do not save to mysql");
                                }
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
                    Bundle bundle = getIntent().getExtras();
                    User user=getIntent().getParcelableExtra("user");
                    String id = user.getUser_id();
                    Log.d("id",id);
                    Mysql mysql=new Mysql();
                    String query=mysql.updateUserDetailToMysql(id,selectHeight,selectWeight,ezcard.getText().toString(),selectCollege,selectDepartment);
                    params.put("query",query);
                    params.put("id",id);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleTon.getmInstance(AccountDetailActivity.this).addToRequestque(stringRequest);
        }else{
            Log.d("error","map error");
        }


    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }


    public void addItemsOnSpinner(Spinner spinner, int start, int end) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=start; i<=end; i++){
            list.add(i);
        }
        adapter=new ArrayAdapter<Integer>(this,R.layout.spinner_item,list);
//        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }
    public void addCollegesOnSpinner(Spinner spinner) {
        String[] college={"文學院","理學院","社會科學院","法學院","商學院","外國語文學院","傳播學院",
                "國際事務學院","教育學院"};
        List<String> newList = Arrays.asList(college);
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(newList);

        adapterString=new ArrayAdapter<>(this,R.layout.spinner_item,list);
        adapterString.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapterString);
    }
    public void addDepartmentsOnSpinner(Spinner spinner) {
        final String[] department={"中國文學系","歷史學系","哲學系",
                "應用數學系","心理學系","資訊科學系","政治學系","社會學系",
                "財政學系","公共行政學系","地政學系","經濟學系","民族學系",
                "法律學系","金融學系","國際經營與貿易學系","會計學系",
                "統計學系","企業管理學系","資訊管理學系","財務管理學系",
                "風險管理與保險學系","英國語文學系","阿拉伯語文學系","斯拉夫語文學系",
                "日本語文學系","韓國語文學系","土耳其語文學系","歐洲語文學系",
                "新聞學系","廣告學系","廣播電視學系","傳播學院大一大二不分系",
                "外交學系","教育學系"};
        List<String> newList = Arrays.asList(department);
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(newList);

        adapterString=new ArrayAdapter<String>(this,R.layout.spinner_item,list);
        adapterString.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapterString);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.finishButton:
                saveInfoToMysql();
                break;
            case R.id.backButton:
                startActivity(new Intent(AccountDetailActivity.this,AccountActivity.class));
                finish();
                break;
        }

    }
}
