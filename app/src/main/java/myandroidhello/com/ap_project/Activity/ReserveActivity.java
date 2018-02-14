package myandroidhello.com.ap_project.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.github.florent37.bubbletab.BubbleTab;

import butterknife.Bind;
import butterknife.ButterKnife;
import myandroidhello.com.ap_project.Adapter.ReserveFakeAdapter;
import myandroidhello.com.ap_project.R;

public class ReserveActivity extends AppCompatActivity {
    @Bind(R.id.bubbleTab)
    BubbleTab bubbleTab;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        ButterKnife.bind(this);

        viewPager.setAdapter(new ReserveFakeAdapter(getSupportFragmentManager()));
        bubbleTab.setupWithViewPager(viewPager);
    }
}
