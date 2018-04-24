package myandroidhello.com.ap_project.Share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import myandroidhello.com.ap_project.Adapter.SectionsPagerAdapter;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.Permissions;

public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";

    private ViewPager mViewPager;

    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private Context mContext = ShareActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started");

        if (checkPermissionArray(Permissions.PERMISSIONS)){
            setUpViewPager();
        }else {
            verifyPermissions(Permissions.PERMISSIONS);
        }
    }

    /**
     * return the current fragment number
     * 0 = gallery fragment
     * 1 = photo fragment
     * @return
     */
    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }

    /**
     * setup viewPager for managing the tabs
     */

    @SuppressLint("WrongViewCast")
    private void setUpViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        tabLayout.getTabAt(1).setText(getString(R.string.photo));
    }

    public int getTask(){
//        Log.d(TAG, "getTask: TASK: "+ getIntent().getFlags());
        return getIntent().getFlags();
    }


    public void verifyPermissions(String[] permissions){
        Log.d("verifyPermissions:", "verifying permissions.");

        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );

    }

    /**
     * Check an array of permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissionArray(String[] permissions){
        Log.d("checkPermissionArray:", "checking permissions array.");

        for (int i = 0; i < permissions.length; i++){
            String Check = permissions[i];
            if (!checkPermissions(Check)){
                return false;
            }
        }
        return true;

    }

    /**
     * check a single permission if it has been verified
     * @param permission
     * @return
     */

    public boolean checkPermissions (String permission){
        Log.d("checkPermissions:", "checking permission"+ permission);
        int PermissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this, permission);
        if (PermissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d("checkPermissions:", "permission was not granted for " + permission );
            return false;
        }else {
            return true;
        }
    }

//    private void setupBottomNavigationView(){
//        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//    }
}
