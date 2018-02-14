package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import myandroidhello.com.ap_project.R;

public class MainpageActivity extends Navigation_BaseActivity {


    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);


        bottomNavigationView=findViewById(R.id.bottom_navigation);

        //toolbar
        toolbar.setTitle(R.string.view_one);//設置ToolBar Title
        setUpToolBar();//使用父類別的setUpToolBar()，設置ToolBar
        CurrentMenuItem = 0;
        NV.getMenu().getItem(CurrentMenuItem).setChecked(true);//設置Navigation目前項目被選取狀態



        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.reserve:
                        Intent intent=new Intent(MainpageActivity.this, ReserveActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.group:
                        break;
                    case R.id.start:
                        break;
                }

                return true;
            }
        });
    }
}
