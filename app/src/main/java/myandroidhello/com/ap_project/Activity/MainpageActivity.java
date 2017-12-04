package myandroidhello.com.ap_project.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import myandroidhello.com.ap_project.R;

public class MainpageActivity extends AppCompatActivity {

    private Button friendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        friendBtn=(Button)findViewById(R.id.friendBtn);

        friendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainpageActivity.this,FriendsActivity.class);
                startActivity(intent);

            }
        });
    }
}
