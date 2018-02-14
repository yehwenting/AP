package myandroidhello.com.ap_project.Adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.model.Item;

/**
 * Created by Yehwenting on 2018/2/4.
 */



public class ReserveFakeFragment extends Fragment {

    List<Item> items=new ArrayList<>();
    @Bind(R.id.reserve_rv) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.adapter_reserve, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        for (int i=0;i<20;i++) {
            if (i % 2 == 0) {
                Item item = new Item("this is item" + (i + 1), "this is child item" + (i + 1), true);
                items.add(item);
            } else {
                Item item = new Item("this is item" + (i + 1), "", false);
                items.add(item);
            }
        }
        ReserveAdapter myAdapter=new ReserveAdapter(items);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(myAdapter);
    }

    public static Fragment newInstance() {
        return new ReserveFakeFragment();
    }

}
