package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.Set;

import myandroidhello.com.ap_project.R;

public class TestActivity extends AppCompatActivity {

    Button test;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        test=findViewById(R.id.testBtn);
        callbackManager = CallbackManager.Factory.create();


        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //post
                Set permissions = AccessToken.getCurrentAccessToken().getPermissions();
                if (permissions.contains("publish_actions")) {
                    Log.d("ppp","publish_actions exist!");
                    post();
                }
                else {
                    // prompt user to grant permissions
                    LoginManager loginManager = LoginManager.getInstance();
                    loginManager.logInWithPublishPermissions(TestActivity.this, Arrays.asList("publish_actions"));
                    Log.d("ppp","not exist");
                    loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            post();
                        }

                        @Override
                        public void onCancel() {
                            Log.d("ppp","cancel");

                            // inform user that permission is required
                            String permissionMsg = getResources().getString(R.string.permission_message);
                            Toast.makeText(TestActivity.this, permissionMsg, Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.d("ppp","error");
                            error.printStackTrace();
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Forward result to the callback manager for Login Button
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void post(){
        Log.d("ppp","post");
        Bundle params = new Bundle();
        params.putString("message", "hello!!");
/* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                params,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Toast.makeText(TestActivity.this, "success to post on fb", Toast.LENGTH_LONG).show();
            /* handle the result */
                    }
                }
        ).executeAsync();

    }

    }
