package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.Share.ShareActivity;

public class StartExercise2Activity extends Navigation_BaseActivity {

    private TextView exerciseSummary;
    private TextView equipName_tv;
    private Button postBtn;

    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise2);

        final Intent intent = this.getIntent();
        String exerciseDuration = intent.getStringExtra("exerciseDuration");
        String equipName = intent.getStringExtra("equipName");

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
                                Intent intent=new Intent(StartExercise2Activity.this, ReserveActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.group:
                                Intent intent1=new Intent(StartExercise2Activity.this, JFGroupActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.start:
                                Intent intent2=new Intent(StartExercise2Activity.this, MenuActivity.class);
                                startActivity(intent2);
                                break;
                        }

                        return true;
                    }
                });

        exerciseSummary = (TextView)findViewById(R.id.exerciseSummary);
        exerciseSummary.setText(exerciseDuration);

        equipName_tv = (TextView)findViewById(R.id.equip_name);
        equipName_tv.setText(equipName);


        postBtn = (Button)findViewById(R.id.postBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(StartExercise2Activity.this, ShareActivity.class);
                startActivity(intent);
            }
        });
    }
}
