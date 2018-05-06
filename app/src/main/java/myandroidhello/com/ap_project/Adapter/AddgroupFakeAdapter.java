package myandroidhello.com.ap_project.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Stanley on 2018/3/13.
 */

public class AddgroupFakeAdapter extends FragmentStatePagerAdapter {
     public AddgroupFakeAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Log.d("position",Integer.toString(position));
                return AddgroupFakeFragment.newInstance();
            case 1:
                return CreateGroupFragment.newInstance();
            case 2:
                return CompetitionFragment.newInstance();
            default:
                return AddgroupFakeFragment.newInstance();

        }


    }

    @Override
    public int getCount() {
         return 3;
    }
}
