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
import java.util.HashMap;
import java.util.Map;

import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;

public class AccountDetailActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO back button,add to db(finish),try to make setOnItemSelectedListener efficiently

    public Spinner weight;
    public Spinner height;
    public EditText ezcard;
    public ArrayAdapter<Integer> adapter;
    public String selectWeight;
    public String selectHeight;
    public Button finish;
    public Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);


        weight=(Spinner)findViewById(R.id.weightSpinner);
        height=(Spinner)findViewById(R.id.heightSpinner);
        ezcard=(EditText)findViewById(R.id.ezcard);
        addItemsOnSpinner(weight,40,150);
        addItemsOnSpinner(height,145,210);
        ezcard=(EditText)findViewById(R.id.ezcard);
        finish=(Button)findViewById(R.id.finishButton);
        back=(Button)findViewById(R.id.backButton);



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

        //button
        finish.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    private void saveInfoToMysql(){
        if(checkNetworkConnection()){
            StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("success",response);
                                JSONObject jsonObject=new JSONObject(response);
                                String Response=jsonObject.getString("response");
                                if(Response.equals("OK")){
                                    Log.d("success","ya");
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
                                            //start new activity
                                            startActivity(new Intent(AccountDetailActivity.this,MainpageActivity.class));
                                            finish();
//                                        }
//                                    },1200); //1 sec
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
                    String id = bundle.getString("id");
                    Log.d("id",id);
                    Mysql mysql=new Mysql();
                    String query=mysql.updateUserDetailToMysql(id,selectHeight,selectWeight,ezcard.getText().toString());
                    params.put("query",query);
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
        adapter=new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
