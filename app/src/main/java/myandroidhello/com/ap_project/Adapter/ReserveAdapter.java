package myandroidhello.com.ap_project.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;

import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.model.Item;

/**
 * Created by Yehwenting on 2018/2/4.
 */


class MyViewHolderWithChild extends RecyclerView.ViewHolder{
    public TextView textView,textViewChild;
    public RelativeLayout button;
    public ExpandableLinearLayout expandableLayout;

    public MyViewHolderWithChild(View itemView) {
        super(itemView);
        textView=itemView.findViewById(R.id.parentTextView);
        textViewChild=itemView.findViewById(R.id.childTextView);
        button=itemView.findViewById(R.id.childButton);
        expandableLayout=itemView.findViewById(R.id.expendableLayout);

    }
}

public class ReserveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Item> items;
    Context context;
    SparseBooleanArray expendState= new SparseBooleanArray();

    public ReserveAdapter(List<Item> items) {
        this.items = items;
        for (int i=0;i<items.size();i++){
            expendState.append(i,false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position).getExpendable()){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
            LayoutInflater inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.layout_reserve_with_child,parent,false);
            return new MyViewHolderWithChild(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

                final MyViewHolderWithChild viewHolderWithChild=(MyViewHolderWithChild)holder;
                Item item=items.get(position);
                viewHolderWithChild.setIsRecyclable(false);
                viewHolderWithChild.textView.setText(item.getText());

                viewHolderWithChild.expandableLayout.setInRecyclerView(true);
                viewHolderWithChild.expandableLayout.setExpanded(expendState.get(position));
                viewHolderWithChild.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {

                    @Override
                    public void onPreOpen() {
                        changeRotate(viewHolderWithChild.button,180f,0f).start();
                        expendState.put(position,false);

                    }

                    @Override
                    public void onPreClose() {

                    }

                });

                viewHolderWithChild.button.setRotation(expendState.get(position)?180f:0f);
                viewHolderWithChild.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolderWithChild.expandableLayout.toggle();
                    }
                });

                viewHolderWithChild.textViewChild.setText(items.get(position).getSubtext());

        }


    private ObjectAnimator changeRotate(RelativeLayout button, float from, float to) {
        ObjectAnimator animator=ObjectAnimator.ofFloat(button,"rotation",from,to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
