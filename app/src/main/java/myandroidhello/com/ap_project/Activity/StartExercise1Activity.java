package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import myandroidhello.com.ap_project.R;

public class StartExercise1Activity extends AppCompatActivity {
    private static final String TAG = "StartExercise1";

    private String uid = null;
    private String uname = null;
    private String pname = null;
    private String ename = null;
    private boolean firstStartClick = true;
    private DatabaseReference mDatabase;
    MediaPlayer xray;

    private Button startBtn;
    private Button pauseBtn;
    private Button stopBtn;
    private TextView timerValue;
    private TextView exerciseStatus;
    private long startTime = 0L;
    private String sTime ;
    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    com.android.volley.RequestQueue requestQueue;
    String Url = "http://140.119.19.36:80/saveStory.php";
    String Url2 = "http://140.119.19.36:80/saveUseData.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise1);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        final Intent intent = this.getIntent();
        uid = intent.getStringExtra("uid");
        uname = intent.getStringExtra("uname");
        pname = intent.getStringExtra("pname");
        ename = intent.getStringExtra("ename");

        timerValue = (TextView)findViewById(R.id.timerValue);
        exerciseStatus = (TextView)findViewById(R.id.statusTv);
        startBtn = (Button)findViewById(R.id.startBtn);
        LottieAnimationView animationView = findViewById(R.id.animation_view);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exerciseStatus.setText("努力運動中...");

                customHandler.postDelayed(updateTimerThread, 0);
                startTime = SystemClock.uptimeMillis();

                String content = uname + "現在正在" + pname + "使用" + ename;
                if (firstStartClick) {
                    firstStartClick = false;
                    sTime = String.valueOf(System.currentTimeMillis());
                    Log.d(TAG, "onClick: sTime = "+content);
                    Log.d("onClick", Url + "?uid=" + uid + "&place=" + pname + "&content=" + content + "&date=" + sTime);

                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, Url + "?uid=" + uid + "&place=" + pname +  "&date=" + sTime+"&content=" + content , new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("onClick", Url + "?uid=" + uid + "&place=" + pname + "&content=" + content + "&date=" + sTime);


                                }


                            }, new Response.ErrorListener() {


                                @Override


                                public void onErrorResponse(VolleyError error) {
//                                if (error.getMessage() != null)
//                                    Log.d("11111111",error.getMessage());
                                    VolleyLog.e("error", error.getMessage());
//


                                }

                            });

                    requestQueue.add(jsonObjectRequest);

                }
            }
        });
        pauseBtn = (Button)findViewById(R.id.pauseBtn);

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exerciseStatus.setText("休息是為了走更長遠的路！");

                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
            }
        });

        stopBtn = (Button)findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);

                requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, Url2 + "?uid=" + uid + "&ename=" + ename + "&start_time=" + sTime + "&end_time=" + System.currentTimeMillis(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("1111111", "onResponse");

                            }


                        }, new Response.ErrorListener() {

                            @Override

                            public void onErrorResponse(VolleyError error) {
//                                if (error.getMessage() != null)
//                                    Log.d("11111111",error.getMessage());
                                VolleyLog.e("error", error.getMessage());
//
                            }

                        });

                requestQueue.add(jsonObjectRequest);

                Intent intent = new Intent(StartExercise1Activity.this, StartExercise2Activity.class);
                intent.putExtra("exerciseDuration", timerValue.getText());
                intent.putExtra("equipName", ename);
                intent.putExtra("placeName",pname);
                startActivity(intent);
                finish();
            }
        });

        //receive firebase encouragement
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        //before adding Listener, delete the data in the database first
        myRef.removeValue();

        //add childeventListener to check if anyone sent encouragement to the user
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                String key = dataSnapshot.getKey();
                if (value.equals(uid)){
                    Toast.makeText(StartExercise1Activity.this, key + "向你發送X光波！", Toast.LENGTH_LONG).show();
                    animationView.setVisibility(View.VISIBLE);
                    animationView.playAnimation();
                    xray = MediaPlayer.create(StartExercise1Activity.this, R.raw.magic_wand);
                    xray.start();
                    Log.d(TAG, "onChildAdded: media play");
                    xray.setLooping(true);
                    xray.setVolume(100, 100);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(4000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        animationView.setVisibility(View.INVISIBLE);
                                        xray.stop();
                                    }
                                });
                            }catch (Exception e){

                            }

                        }
                    }).start();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                String key = dataSnapshot.getKey();
                if (value.equals(uid)){
                    Toast.makeText(StartExercise1Activity.this, key + "向你發送X光波！", Toast.LENGTH_LONG).show();
                    animationView.setVisibility(View.VISIBLE);
                    animationView.playAnimation();
                    xray = MediaPlayer.create(StartExercise1Activity.this, R.raw.magic_wand);
                    xray.start();
                    xray.setLooping(true);
                    xray.setVolume(100, 100);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(4000);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        animationView.setVisibility(View.INVISIBLE);
                                        xray.stop();
                                    }
                                });
                            }catch (Exception e){

                            }

                        }
                    }).start();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: failed to read value: " + databaseError);
            }
        });
    }



    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int)(updatedTime/1000);
            int mins = secs / 60;
            int hours = mins / 60;
            mins = mins % 60;
            secs = secs % 60;
            int milliseconds = (int)(updatedTime % 1000);
            timerValue.setText(String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs) );
            customHandler.postDelayed(this,0);

        }
    };
}
