package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.Model.GlobalVariables;

public class CreateCGroupActivity extends AppCompatActivity {

    TextView compete,name,time,place,num,note;
    Button create;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cgroup);
        context=getApplicationContext();

        compete=findViewById(R.id.com);
        Intent intent = this.getIntent();
        compete.setText(intent.getStringExtra("activity"));
        name=findViewById(R.id.cgName);
        time=findViewById(R.id.cgTime);
        place=findViewById(R.id.cgPlace);
        num=findViewById(R.id.cgNum);
        note=findViewById(R.id.cgContent);
        create=findViewById(R.id.cg_button);
        int max=Integer.parseInt(intent.getStringExtra("maxnum"));
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("")||
                        time.getText().toString().equals("")||
                        place.getText().toString().equals("")||
                        num.getText().toString().equals("")||
                        note.getText().toString().equals("")||compete.getText().toString().equals("")){
                    Toast.makeText(CreateCGroupActivity.this, "所有資料都須填寫才能發布揪團唷!", Toast.LENGTH_LONG).show();
                }else if(Integer.parseInt(num.getText().toString())>max){
                    String text="揪團人數大於上限"+max+"人";
                    Toast.makeText(CreateCGroupActivity.this, text, Toast.LENGTH_LONG).show();

                }else{
                    saveGroup();
                }
            }
        });
    }

    private void saveGroup(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Values.CREATE_COMPETITION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("rrrrr",response);
                            JSONObject jsonObject = new JSONObject(response);
                            android.support.v7.app.AlertDialog.Builder alert=new android.support.v7.app.AlertDialog.Builder(CreateCGroupActivity.this);
                            alert.setMessage("成功新增");
                            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(CreateCGroupActivity.this,"來加入別人的團吧！",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(CreateCGroupActivity.this,JFGroupActivity.class);
                                    startActivity(intent);
                                }
                            });
                            alert.show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("error","do not save group to mysql");
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                GlobalVariables user=(GlobalVariables)context.getApplicationContext();
                String uid=user.getId();
                int remain=Integer.parseInt(num.getText().toString())-1;
                Mysql mysql=new Mysql();
                String query = mysql.createCompetition(uid,name.getText().toString(),time.getText().toString(),place.getText().toString(),num.getText().toString(),
                        note.getText().toString(),compete.getText().toString(),String.valueOf(remain));
                params.put("query", query);
                params.put("uid",uid);
                params.put("name",compete.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(context).addToRequestque(stringRequest);



    }
}
