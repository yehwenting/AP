package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myandroidhello.com.ap_project.Adapter.MessageAdapter;
import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;

public class MessageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView emptyText;
    ImageView back;
    List<MessageAdapter.MessageItem> massageList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        emptyText=findViewById(R.id.empty);
        back=findViewById(R.id.back);
        recyclerView = (RecyclerView) findViewById(R.id.m_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        back.setOnClickListener(view -> {
            Intent intent=new Intent(MessageActivity.this,PersonInfoActivity.class);
            startActivity(intent);

        });
        getMessage();
    }
    private void getMessage(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.READ_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("success",response);
                            JSONObject jsonObject=new JSONObject(response);
                            Log.d("success",jsonObject.getString("response"));
                            JSONArray array = jsonObject.getJSONArray("response");
                            if (array.length()==0){
                                emptyText.setVisibility(View.VISIBLE);
                            }else {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject message = array.getJSONObject(i);
                                    String id = message.getString("uid");
                                    String name = message.getString("name");
                                    String image = message.getString("pic_url");
                                    String content = message.getString("content");

                                    // insert new friend item in friendList
                                    MessageAdapter.MessageItem messages = new MessageAdapter.MessageItem(id, name, image,content);
                                    massageList.add(messages);
                                }
                            }
                            MessageAdapter friendsAdapter = new MessageAdapter(massageList);
                            recyclerView.setAdapter(friendsAdapter);

                            if (massageList.size() == 0) {
                                // show message when the list is empty
                                emptyText.setVisibility(View.VISIBLE);
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
                GlobalVariables User=(GlobalVariables)getApplicationContext();
                String query=mysql.getMessage(User.getId());
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(MessageActivity.this).addToRequestque(stringRequest);
    }
}
