package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import myandroidhello.com.ap_project.R;


public class MenuActivity extends Navigation_BaseActivity {

    private RelativeLayout gymBtn;
    private RelativeLayout othersBtn;
    private ImageView ic_gym;

    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        toolBar_title=findViewById(R.id.toolbar_title);

        //toolbar
        toolbar.setTitle(" ");//設置ToolBar Title
        toolBar_title.setText("開 始 運 動");
        setUpToolBar();//使用父類別的setUpToolBar()，設置ToolBar
//        CurrentMenuItem = 0;
//        NV.getMenu().getItem(CurrentMenuItem).setChecked(true);//設置Navigation目前項目被選取狀態
        //bottomNavigation
        bottomNavigationView.setSelectedItemId(R.id.start);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.reserve:
                                Intent intent=new Intent(MenuActivity.this, ReserveActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.group:
                                Intent intent1=new Intent(MenuActivity.this, JFGroupActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.start:
                                Intent intent2=new Intent(MenuActivity.this, MenuActivity.class);
                                startActivity(intent2);
                                break;
                        }

                        return true;
                    }
                });

        gymBtn = findViewById(R.id.gymLayout);
        gymBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, IndoorStartActivity.class);
                startActivity(intent);

            }
        });

        ic_gym = (ImageView)findViewById(R.id.ic_white_dumbbell);

        othersBtn = findViewById(R.id.other_layout);
        othersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ChooseActivity.class);
                startActivity(intent);
            }
        });
    }
}
