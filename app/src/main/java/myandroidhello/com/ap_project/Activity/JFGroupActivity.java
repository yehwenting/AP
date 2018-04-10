package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.florent37.bubbletab.BubbleTab;

import butterknife.Bind;
import butterknife.ButterKnife;
import myandroidhello.com.ap_project.Adapter.AddgroupFakeAdapter;
import myandroidhello.com.ap_project.R;

public class JFGroupActivity extends Navigation_BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;


    @Bind(R.id.bubbleTab)
    BubbleTab bubbleTab;
    @Bind(R.id.viewPager)
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bubbletab_group);
        ButterKnife.bind(this);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        toolBar_title=findViewById(R.id.toolbar_title);

        //toolbar
        toolbar.setTitle("");//設置ToolBar Title
        toolBar_title.setText("揪 團");
        setUpToolBar();//使用父類別的setUpToolBar()，設置ToolBar

        //bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.reserve:
                                Intent intent=new Intent(JFGroupActivity.this, ReserveActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.group:
                                recreate();
                                break;
                            case R.id.start:
                                break;
                        }

                        return true;
                    }
                });




        viewPager.setAdapter(new AddgroupFakeAdapter(getSupportFragmentManager()));
        bubbleTab.setupWithViewPager(viewPager);
    }
}
