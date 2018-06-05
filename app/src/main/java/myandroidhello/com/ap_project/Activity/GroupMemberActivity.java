package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import myandroidhello.com.ap_project.Adapter.GmemberAdapter;
import myandroidhello.com.ap_project.Model.User;
import myandroidhello.com.ap_project.R;

public class GroupMemberActivity extends AppCompatActivity {
    private static final String TAG = "GroupMemberActivity";

    //vars
    private ListView mListView;
    private GmemberAdapter mAdapter;
    private ArrayList<User> mUser;
    private String HTTP_URL = "http://140.119.19.36:80/getmember.php";
    private String FinalJSonObject;
    private String gid;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);

        Intent intent = getIntent();
        gid = intent.getStringExtra("group_number");
        mListView = (ListView)findViewById(R.id.member_lv);
        back=findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });

        getMember();

    }

    private void getMember(){
        Log.d(TAG, "getMember: getting member data");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HTTP_URL + "?getid=" + gid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // After done Loading store JSON response in FinalJSonObject string variable.
                        FinalJSonObject = response ;

                        // Calling method to parse JSON object.
                        new ParseJSonDataClass(GroupMemberActivity.this).execute();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(GroupMemberActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(GroupMemberActivity.this);

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);
    }

    class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {

        public Context context;


        public ParseJSonDataClass(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                // Checking whether FinalJSonObject is not equals to null.
                if (FinalJSonObject != null) {

                    // Creating and setting up JSON array as null.
                    JSONArray jsonArray = null;
                    try {

                        // Adding JSON response object into JSON array.
                        jsonArray = new JSONObject(FinalJSonObject).getJSONArray("data");

                        // Creating JSON Object.
                        JSONObject jsonObject;

                        // Creating Subject class object.
                        User user;



                        // Defining CustomSubjectNamesList AS Array List.
                        mUser = new ArrayList<User>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            user = new User();

                            jsonObject = jsonArray.getJSONObject(i);

                            user.setUsername(jsonObject.getString("name"));
                            user.setPic_url(jsonObject.getString("pic_url"));
                            user.setUser_id(jsonObject.getString("fb_id"));


                            // Adding subject list object into CustomSubjectNamesList.
                            mUser.add(user);
                            Log.d(TAG, "doInBackground: mRankitem: " + mUser);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)

        {
            mAdapter = new GmemberAdapter(GroupMemberActivity.this,  R.layout.layout_gmember_item, mUser);
            mListView.setAdapter(mAdapter);

        }
    }

}
