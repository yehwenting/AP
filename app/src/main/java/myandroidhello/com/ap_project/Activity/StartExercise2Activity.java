package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import myandroidhello.com.ap_project.Model.GlobalVariables;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Share.ShareActivity;

public class StartExercise2Activity extends Navigation_BaseActivity implements View.OnClickListener{

    private static final String TAG = "StartExercise2Activity";

    private TextView exerciseSummary;
    private TextView equipName_tv,place_tv,consume;
    private Button postBtn;
    private String HTTP_URL = "http://140.119.19.36:80/getXray.php";
    private String FinalJSonObject,exerciseDuration,equipName,placeName;
    private List<ImageView> friends = new ArrayList<>();
    private List<String> pic_url = new ArrayList<>();
    private List<String> friends_name = new ArrayList<>();
    private List<String> friends_id = new ArrayList<>();
    private List<TextView> fsname = new ArrayList<>();
    private ImageView f1;
    private ImageView f2;
    private ImageView f3;
    private ImageView f4;
    private ImageView f5;
    private TextView fname1,fname2,fname3,fname4,fname5;
    private View facebook;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    GlobalVariables User = (GlobalVariables) FacebookSdk.getApplicationContext();

    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise2);

        callbackManager = CallbackManager.Factory.create();

        final Intent intent = this.getIntent();
         exerciseDuration = intent.getStringExtra("exerciseDuration");
         equipName = intent.getStringExtra("equipName");
         placeName = intent.getStringExtra("placeName");

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
        place_tv=findViewById(R.id.place_tv);
        place_tv.setText(placeName);



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
        f1.setOnClickListener(this);
        f2.setOnClickListener(this);
        f3.setOnClickListener(this);
        f4.setOnClickListener(this);
        f5.setOnClickListener(this);

        fname1=findViewById(R.id.fname1);
        fname2=findViewById(R.id.fname2);
        fname3=findViewById(R.id.fname3);
        fname4=findViewById(R.id.fname4);
        fname5=findViewById(R.id.fname5);
        Log.d("hhh",f1.getContentDescription().toString());
        friends.add(f1);
        friends.add(f2);
        friends.add(f3);
        friends.add(f4);
        friends.add(f5);
        fsname.add(fname1);
        fsname.add(fname2);
        fsname.add(fname3);
        fsname.add(fname4);
        fsname.add(fname5);
        facebook=findViewById(R.id.facebook);
        facebook.setOnClickListener(view -> {
            shareDialog = new ShareDialog(this);
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(StartExercise2Activity.this, "已分享運動成就至臉書!!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });
            String text="#Xer翻轉你的運動生活";
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://www.facebook.com/profile.php?id=100000395296933"))
                        .setShareHashtag(new ShareHashtag.Builder()
                                .setHashtag(text)
                                .build()
                                )
                        .build();
                shareDialog.show(linkContent);
            }
        });
        consume=findViewById(R.id.consume);
        GlobalVariables user=(GlobalVariables)getApplicationContext();
        String[] split_line = exerciseDuration.split(":");
        int min= Integer.parseInt(split_line[0])*60+Integer.parseInt(split_line[1]);
        int weight=Integer.parseInt(user.getWeight());
        double calories=weight*(3.3/60)*min;
        DecimalFormat df = new DecimalFormat("######.00");
        String text = String.valueOf(df.format(calories));
        consume.setText(text);
        getXray();
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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


    private void getXray(){
        Log.d(TAG, "getXray: getting xray friend list");
        StringRequest stringRequest = new StringRequest(HTTP_URL + "?uid=" + User.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: uid = " + User.getId());

                        // After done Loading store JSON response in FinalJSonObject string variable.
                        FinalJSonObject = response ;

                        // Calling method to parse JSON object.
                        new ParseJSonDataClass(StartExercise2Activity.this).execute();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: uid = " + User.getId());
                        // Showing error message if something goes wrong.
                        Toast.makeText(StartExercise2Activity.this,error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

        // Creating String Request Object.
        RequestQueue requestQueue = Volley.newRequestQueue(StartExercise2Activity.this);

        // Passing String request into RequestQueue.
        requestQueue.add(stringRequest);

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(StartExercise2Activity.this,FriendInfoActivity.class);
        intent.putExtra("id", view.getContentDescription());
        startActivity(intent);
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
                                jsonObject = jsonArray.getJSONObject(i);
                                Log.d(TAG, "doInBackground: pic_url:" + jsonObject.getString("pic_url"));

                                if (jsonObject != null){
                                    pic_url.add(jsonObject.getString("pic_url"));
                                    friends_name.add(jsonObject.getString("name"));
                                    friends_id.add(jsonObject.getString("fb_id"));
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
            for (int i = 0; i < pic_url.size(); i++){
                if (pic_url.get(i) != null)
                    displayProfilePic(friends.get(i), pic_url.get(i));
                    friends.get(i).setContentDescription(friends_id.get(i));

                if(friends_name.get(i) !=null){
                    fsname.get(i).setText(friends_name.get(i));
                }
            }

        }
    }

}
