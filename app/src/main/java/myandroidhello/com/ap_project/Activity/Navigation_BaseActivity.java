package myandroidhello.com.ap_project.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.model.GlobalVariables;

/**
 * Created by Yehwenting on 2017/12/12.
 */

public class Navigation_BaseActivity extends AppCompatActivity {
    private DrawerLayout DL;
    private FrameLayout FL;
    private ImageView userPhoto;
    private TextView userName;
    protected NavigationView NV;
    protected Toolbar toolbar;
    protected int CurrentMenuItem = 8;//紀錄目前User位於哪一個項目
    private LinearLayout LL;
    private View view;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        DL = (DrawerLayout) getLayoutInflater().inflate(R.layout.navigation_drawer, null);
        LL = (LinearLayout) getLayoutInflater().inflate(R.layout.navigation_drawer_header,null);
        FL = DL.findViewById(R.id.content_frame);
        NV = DL.findViewById(R.id.Left_Navigation);
        view=NV.getHeaderView(0);
        userPhoto=view.findViewById(R.id.navigation_header_userPhoto);
        userName=view.findViewById(R.id.navigation_header_userID);
        getLayoutInflater().inflate(layoutResID, FL, true);
        super.setContentView(DL);
        toolbar =findViewById(R.id.toolbar);
        GlobalVariables User = (GlobalVariables)getApplicationContext();
        userName.setText(User.getName());
        Log.d("ooooo",userName.getText().toString());
        if(User.getUrl().equals("")) {
            Log.d("777","no photo");
        }else{
            displayProfilePic(userPhoto,User.getUrl());
        }
        setUpNavigation();




    }

    private void setUpNavigation() {



        // Set navigation item selected listener
        NV.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(!(menuItem == NV.getMenu().getItem(CurrentMenuItem))) {//判斷使者者是否點擊當前畫面的項目，若不是，根據所按的項目做出分別的動作
                    switch (menuItem.getItemId()) {
                        case R.id.navItemOne:
                            Log.d("rrrrr","rrrrrr");
                            Intent intent = new Intent();
                            intent.setClass(Navigation_BaseActivity.this, MainpageActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                            break;
                        case R.id.navItemTwo:
                            Intent intent2 = new Intent();
                            intent2.setClass(Navigation_BaseActivity.this, PersonInfoActivity.class);
                            startActivity(intent2);
                            overridePendingTransition(0, 0);
                            finish();
                            break;
                        case R.id.navItemThree:
                            Intent intent3 = new Intent();
                            intent3.setClass(Navigation_BaseActivity.this, TestActivity.class);
                            startActivity(intent3);
                            overridePendingTransition(0, 0);
                            break;
                        case R.id.navItemEight:
                            Intent intent1=new Intent(Navigation_BaseActivity.this,FriendsActivity.class);
                            startActivity(intent1);
                            overridePendingTransition(0, 0);

                            break;
                        case R.id.navItemSeven:
                            new AlertDialog.Builder(Navigation_BaseActivity.this)
                                    .setTitle("Logout")
                                    .setMessage("Are you sure you want to Logout?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // logout of Account Kit
                                            AccountKit.logOut();
                                            //log out of login button
                                            LoginManager.getInstance().logOut();
                                            Intent intent3 = new Intent(Navigation_BaseActivity.this, LoginActivity.class);
                                            startActivity(intent3);
                                            finish();
                                        }

                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                            break;
                    }
                }
                else {//點擊當前項目時，收起Navigation
                    DL.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });

    }
    public void setUpToolBar() {//設置ToolBar
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DL.openDrawer(GravityCompat.START);
            }
        });
        //設定當使用者點擊ToolBar中的Navigation Icon時，Icon會隨著轉動
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle( this, DL, toolbar,R.string.open_navigation,R.string.close_navigation){
            @Override
            public void onDrawerClosed(View drawerView) {
                super .onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super .onDrawerOpened(drawerView);
            }
        };
        DL.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
    private void displayProfileInfo(Profile profile) {

        // display the Profile name
        String name = profile.getName();
        this.userName.setText(name);

        // display the profile picture
        Uri profilePicUri = profile.getProfilePictureUri(100, 100);
//        displayProfilePic(profilePicUri);
    }

    private void displayProfilePic(ImageView imageView, String url) {
        Log.d("777","photo"+url);
        Log.d("777",String.valueOf(imageView.getContext()));

        // helper method to load the profile pic in a circular imageview
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.user)
                .transform(transformation)
                .into(imageView);
    }


}


