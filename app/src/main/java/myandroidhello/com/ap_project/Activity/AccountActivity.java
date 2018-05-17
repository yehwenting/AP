package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.login.LoginManager;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
import myandroidhello.com.ap_project.Font.FontHelper;
import myandroidhello.com.ap_project.Model.User;

public class AccountActivity extends AppCompatActivity {

    //TODO　typescreen
    public ProfileTracker profileTracker;
    public ImageView profilePic;
//    public TextView id;
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
    private String ID;
    private String picUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));

        name=findViewById(R.id.info);
        sex=findViewById(R.id.sex);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        student_id=findViewById(R.id.stuID);
        phoneNum=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        submit=findViewById(R.id.submit);

        // defined user's sex
        sex.setOnCheckedChangeListener(listener);

        // register a receiver for the onCurrentProfileChanged event
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    displayProfileInfo(currentProfile);
                }
            }
        };

        if (AccessToken.getCurrentAccessToken() != null) {
            // If there is an access token then Login Button was used
            // Check if the profile has already been fetched
            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                displayProfileInfo(currentProfile);
            } else {
                // Fetch the profile, which will trigger the onCurrentProfileChanged receiver
                Profile.fetchProfileForCurrentAccessToken();
            }
        } else {

            // Otherwise, get Account Kit login information
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    // get Account Kit ID
                    String accountKitId = account.getId();
                    ID=accountKitId;

                    PhoneNumber phoneNumber = account.getPhoneNumber();
                    if (account.getPhoneNumber() != null) {
                        // if the phone number is available, display it
                        String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                        phoneNum.setText(formattedPhoneNumber);
                    } else {
                        // if the email address is available, display it
                        String emailString = account.getEmail();
                        email.setText(emailString);
                    }

                }

                @Override
                public void onError(final AccountKitError error) {
                    String toastMessage = error.getErrorType().getMessage();
                    Toast.makeText(AccountActivity.this, toastMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO check if the user has already got the account
                Log.d("submit","click!!");
                if(name.getText().toString()!=null && sexT!=null &&
                        student_id.getText().toString()!=null &&
                        phoneNum.getText().toString()!=null && email.getText().toString()!=null){
                    saveInfoToMysql();
                }else{
                    Toast.makeText(AccountActivity.this, "please finish info above!!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void saveInfoToMysql(){
        Log.d("connect",String.valueOf(checkNetworkConnection()));
        if(checkNetworkConnection()){
            StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.lOGIN_SERVER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("success",response);
                                JSONObject jsonObject=new JSONObject(response);
                                String Response=jsonObject.getString("response");
                                if(Response.equals("OK")){
                                    Log.d("success","ya");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //start new activity
                                            Intent intent=new Intent(AccountActivity.this,AccountDetailActivity.class);
//                                            Bundle bundle = new Bundle();
//                                            bundle.putString("id",ID);
                                            try {
                                                nameCorrect= URLDecoder.decode(name.getText().toString(), "UTF-8");
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }

                                            User user=new User(ID,phoneNum.getText().toString(),nameCorrect);
                                            intent.putExtra("user", user);
                                            startActivity(intent);
//                                            finish();
                                        }
                                    },1200); //1 sec
                                }else{
//                                    saveGroceryToServer(view);
                                    Log.d("error","do not save to mysql");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.d("error","do not save to mysql 2");

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    try {
                        nameCorrect= URLDecoder.decode(name.getText().toString(), "UTF-8");
                        sexT=URLDecoder.decode(sexT, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d("id",sexT);
                    Mysql mysql=new Mysql();
                    String query=mysql.addUserToMysql(ID,nameCorrect,student_id.getText().toString(),phoneNum.getText().toString(),email.getText().toString(),sexT,picUrl);
                    params.put("query",query);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleTon.getmInstance(AccountActivity.this).addToRequestque(stringRequest);
        }else{
            Log.d("error","map error");
        }


    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        // unregister the profile tracker receiver
        profileTracker.stopTracking();
    }

    public void onLogout(View view) {
        // logout of Account Kit
        AccountKit.logOut();
        //log out of login button
        LoginManager.getInstance().logOut();

        launchLoginActivity();
    }

    private void displayProfileInfo(Profile profile) {
        // get Profile ID
        String profileId = profile.getId();
        ID=profileId;

        // display the Profile name
        String name = profile.getName();
        this.name.setText(name);

        // display the profile picture
        Uri profilePicUri = profile.getProfilePictureUri(100, 100);
        picUrl=String.valueOf(profilePicUri);
//        displayProfilePic(profilePicUri);
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
        }
        catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }

    private void displayProfilePic(Uri uri) {
        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(AccountActivity.this)
                .load(uri)
                .transform(transformation)
                .into(profilePic);
    }

    //define user's sex
    private RadioGroup.OnCheckedChangeListener listener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch (i){
                case R.id.male:
                    sexT=male.getText().toString();
                break;
                case R.id.female:
                    sexT=female.getText().toString();
                    break;
            }
        }
    };
}
