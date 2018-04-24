package myandroidhello.com.ap_project.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.bubbletab.BubbleTab;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import myandroidhello.com.ap_project.Adapter.ReserveFakeAdapter;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.model.GlobalVariables;

public class ReserveActivity extends Navigation_BaseActivity {
    @Bind(R.id.bubbleTab)
    BubbleTab bubbleTab;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private TextView toolBar_title;
    private ImageView cart;
    private int mYear, mMonth, mDay;
    private String dateNum,id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        ButterKnife.bind(this);

        viewPager.setAdapter(new ReserveFakeAdapter(getSupportFragmentManager()));
        bubbleTab.setupWithViewPager(viewPager);
        toolBar_title=findViewById(R.id.toolbar_title);
        //toolbar
        toolbar.setTitle("");//設置ToolBar Title
        toolBar_title.setText(R.string.reserve);
        setUpToolBar();//使用父類別的setUpToolBar()，設置ToolBar
//        CurrentMenuItem = 0;
//        NV.getMenu().getItem(CurrentMenuItem).setChecked(true);//設置Navigation目前項目被選取狀態


        bottomNavigationView=findViewById(R.id.bottom_navigation);
        cart=findViewById(R.id.exerciseCart);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        dateNum=String.valueOf(mYear)+String.valueOf(mMonth+1)+String.valueOf(mDay);
        GlobalVariables User = (GlobalVariables)getApplicationContext();
        String uid= User.getId();
        id=uid+dateNum;


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReserveActivity.this,ReserveCheckActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("number",String.valueOf(id));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.reserve:
                                // 1. Instantiate an AlertDialog.Builder with its constructor
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReserveActivity.this);
                                // 2. Chain together various setter methods to set the dialog characteristics
                                builder.setMessage("確定要離開?");
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        recreate();
                                    }
                                });
                                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                                // 3. Get the AlertDialog from create()
                                AlertDialog dialog = builder.create();
                                dialog.show();
//                                Intent intent=new Intent(MainpageActivity.this, ReserveActivity.class);
//                                startActivity(intent);
                                break;
                            case R.id.group:
                                Intent intent1=new Intent(ReserveActivity.this, JFGroupActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.start:
                                Intent intent2=new Intent(ReserveActivity.this, MenuActivity.class);
                                startActivity(intent2);
                                break;
                        }

                        return true;
                    }
                });

    }
}
