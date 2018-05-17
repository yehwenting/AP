package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;

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
import myandroidhello.com.ap_project.Font.FontHelper;
import myandroidhello.com.ap_project.Model.GlobalVariables;

public class FriendsActivity extends Navigation_BaseActivity {

    RecyclerView recyclerView;
    TextView emptyText;
    String isFriend="uuuu";
    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;
    ImageView addFriend;
    List<FriendAdapter.FriendItem> friendList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));

        toolBar_title=findViewById(R.id.toolbar_title);


        //toolbar
        toolbar.setTitle("");//設置ToolBar Title
        toolBar_title.setText(R.string.view_eight);
        setUpToolBar();//使用父類別的setUpToolBar()，設置ToolBar
        CurrentMenuItem = 2;
        NV.getMenu().getItem(CurrentMenuItem).setChecked(true);//設置Navigation目前項目被選取狀態

        //bottomNavigation
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.reserve:
                                Intent intent=new Intent(FriendsActivity.this, ReserveActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.group:
                                Intent intent1=new Intent(FriendsActivity.this, JFGroupActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.start:
                                Intent intent2=new Intent(FriendsActivity.this, MenuActivity.class);
                                startActivity(intent2);
                                break;
                        }

                        return true;
                    }
                });

        if (AccessToken.getCurrentAccessToken() == null) {
            // a Facebook Login access token is required
            finish();
            return;
        }

        emptyText = findViewById(R.id.emptytext);
        addFriend = findViewById(R.id.addFriendBtn);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                findFriend();
                Intent intent=new Intent(FriendsActivity.this,AddFriendActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.friends_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        getFriends();
    }


    private void getFriends(){
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
                String query=mysql.getFriends(User.getId());
                params.put("query",query);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleTon.getmInstance(FriendsActivity.this).addToRequestque(stringRequest);

    }


    }


