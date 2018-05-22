package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Share.ShareActivity;

public class StartExercise2Activity extends Navigation_BaseActivity {

    private static final String TAG = "StartExercise2Activity";

    private TextView exerciseSummary;
    private TextView equipName_tv;
    private Button postBtn;
    private String HTTP_URL = "http://140.119.19.36:80/getXray.php";
    private String FinalJSonObject;
    private List<ImageView> friends;
    private ImageView f1;
    private ImageView f2;
    private ImageView f3;
    private ImageView f4;
    private ImageView f5;
    GlobalVariables User = (GlobalVariables)getApplicationContext();

    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise2);

        final Intent intent = this.getIntent();
        String exerciseDuration = intent.getStringExtra("exerciseDuration");
        String equipName = intent.getStringExtra("equipName");

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
                                Intent intent=new Intent(StartExercise2Activity.this, ReserveActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.group:
                                Intent intent1=new Intent(StartExercise2Activity.this, JFGroupActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.start:
                                Intent intent2=new Intent(StartExercise2Activity.this, MenuActivity.class);
                                startActivity(intent2);
                                break;
                        }

                        return true;
                    }
                });

        exerciseSummary = (TextView)findViewById(R.id.exerciseSummary);
        exerciseSummary.setText(exerciseDuration);

        equipName_tv = (TextView)findViewById(R.id.equip_name);
        equipName_tv.setText(equipName);


        postBtn = (Button)findViewById(R.id.postBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(StartExercise2Activity.this, ShareActivity.class);
                startActivity(intent);
            }
        });

        f1 = (ImageView)findViewById(R.id.friend1);
        f2 = (ImageView)findViewById(R.id.friend2);
        f3 = (ImageView)findViewById(R.id.friend3);
        f4 = (ImageView)findViewById(R.id.friend4);
        f5 = (ImageView)findViewById(R.id.friend5);
        friends.add(f1);
        friends.add(f2);
        friends.add(f3);
        friends.add(f4);
        friends.add(f5);

        getXray();
    }
    private void displayProfilePic(ImageView imageView, String url) {
        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.user)
                .transform(transformation)
                .into(imageView);
    }

    private void getXray(){
        Log.d(TAG, "getXray: getting xray friend list");
        StringRequest stringRequest = new StringRequest(HTTP_URL + "?uid = " + User.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // After done Loading store JSON response in FinalJSonObject string variable.
                        FinalJSonObject = response ;

                        // Calling method to parse JSON object.
                        new ParseJSonDataClass(StartExercise2Activity.this).execute();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // Showing error message if something goes wrong.
                        Toast.makeText(StartExercise2Activity.this,error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(StartExercise2Activity.this);

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);

    }

    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {

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


                        for (int i = 0; i < jsonArray.length(); i++) {

//                    
                            for (int j = 0; j < friends.size(); j++){
                                jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject != null) displayProfilePic(friends.get(j), jsonObject.getString("pic_url"));
                            }

//                            home2item.setContent(jsonObject.getString("content"));
//                            home2item.setDate(convertTime(jsonObject.getString("date")));
//                            home2item.setPic_path(jsonObject.getString("icon_url"));
//                            home2item.setUid(jsonObject.getString("uid"));
//                            home2item.setLongdate(Long.parseLong(jsonObject.getString("date")));


                            // Adding subject list object into CustomSubjectNamesList.
                            //mHome2items.add(home2item);
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
            //display();

        }
    }

}
