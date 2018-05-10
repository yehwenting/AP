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

import myandroidhello.com.ap_project.Adapter.FriendAdapter;
import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.Model.GlobalVariables;

public class AddFriendActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView emptyText;
    List<FriendAdapter.FriendItem> friendList = new ArrayList<>();
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        recyclerView = (RecyclerView) findViewById(R.id.potentialFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        back=findViewById(R.id.back);
        findFriend();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddFriendActivity.this,FriendsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findFriend(){
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
                                    JSONObject friend = array.getJSONObject(i);
                                    String id = friend.getString("fb_id");
                                    String name = friend.getString("name");
                                    String image = friend.getString("pic_url");
                                    // insert new friend item in friendList
                                    FriendAdapter.FriendItem friends = new FriendAdapter.FriendItem(id, name, image);
                                    friendList.add(friends);
                                }
                            }
                            FriendAdapter friendsAdapter = new FriendAdapter(friendList);
                            recyclerView.setAdapter(friendsAdapter);

                            if (friendList.size() == 0) {
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
                String query=mysql.lookForPotentialFriend(User.getId());
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(AddFriendActivity.this).addToRequestque(stringRequest);


    }
}
