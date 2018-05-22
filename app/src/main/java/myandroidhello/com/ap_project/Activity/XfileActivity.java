package myandroidhello.com.ap_project.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import myandroidhello.com.ap_project.Adapter.SectionsPagerAdapter;
import myandroidhello.com.ap_project.Fragment.XGroupFragment;
import myandroidhello.com.ap_project.Fragment.XReserveFragment;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Util.UniversalImageLoader;

public class XfileActivity extends Navigation_BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;

    private static final String TAG = "XfileActivity";

    private ViewPager mViewPager;
    private Context mContext = XfileActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfile);
        Log.d(TAG, "onCreate: starting..");

        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        toolBar_title=findViewById(R.id.toolbar_title);

        //toolbar
        toolbar.setTitle("");//設置ToolBar Title
        toolBar_title.setText(R.string.view_five);
        setUpToolBar();//使用父類別的setUpToolBar()，設置ToolBar
        CurrentMenuItem = 3;
        NV.getMenu().getItem(CurrentMenuItem).setChecked(true);//設置Navigation目前項目被選取狀態
        //bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.reserve:
                                Intent intent=new Intent(XfileActivity.this, ReserveActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.group:
                                Intent intent1=new Intent(XfileActivity.this, JFGroupActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.start:
                                Intent intent2=new Intent(XfileActivity.this, MenuActivity.class);
                                startActivity(intent2);
                                break;
                        }

                        return true;
                    }
                });

        setupViewPager();
        initImageLoader();
    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    /**
     * Responsible for adding two fragment: RankAll fragment and RankByType fragment
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new XReserveFragment());//index 0
        adapter.addFragment(new XGroupFragment()); //index 1

        mViewPager.setAdapter(adapter);

//        adapter.notifyDataSetChanged();
//        mViewPager.invalidateBullets(adapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("預 約");
        tabLayout.getTabAt(1).setText("揪 團");

    }

}

