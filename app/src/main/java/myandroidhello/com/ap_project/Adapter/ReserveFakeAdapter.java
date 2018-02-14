package myandroidhello.com.ap_project.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Yehwenting on 2018/2/4.
 */

public class ReserveFakeAdapter extends FragmentStatePagerAdapter {
    public ReserveFakeAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            default:
                Log.d("position",Integer.toString(position));
                return ReserveFakeFragment.newInstance();
//                        FakeFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
