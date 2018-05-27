package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import myandroidhello.com.ap_project.Data.MySingleTon;
import myandroidhello.com.ap_project.Data.Mysql;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Values;
import myandroidhello.com.ap_project.Font.FontHelper;
import myandroidhello.com.ap_project.Model.GlobalVariables;

public class LoginActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 1;
    //FB login
    LoginButton fbLoginButton;
    CallbackManager callbackManager;
    EditText loginText;
    Button accountkitButton;
    AppEventsLogger logger;
    String currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));
        logger = AppEventsLogger.newLogger(this);

        loginText=(EditText)findViewById(R.id.login_text);
        accountkitButton=(Button)findViewById(R.id.accountkit_button);
        accountkitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String login = loginText.getText().toString();
                if (login.contains("@")) {
                    logger.logEvent("emailLogin");
                    onAccountKitLogin(login, LoginType.EMAIL);
                } else {
                    logger.logEvent("phoneLogin");
                    onAccountKitLogin(login, LoginType.PHONE);
                }
            }
        });

        fbLoginButton=(LoginButton)findViewById(R.id.facebook_login_button);
        fbLoginButton.setReadPermissions("email");
        //login button callback registration
        callbackManager=CallbackManager.Factory.create();
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                currentId =loginResult.getAccessToken().getUserId().toString();
                Log.d("test", currentId);
                checkExistedAccount();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                //display error
                String toastMessage=error.getMessage();
                Toast.makeText(LoginActivity.this,toastMessage,Toast.LENGTH_LONG).show();

            }
        });


        // check for an existing access token
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        com.facebook.AccessToken loginToken = com.facebook.AccessToken.getCurrentAccessToken();
        if (accessToken != null || loginToken !=null) {
            // if previously logged in, proceed to the account activity
            Log.d("test","exist!!");
//            Log.d("test",String.valueOf(loginToken.getUserId()));
            Log.d("test",String.valueOf(accessToken.getAccountId()));
            GlobalVariables User = (GlobalVariables)getApplicationContext();
            if(accessToken != null){
                User.setId(accessToken.getAccountId());
            }else {
                User.setId(loginToken.getUserId());
            }
            launchMainpageActivity();
//            checkExistedAccount();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //forward result to the callback manager for Login Button
        callbackManager.onActivityResult(requestCode,resultCode,data);
        // For Account Kit, confirm that this response matches your request
        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                // display login error
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            } else if (loginResult.getAccessToken() != null) {
                // on successful login, proceed to the account activity
                Log.d("test",String.valueOf(loginResult.getAccessToken().getAccountId()));
                currentId=loginResult.getAccessToken().getAccountId();

//                launchAccountActivity();
                checkExistedAccount();
            }

        }
    }

    private void onAccountKitLogin(final String login, final LoginType loginType) {  //LoginType comes from Accountkit
        // create intent for the Account Kit activity
        final Intent intent = new Intent(this, AccountKitActivity.class);

        // configure login type and response type
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );

        if (loginType == LoginType.EMAIL) {
            configurationBuilder.setInitialEmail(login);
        }
        else {
            PhoneNumber phoneNumber = new PhoneNumber(Locale.getDefault().getCountry(), login, null);
            configurationBuilder.setInitialPhoneNumber(phoneNumber);
        }

        final AccountKitConfiguration configuration = configurationBuilder.build();

        // launch the Account Kit activity
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    private void launchAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }
    private void launchMainpageActivity(){
        Intent intent = new Intent(this, MainpageActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkExistedAccount(){
        Log.d("connect",String.valueOf(checkNetworkConnection()));
        if(checkNetworkConnection()){
            StringRequest stringRequest=new StringRequest(Request.Method.POST, Values.READ_DATA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GlobalVariables User = (GlobalVariables)getApplicationContext();
                            User.setId(currentId);
                            Log.d("test",response);
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.isNull("response")){
                                    launchAccountActivity();
                                }else {
                                    //set data to global
                                    launchMainpageActivity();
                                }
                            } catch (JSONException e) {
                                Log.d("error","json error");
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
                    Mysql mysql=new Mysql();
                    String query=mysql.checkExistId(currentId);
                    params.put("query",query);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleTon.getmInstance(LoginActivity.this).addToRequestque(stringRequest);
        }else{
            Log.d("error","map error");
        }


    }
    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }
}
