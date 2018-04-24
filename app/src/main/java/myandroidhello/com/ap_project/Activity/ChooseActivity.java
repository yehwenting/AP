package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import myandroidhello.com.ap_project.R;

public class ChooseActivity extends Navigation_BaseActivity {
    private ImageButton ButtonBad;
    private ImageButton ButtonBase;
    private ImageButton ButtonBasket;
    private ImageButton ButtonJog;
    private ImageButton ButtonTennis;
    private ImageButton ButtonVolley;
    private ImageButton ButtonPing;
    private ImageButton ButtonGym;
    private ImageButton ButtonOther;

    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picksport);

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
                                Intent intent=new Intent(ChooseActivity.this, ReserveActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.group:
                                Intent intent1=new Intent(ChooseActivity.this, JFGroupActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.start:
                                Intent intent2=new Intent(ChooseActivity.this, MenuActivity.class);
                                startActivity(intent2);
                                break;
                        }

                        return true;
                    }
                });

        ButtonBad = (ImageButton)findViewById(R.id.ButtonBad);
        ButtonBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this, MapsActivity.class);
                intent.putExtra("type","羽毛球");
                startActivity(intent);

            }
        });

        ButtonBase = (ImageButton)findViewById(R.id.ButtonBase);
        ButtonBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this, MapsActivity.class);
                intent.putExtra("type","棒球");
                startActivity(intent);

            }
        });

        ButtonBasket = (ImageButton)findViewById(R.id.ButtonBasket);
        ButtonBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this, MapsActivity.class);
                intent.putExtra("type","籃球");
                startActivity(intent);

            }
        });

        ButtonJog = (ImageButton)findViewById(R.id.ButtonJog);
        ButtonJog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this, MapsActivity.class);
                intent.putExtra("type","慢跑");
                startActivity(intent);

            }
        });

        ButtonTennis = (ImageButton)findViewById(R.id.ButtonTennis);
        ButtonTennis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this, MapsActivity.class);
                intent.putExtra("type","網球");
                startActivity(intent);

            }
        });
        ButtonVolley = (ImageButton)findViewById(R.id.ButtonVolley);
        ButtonVolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this, MapsActivity.class);
                intent.putExtra("type","排球");
                startActivity(intent);

            }
        });
        ButtonPing = (ImageButton)findViewById(R.id.ButtonPing);
        ButtonPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this, MapsActivity.class);
                intent.putExtra("type","桌球");
                startActivity(intent);

            }
        });

        ButtonGym = (ImageButton)findViewById(R.id.ButtonExer);
        ButtonGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this, MapsActivity.class);
                intent.putExtra("type","健身");
                startActivity(intent);

            }
        });

       ButtonOther = (ImageButton)findViewById(R.id.ButtonOther);
       ButtonOther.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent();
               intent.setClass(ChooseActivity.this, MapsActivity.class);
               intent.putExtra("type","其他運動");
               startActivity(intent);

           }
       });




    }
}
