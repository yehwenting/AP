package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.font.FontHelper;
import myandroidhello.com.ap_project.model.GlobalVariables;

/**
 * Created by Yehwenting on 2018/3/25.
 */

public class EditPInfoActivity extends AppCompatActivity {
    public EditText name;
    public EditText student_id;
    public EditText phoneNum;
    public EditText email;
    public Button submit;
    public RadioGroup sex;
    public RadioButton male;
    public RadioButton female;
    public String sexT;
    public String nameCorrect;
    private String picUrl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));

        name = findViewById(R.id.info);
        sex = findViewById(R.id.sex);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        student_id = findViewById(R.id.stuID);
        phoneNum = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        submit = findViewById(R.id.submit);

        // defined user's sex
        sex.setOnCheckedChangeListener(listener);
        displayProfileInfo();

        submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO check if the user has already got the account
                    Log.d("submit", "click!!");
                    if (name.getText().toString() != null && sexT != null &&
                            student_id.getText().toString() != null &&
                            phoneNum.getText().toString() != null && email.getText().toString() != null) {
                        saveInfoToMysql();
                    } else {
                        Toast.makeText(EditPInfoActivity.this, "please finish info above!!", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

    private void saveInfoToMysql() {
        Log.d("connect", String.valueOf(checkNetworkConnection()));
        if (checkNetworkConnection()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("success", response);
                                JSONObject jsonObject = new JSONObject(response);
                                String Response = jsonObject.getString("response");
                                if (Response.equals("OK")) {
                                    Log.d("success", "ya");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            GlobalVariables User = (GlobalVariables)getApplicationContext();
                                            User.setSex(sexT);
                                            User.setStuId(student_id.getText().toString());
                                            User.setPhoneNum(phoneNum.getText().toString());
                                            User.setEmail(email.getText().toString());
                                            //start new activity
                                            Intent intent = new Intent(EditPInfoActivity.this, EditPInfoDetailActivity.class);
//
                                            try {
                                                nameCorrect = URLDecoder.decode(name.getText().toString(), "UTF-8");
                                                User.setName(nameCorrect);
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }

                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 1200); //1 sec
                                } else {
//                                    saveGroceryToServer(view);
                                    Log.d("error", "do not save to mysql");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.d("error", "do not save to mysql 2");

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    try {
                        nameCorrect = URLDecoder.decode(name.getText().toString(), "UTF-8");
                        sexT = URLDecoder.decode(sexT, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d("id", sexT);
                    GlobalVariables User = (GlobalVariables)getApplicationContext();
                    String ID=User.getId();
                    Mysql mysql = new Mysql();
                    String query = mysql.updateUserToMysql(ID, nameCorrect, student_id.getText().toString(), phoneNum.getText().toString(), email.getText().toString(), sexT);
                    params.put("query", query);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleTon.getmInstance(EditPInfoActivity.this).addToRequestque(stringRequest);
        } else {
            Log.d("error", "map error");
        }


    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    private void displayProfileInfo() {
        GlobalVariables User = (GlobalVariables)getApplicationContext();

        // display the Profile name
        String name = User.getName();
        this.name.setText(name);
        String stuId=User.getStuId();
        student_id.setText(stuId);
        String phone=User.getPhoneNum();
        phoneNum.setText(phone);
        String email1=User.getEmail();
        email.setText(email1);
        String sex1=User.getSex();
        if(sex1=="女"){
            sex.check(R.id.female);
        }else{
            sex.check(R.id.male);
        }
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String formatPhoneNumber(String phoneNumber) {
        // helper method to format the phone number for display
        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

//    private void displayProfilePic(Uri uri) {
//        // helper method to load the profile pic in a circular imageview
//        Transformation transformation = new RoundedTransformationBuilder()
//                .cornerRadiusDp(30)
//                .oval(false)
//                .build();
//        Picasso.with(AccountActivity.this)
//                .load(uri)
//                .transform(transformation)
//                .into(profilePic);
//    }

    //define user's sex
    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch (i) {
                case R.id.male:
                    sexT = male.getText().toString();
                    break;
                case R.id.female:
                    sexT = female.getText().toString();
                    break;
            }
        }
    };
}


