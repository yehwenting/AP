package myandroidhello.com.ap_project.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.List;

import myandroidhello.com.ap_project.Activity.CreateCGroupActivity;
import myandroidhello.com.ap_project.R;
import myandroidhello.com.ap_project.model.Competitions;

/**
 * Created by Yehwenting on 2018/4/25.
 */

public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionAdapter.CompetitionViewHolder> {
    public class CompetitionViewHolder extends RecyclerView.ViewHolder {

        TextView name,content,num,constraint,deadline;
        Button create,findBtn;
        public ExpandableLinearLayout expandableLayout;
        RecyclerView recyclerView;

        public CompetitionViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.c_name);
            content= itemView.findViewById(R.id.uName);
            num=itemView.findViewById(R.id.c_number);
            constraint=itemView.findViewById(R.id.c_constraint);
            deadline=itemView.findViewById(R.id.c_deadline);
            create=itemView.findViewById(R.id.create_button);
            expandableLayout=itemView.findViewById(R.id.expendableLayout);
            findBtn=itemView.findViewById(R.id.detail);
            recyclerView=itemView.findViewById(R.id.cGroup_rv);

        }
    }

    private List<Competitions> competitions;
    Context context;
    SparseBooleanArray expendState= new SparseBooleanArray();

//    @Bind(R.id.cGroup_rv)
    RecyclerView recyclerView1;



    public CompetitionAdapter(List<Competitions> competitions) {
        this.competitions = competitions;
        for (int i=0;i<competitions.size();i++){
            expendState.append(i,false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(competitions.get(position).getExpendable()){
            return 1;
        }else {
            return 0;
        }
    }

    @NonNull
    @Override
    public CompetitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_compete,null);

        return new CompetitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompetitionViewHolder holder, final int position) {
        final Competitions competition=competitions.get(position);

        CompetitionGroupAdapter competitionGroupAdapter = new CompetitionGroupAdapter(competition.getCompetitions());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(competitionGroupAdapter);

        holder.name.setText(competition.getName());
        holder.content.setText(competition.getContent());
        holder.num.setText(competition.getNum());
        holder.constraint.setText(competition.getGoal());
        holder.deadline.setText(competition.getDeadline());
        holder.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, CreateCGroupActivity.class);
                intent.putExtra("activity",holder.name.getText());
                context.startActivity(intent);
            }
        });

        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setExpanded(expendState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
//                holder.main.setBackgroundColor(Color.parseColor("#F1E27B"));
//                changeRotate(viewHolderWithChild.buttonChild,180f,0f).start();
                Log.d("hhhhh","preopen");
                expendState.put(position,false);
            }

            @Override
            public void onPreClose() {
//                viewHolderWithChild.main.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });
        holder.findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.expandableLayout.toggle();
            }
        });

}

    @Override
    public int getItemCount() {
        return competitions.size();
    }

    private ObjectAnimator changeRotate(RelativeLayout button, float from, float to) {
        ObjectAnimator animator=ObjectAnimator.ofFloat(button,"rotation",from,to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }


}
