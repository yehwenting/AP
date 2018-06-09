package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;

public class FriendInfoActivity extends AppCompatActivity {
    Button addfriend, sent;
    ImageView pic, back;
    EditText message;
    TextView pName, intro,gold,silver,brown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        back = findViewById(R.id.back);
        addfriend = findViewById(R.id.addfriend);
        pic = findViewById(R.id.personalPic1);
        sent = findViewById(R.id.sent);
        pName = findViewById(R.id.p_name1);
        intro = findViewById(R.id.pintro);
        message = findViewById(R.id.message);
        message.setSelected(false);
        back.setOnClickListener(view -> finish());
        final Intent intent = this.getIntent();
        String id = intent.getStringExtra("id");
        displayFriend(id);
        addfriend.setOnClickListener(view -> {
            if (addfriend.getText().equals("戰 友")) {
                Toast.makeText(FriendInfoActivity.this, "你們已是好戰友", Toast.LENGTH_LONG).show();
            } else {
                becomeFriend(id);
            }
        });
        sent.setOnClickListener(view -> {
            savemessage(id);
        });

        gold=findViewById(R.id.gold);
        silver=findViewById(R.id.silver);
        brown=findViewById(R.id.brown);
        int num1=(int)((Math.random())*4+1);
        int num2=(int)((Math.random())*8+1);
        int num3=(int)((Math.random())*12+1);
        gold.setText("金牌: "+num1);
        silver.setText("銀牌: "+num2);
        brown.setText("銅牌: "+num3);


    }

    private void displayFriend(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Values.GET_FRIENDINFO_URL,
                response -> {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        JSONArray subArray = jsonObject.getJSONArray("response");
                        Log.d("t1", String.valueOf(jsonObject));
                        String content = subArray.getJSONObject(0).getString("ezcard");
                        String name = subArray.getJSONObject(0).getString("name");
                        String picurl = subArray.getJSONObject(0).getString("pic_url");
                        displayProfilePic(pic, picurl);
                        pName.setText(name);
                        intro.setText(content);
                        String isfd = jsonObject.getString("isfriend");
                        if (isfd.equals("true")) {
                            addfriend.setText("戰 友");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("error", "do not get equipment from mysql 2");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                GlobalVariables user = (GlobalVariables) getApplicationContext();
                params.put("fid", id);
                params.put("uid", user.getId());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(FriendInfoActivity.this).addToRequestque(stringRequest);
    }

    private void displayProfilePic(ImageView imageView, String url) {
        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(true)
                .build();
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.user)
                .transform(transformation)
                .into(imageView);
    }
    private void becomeFriend(String fid){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("success",response);
                            JSONObject jsonObject=new JSONObject(response);
                            addfriend.setText("戰 友");
                            Toast.makeText(FriendInfoActivity.this, "已成為戰友", Toast.LENGTH_LONG).show();
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
                GlobalVariables User=(GlobalVariables)getApplicationContext();
                String query=mysql.addFriend(User.getId(),fid);
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(FriendInfoActivity.this).addToRequestque(stringRequest);
    }
    private void savemessage(String fid){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                response -> {
                    try {
                        Log.d("success",response);
                        JSONObject jsonObject=new JSONObject(response);
                        Toast.makeText(FriendInfoActivity.this, "留言已傳送", Toast.LENGTH_LONG).show();
                        message.setText("");
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                GlobalVariables User=(GlobalVariables)getApplicationContext();
                String query=mysql.addMessage(User.getId(),fid,message.getText().toString());
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(FriendInfoActivity.this).addToRequestque(stringRequest);
    }

}
