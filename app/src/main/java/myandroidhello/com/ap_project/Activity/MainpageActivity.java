package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import myandroidhello.com.ap_project.R;

public class MainpageActivity extends AppCompatActivity {

    private Button friendBtn;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        friendBtn=findViewById(R.id.friendBtn);
        bottomNavigationView=findViewById(R.id.bottom_navigation);

        friendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainpageActivity.this,FriendsActivity.class);
                startActivity(intent);
            }
        });

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
